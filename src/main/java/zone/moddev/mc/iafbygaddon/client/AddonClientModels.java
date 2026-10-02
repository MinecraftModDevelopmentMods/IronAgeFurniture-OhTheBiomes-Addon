package zone.moddev.mc.iafbygaddon.client;

import zone.moddev.mc.iafbygaddon.IAFOhTheBiomesAddon;
import zone.moddev.mc.ironagefurniture.client.model.PaddedBenchModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Registers loaders before Forge discovers the add-on's block and inventory models. */
@SideOnly(Side.CLIENT)
public final class AddonClientModels {
    private AddonClientModels() { }

    public static void initialise() {
        PaddedBenchModelLoader.registerNamespace(IAFOhTheBiomesAddon.MODID);
        ModelLoaderRegistry.registerLoader(AddonUpholsteryModelLoader.INSTANCE);
    }
}
