package zone.moddev.mc.iafbygaddon.client;

import java.util.Map;

import zone.moddev.mc.iafbygaddon.IAFOhTheBiomesAddon;
import zone.moddev.mc.iafbygaddon.init.AddonRegistries;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;
import zone.moddev.mc.ironagefurniture.init.ClientModelInitialiser;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/** Client-only inventory model registration. */
@Mod.EventBusSubscriber(modid = IAFOhTheBiomesAddon.MODID, value = Side.CLIENT)
public final class ClientRegistryEvents {
    private ClientRegistryEvents() {
        throw new IllegalAccessError("This class cannot be instantiated");
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Map.Entry<String, Item> entry : AddonRegistries.ITEMS.entrySet()) {
            if (entry.getValue() instanceof ItemBlockPaddedBench) {
                ClientModelInitialiser.registerPaddedBenchItemModel(
                        IAFOhTheBiomesAddon.MODID, entry.getKey(), entry.getValue());
            } else {
                ModelLoader.setCustomModelResourceLocation(entry.getValue(), 0,
                        new ModelResourceLocation(
                                IAFOhTheBiomesAddon.MODID + ":" + entry.getKey(),
                                "inventory"));
            }
        }
    }
}
