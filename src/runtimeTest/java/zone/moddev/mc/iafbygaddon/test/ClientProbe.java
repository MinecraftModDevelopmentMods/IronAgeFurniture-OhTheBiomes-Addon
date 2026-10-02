package zone.moddev.mc.iafbygaddon.test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import org.apache.logging.log4j.LogManager;

/** Checks all inventory variants after the real client has finished baking models. */
@Mod.EventBusSubscriber(modid = RuntimeProbe.MODID, value = Side.CLIENT)
public final class ClientProbe {
    private static boolean finished;
    private ClientProbe() { }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) throws Exception {
        if (finished || !Boolean.getBoolean("iafbyg.probe.client") || event.phase != TickEvent.Phase.END) return;
        Minecraft client = Minecraft.getMinecraft();
        if (!(client.currentScreen instanceof GuiMainMenu)) return;
        finished = true;
        PhaseFourProbe.verifyRecipes();
        int models = 0;
        for (Item item : ForgeRegistries.ITEMS) {
            if (!"iafbygaddon".equals(item.getRegistryName().getNamespace())) continue;
            NonNullList<ItemStack> variants = NonNullList.create();
            item.getSubItems(Ironagefurniture.ironagefurnitureTab, variants);
            for (ItemStack stack : variants) {
                IBakedModel model = client.getRenderItem().getItemModelWithOverrides(stack, null, null);
                RuntimeProbe.check(model != null && model != client.getRenderItem().getItemModelMesher()
                        .getModelManager().getMissingModel(), "Missing inventory model " + stack);
                RuntimeProbe.check(!model.getQuads(null, null, 0).isEmpty(), "Empty inventory geometry " + stack);
                RuntimeProbe.check(!stack.getDisplayName().contains(".name"), "Untranslated item " + stack);
                ++models;
            }
        }
        RuntimeProbe.check(models == 3645, "Missing Creative variants: " + models);
        int blocks = 0;
        for (Block block : ForgeRegistries.BLOCKS) {
            if (!"iafbygaddon".equals(block.getRegistryName().getNamespace())) continue;
            for (IBlockState state : block.getBlockState().getValidStates()) {
                IBakedModel model = client.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
                RuntimeProbe.check(model != client.getRenderItem().getItemModelMesher().getModelManager().getMissingModel(),
                        "Missing placed model " + state);
                boolean geometry = !model.getQuads(state, null, 0).isEmpty();
                for (EnumFacing face : EnumFacing.values()) geometry |= !model.getQuads(state, face, 0).isEmpty();
                RuntimeProbe.check(geometry, "Empty placed model " + state);
            }
            ++blocks;
        }
        RuntimeProbe.check(blocks == 432, "Missing block registrations: " + blocks);
        Files.write(client.gameDir.toPath().resolve("iafbyg-client-pass.txt"),
                ("PASS models=" + models + "\n").getBytes(StandardCharsets.UTF_8));
        LogManager.getLogger().info("IAF BYG CLIENT PROBE PASSED: {} inventory variants, {} block-state families", models, blocks);
        client.shutdown();
    }
}
