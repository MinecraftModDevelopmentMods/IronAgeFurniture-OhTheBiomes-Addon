# Upgrading and compatibility

Phase 4 keeps every Phase 3 block and item registry name in `iafbygaddon`.
It adds new furniture rather than renaming old pieces. No world-conversion
command or numeric-ID reassignment is needed for this same-Minecraft-version
upgrade; Forge restores the world's saved registry mapping.

## Upholstery and shields

Padded benches retain the core's stable `Color` NBT string and legacy item
metadata. Missing colour information represents red. The new upholstered chairs
and beds use the same colour names, red-first metadata order and NBT preference.
Valid NBT wins over conflicting item metadata; invalid colour names become red.

Benches of different colours can still connect. Each segment keeps its own
colour, and the add-on does not replace the core's joining algorithm.

Shield chairs now use IAF's shield tile entity without changing their block ID.
An old chair with no shield NBT keeps a plain shield. New chairs preserve the
complete supplied shield stack. Removing or replacing a shield updates that
stored stack; ordinary mining drops the empty frame and shield separately.

New multiblock furniture uses the core's structure and colour handling. Only
visible base parts have items. The right-hand canopy partner is a hidden block.

## Verification

The runtime probes build a disposable world with Phase 3 IAF and the Phase 3
add-on, then open a copy with both Phase 4 jars. They cover all 27 woods, both
padded bench forms and all sixteen colours, saved connection states, the seven
other original furniture forms, facing, pick-block and drops, chest
items, nested shulker contents and old untagged red stacks. A second load checks
that the saved results remain stable.

Separate Phase 4 probes exercise the loaded crafting recipes, new chairs and
beds, shield-item data, inventory models and save/reload. Both the official BYG
1.7.1 and community-fixed 1.9 jars are compatibility targets.

The original BYG 1.7.1 jar has three unrelated recipes which refer to its removed
`parrotegg` item. Its startup errors for `byg:rsbpp`, `byg:rgapp` and `byg:rbbpp`
come from BYG, not this add-on. Community-fixed 1.9 removes those errors, but it
also removes five older BYG entities. Switching an existing world from 1.7.1
to 1.9 therefore triggers Forge's missing-entity confirmation. Back up the world
and consider that upstream change before replacing BYG; updating the add-on
alone does not require removing any furniture.

BYG 1.7.1's client also reports four missing texture paths: an empty block-texture
name, `food_ruto_seeds1`, `kasai ore` and `food_roasted ruto1`. None is used by
the furniture catalog. Its actual plank and log textures are checked separately
for every supported wood.

These are 1.12.2 add-on upgrade tests, not a claim that an entire older modpack
or world can skip Minecraft's flattening upgrade steps. Always retain a backup
before updating your own pack. Hands-on checks remain useful for appearance,
seating, sleep and resource-pack choices.
