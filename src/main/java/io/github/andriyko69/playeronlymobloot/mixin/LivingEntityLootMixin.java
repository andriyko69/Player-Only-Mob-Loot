package io.github.andriyko69.playeronlymobloot.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLootMixin {

    @Shadow
    protected abstract boolean shouldDropLoot();

    @Unique
    private boolean playerOnlyMobLoot$isPlayerInvolved;

    @Inject(method = "hurt", at = @At("RETURN"))
    private void playerOnlyMobLoot$rememberPlayerDamage(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = damageSource.getEntity();
        Entity directAttacker = damageSource.getDirectEntity();

        if (attacker instanceof Player || directAttacker instanceof Player) {
            this.playerOnlyMobLoot$isPlayerInvolved = true;
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void playerOnlyMobLoot$preventInvalidLoot(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (self instanceof Player) {
            return;
        }

        if (!this.shouldDropLoot()) {
            return;
        }

        if (this.playerOnlyMobLoot$isPlayerInvolved) {
            return;
        }

        Entity attacker = damageSource.getEntity();
        Entity directAttacker = damageSource.getDirectEntity();

        if (playerOnlyMobLoot$isAllowedKill(self, attacker, directAttacker)) {
            return;
        }

        ci.cancel();
    }

    @Unique
    private static boolean playerOnlyMobLoot$isAllowedKill(LivingEntity killed, Entity attacker, Entity directAttacker) {
        Entity source = attacker != null ? attacker : directAttacker;

        if (source instanceof Wolf wolf && wolf.isTame()) {
            return true;
        }

        if (source instanceof AbstractSkeleton && killed instanceof Creeper) {
            return true;
        }

        return source instanceof Creeper creeper && creeper.canDropMobsSkull();
    }
}