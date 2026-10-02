package zone.moddev.mc.iafbygaddon.client;

import java.util.Map;

import zone.moddev.mc.iafbygaddon.IAFOhTheBiomesAddon;
import zone.moddev.mc.iafbygaddon.init.AddonRegistries;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockUpholsteredFurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
import zone.moddev.mc.ironagefurniture.init.ClientModelInitialiser;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.util.ResourceLocation;
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
            } else if (entry.getValue() instanceof ItemBlockUpholsteredFurniture) {
                final String path = entry.getKey();
                ResourceLocation[] variants = new ResourceLocation[16];
                for (UpholsteryColour colour : UpholsteryColour.values()) {
                    variants[colour.getItemMetadata()] = colouredItem(path, colour);
                }
                ModelBakery.registerItemVariants(entry.getValue(), variants);
                ModelLoader.setCustomMeshDefinition(entry.getValue(),
                        stack -> colouredItem(path, UpholsteryColourHelper.getColour(stack)));
            } else {
                ModelLoader.setCustomModelResourceLocation(entry.getValue(), 0,
                        new ModelResourceLocation(
                                IAFOhTheBiomesAddon.MODID + ":" + entry.getKey(),
                                "inventory"));
            }
        }
    }

    private static ModelResourceLocation colouredItem(String path, UpholsteryColour colour) {
        return new ModelResourceLocation(IAFOhTheBiomesAddon.MODID + ":upholstered/"
                + colour.getSerializedName() + "/" + path, "inventory");
    }
}
