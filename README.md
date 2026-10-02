# Iron Age Furniture Oh The Biomes Add-On

Bring the forests of Oh The Biomes You'll Go into your home with matching Iron
Age Furniture. This Forge 1.12.2 Phase 4 update supports all 27 BYG wood families,
from Aspen and Baobab to Witch Hazel and Zelkova.

Alongside classic and shield chairs, stools and benches, each wood now has
wingback chairs, thrones, wooden beds and canopy beds. Padded benches, wingback
chairs, thrones and beds come in all sixteen Minecraft upholstery colours.
Lighting and metal sconces are supplied by the required Iron Age Furniture
core, so the add-on does not duplicate them.

## Requirements

- Minecraft 1.12.2 and Java 8
- Forge 14.23.5.2859 or newer in the 14.x series
- Iron Age Furniture 0.4.0.112021 or newer in the 0.4 series
- Oh The Biomes You'll Go 1.7.1 or newer in the 1.x series

[Official BYG 1.7.1](https://www.curseforge.com/minecraft/mc-mods/oh-the-biomes-youll-go/files/2833179)
is the supported baseline. The separately maintained
[BYG 1.12.2 Fixed 1.9](https://www.curseforge.com/minecraft/mc-mods/biomes-youll-go-1-12-2-fixed/files/8169771)
can be substituted manually if that is the version used by your pack.

Official BYG 1.7.1 contains three broken recipes referring to its removed
`parrotegg` item, and may report cascading world-generation warnings. Those
messages come from BYG rather than the add-on. The community-fixed build
removes the recipe errors.

## Making your furniture

Use the matching BYG planks and logs for each wood family. The furniture appears
in Iron Age Furniture's Creative tab, and its recipe-book entries keep different
woods, designs and colours separate.

Craft a vanilla bed with one matching plank to make a wooden bed in the same
colour. Add another matching plank to turn it into a canopy bed. Two matching
single beds make a double bed; their wood, style and colour must agree.
Craft any IAF bed with carpet to change its upholstery deliberately.

A wingback chair uses carpet, matching planks and a classic chair. Use a
wingback chair instead of the classic chair to make a throne.

Shield chairs keep the actual shield used to craft them, including its pattern,
damage, enchantments and name. Sneak-right-click with an empty hand to remove
it, then right-click with a shield to attach one again. An empty frame remains
usable as a chair.

Wooden furniture is flammable. Beds and tall chairs are one structure: breaking
a part removes the whole piece rather than leaving detached fragments.

## Upgrading an existing world

Back up your world before upgrading, and update both this add-on and Iron Age
Furniture together. The existing `iafbygaddon` furniture names have not changed.
Old benches keep their colours, facing and connected state; old red-only items
remain red. Old shield chairs start with their original plain shield.

See [Upgrading and compatibility](docs/COMPATIBILITY.md) for the saved-data
contract and the runtime checks used for this update.

## Resource packs and languages

The models reference BYG's wood textures directly. A resource pack that replaces
those textures also changes the matching furniture. Upholstery uses vanilla
coloured wool textures, including resource-pack replacements.

Language files cover the same seventeen locale choices as the 1.12 Phase 4
core. BYG wood names remain recognisable across languages.

## Why an add-on?

Installing BYG furniture only where it is wanted keeps the core download and
legacy registry footprint smaller. The add-on requires both parent mods and
owns its furniture in the stable `iafbygaddon` namespace.

Some later Minecraft versions already include BYG or Oh The Biomes We've Gone
support in Iron Age Furniture itself. If the add-on is not available for your
Minecraft version, check that version's core integrations first. Future versions
of this project will cover Oh The Biomes We've Gone where a compatible build
exists and the core does not already include it.

## For pack makers and contributors

- Mod ID and registry/resource namespace: `iafbygaddon`
- Maven coordinate: `zone.moddev.mc:iron-age-furniture-oh-the-biomes-addon:0.4.0.112021`
- Phase 4: 432 block registrations and 405 item registrations
- Phase 3: 243 block registrations and 243 item registrations

No new tile-entity IDs are introduced: the add-on uses IAF's colour and shield
data types. The hidden right-hand canopy partner has no item or Creative entry.
Players who do not install this add-on incur no extra BYG furniture IDs.

See [Building and testing](docs/BUILDING.md) for the Java toolchains, explicit
catalog-generation command and separate runtime probes.

## License

GNU Lesser General Public License v2.1. The add-on uses the public Iron Age
Furniture API and references BYG textures; it redistributes neither mod's
classes or artwork.
