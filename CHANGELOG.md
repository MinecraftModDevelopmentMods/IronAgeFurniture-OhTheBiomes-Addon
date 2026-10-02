# Changelog

## 0.4.0.112021

- Added wingback chairs, thrones, single and double wooden beds, and single and double canopy beds for all 27 BYG wood families.
- Added sixteen upholstery colours to the new chairs and beds, using IAF's saved colour format and colour-preserving drops.
- Added colour-preserving bed recipes: vanilla bed and plank, wooden bed and matching plank, matching singles into a double, and deliberate carpet recolouring.
- Updated shield chairs to preserve removable shields, including patterns, damage, enchantments and names. Old chairs keep a plain shield.
- Separated recipe-book entries by furniture design, wood and upholstery colour.
- Added matching locale key sets for the seventeen language choices used by the Phase 4 core.
- Preserved all existing add-on furniture IDs and padded-bench data.
- Split the build scripts by responsibility and kept runtime probes out of production artifacts and Eclipse launches.
- Updated the required IAF core to the checksum-pinned 0.4.0.112021 release. BYG 1.7.1 remains the supported baseline.

## 0.3.0.112021

- Added matching Iron Age Furniture for all 27 wood families in the official Oh The Biomes You'll Go 1.7.1 release for Minecraft 1.12.2.
- Added classic and shield chairs, short and tall stools, plain benches, log benches, back benches and both padded bench styles for every wood family.
- Added all 16 upholstery colours to both padded bench styles, preserving colours through crafting, placement, drops, pick block and world reloads.
- Added exact recipe advancements so furniture unlocks from its own ingredients rather than unrelated materials.
- Added compatibility verification for both official BYG 1.7.1 and the community-maintained BYG 1.12.2 Fixed 1.9 build.
- Fixed Great Oak furniture so every model uses BYG's published Great Oak plank and log textures.
- Fixed the generated Eclipse launch configurations so they consistently target the imported add-on project.
- Added deterministic data generation, reproducible release jars and guarded release automation.
