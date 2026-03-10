# Player Only Mob Loot

Player Only Mob Loot changes mob drops so that loot is only dropped when a player is involved in the kill.

## Behavior

Mob loot will only drop if:

- the mob was damaged by a player at least once
- the mob was killed by a tamed wolf
- a skeleton killed a creeper
- a charged creeper caused the kill (mob head mechanic)

If none of these conditions are met, the mob will not drop loot, equipment, or experience.

## Examples

Allowed:

- player kills a mob
- player hits a mob and it later dies from fall damage or fire
- a tamed wolf kills a mob
- a skeleton kills a creeper
- a charged creeper produces a mob head

Blocked:

- mobs dying from fall damage, fire, lava, etc. without player involvement
- mobs killed by other mobs
- wolves damaging a mob that later dies from environmental damage