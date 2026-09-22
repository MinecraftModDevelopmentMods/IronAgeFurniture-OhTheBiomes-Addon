package zone.moddev.mc.iafbygaddon;

import zone.moddev.mc.iafbygaddon.init.FurnitureRegistrar;
import zone.moddev.mc.ironagefurniture.client.model.PaddedBenchModelLoader;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/** Entry point for the Forge 1.12.2 Oh The Biomes add-on. */
@Mod(
        modid = IAFOhTheBiomesAddon.MODID,
        name = IAFOhTheBiomesAddon.NAME,
        version = IAFOhTheBiomesAddon.VERSION,
        dependencies = IAFOhTheBiomesAddon.DEPENDENCIES,
        useMetadata = true)
public final class IAFOhTheBiomesAddon {
    public static final String MODID = "iafbygaddon";
    public static final String NAME = "Iron Age Furniture Oh The Biomes Add-On";
    public static final String VERSION = "0.3.0.112021";
    public static final String DEPENDENCIES =
            "required-after:forge@[14.23.5.2859,15);"
            + "required-after:ironagefurniture@[0.3.0.112021,0.4);"
            + "required-after:byg@[1.7.1,2)";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide().isClient()) {
            PaddedBenchModelLoader.registerNamespace(MODID);
        }
        FurnitureRegistrar.initialise();
    }
}
