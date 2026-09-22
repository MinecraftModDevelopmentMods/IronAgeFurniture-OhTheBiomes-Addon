# Iron Age Furniture Oh The Biomes Add-On

Bring the forests of Oh The Biomes You'll Go into your home with matching Iron Age Furniture chairs, stools and benches.

The Forge 1.12.2 release supports all 27 BYG wood families, from Aspen and Baobab to Witch Hazel and Zelkova. Each family includes classic and shield chairs, short and tall stools, plain and log benches, back benches, and padded benches in all 16 Minecraft colours.

## Requirements

- Minecraft 1.12.2
- Forge 14.23.5.2859 or newer in the 14.x series
- Iron Age Furniture 0.3.0.112021 or newer in the 0.3 series
- Oh The Biomes You'll Go 1.7.1 or newer in the 1.x series

The official BYG 1.7.1 release is the supported baseline. The add-on is also tested with the separately maintained BYG 1.12.2 Fixed 1.9 build, which can be substituted manually if that is the version used by your pack.

BYG 1.7.1 itself logs three missing `parrotegg` recipe errors and may report cascading world-generation warnings. These are upstream BYG 1.7.1 issues rather than missing add-on furniture. The community-fixed 1.9 build removes the recipe errors in our compatibility test.

## Why an add-on?

Installing the BYG furniture only where it is wanted keeps Iron Age Furniture's core download and legacy registry footprint smaller. The add-on requires both parent mods and registers its furniture in the stable `iafbygaddon` namespace.

Some later Iron Age Furniture versions already include BYG or Oh The Biomes We've Gone support in the main mod. If this add-on is not published for your Minecraft version, check the Iron Age Furniture description for that version's built-in integrations. Future versions of this project will cover Oh The Biomes We've Gone where a compatible release is available and the integration is not already built into Iron Age Furniture.

## Resource packs

The furniture models refer directly to the wood textures supplied by BYG. No BYG artwork is bundled in this add-on, and resource packs which replace BYG's wood textures will also update the matching furniture.

## For pack makers

- Mod ID: `iafbygaddon`
- Registry and resource namespace: `iafbygaddon`
- Maven coordinate: `zone.moddev.mc:iron-age-furniture-oh-the-biomes-addon:0.3.0.112021`

The first 1.12.2 release adds 243 block registrations and 243 item registrations. Existing Iron Age Furniture worlds are unchanged when the add-on is installed.

## License

Iron Age Furniture Oh The Biomes Add-On is licensed under the GNU Lesser General Public License v2.1. It uses the public Iron Age Furniture API and references textures supplied by BYG; it does not redistribute either mod's assets.
