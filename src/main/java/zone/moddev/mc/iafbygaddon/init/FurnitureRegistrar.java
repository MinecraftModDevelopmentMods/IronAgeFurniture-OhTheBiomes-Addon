package zone.moddev.mc.iafbygaddon.init;

import zone.moddev.mc.iafbygaddon.IAFOhTheBiomesAddon;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Blocks.BackBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Bench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Chair;
import zone.moddev.mc.ironagefurniture.api.Blocks.PaddedBackBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.PaddedBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Stool;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

/** Creates the catalog furniture while leaving all registry ownership with the add-on. */
public final class FurnitureRegistrar {
    private static boolean initialised;

    private FurnitureRegistrar() {
        throw new IllegalAccessError("This class cannot be instantiated");
    }

    public static void initialise() {
        if (initialised) {
            return;
        }
        initialised = true;

        for (GeneratedFurnitureCatalog.Wood wood : GeneratedFurnitureCatalog.WOODS) {
            String suffix = "byg_" + wood.id;
            String classic = id("classic", suffix);
            String shield = id("shield", suffix);
            String shortStool = id("stool_short", suffix);
            String tallStool = id("stool_tall", suffix);
            String bench = id("bench_single", suffix);
            String paddedBench = id("bench_padded_single", suffix);
            String logBench = id("bench_log_single", suffix);
            String backBench = id("bench_back_single", suffix);
            String paddedBackBench = id("bench_back_padded_single", suffix);

            register(new Chair(Material.WOOD, classic, 10.0F, 0.25D, 1.0F), classic, false);
            register(new Chair(Material.WOOD, shield, 10.0F, 0.25D, 1.0F), shield, false);
            register(new Stool(Material.WOOD, shortStool, 10.0F,
                    false, 0.25D, 1.0F), shortStool, false);
            register(new Stool(Material.WOOD, tallStool, 10.0F,
                    true, 0.6D, 1.0F), tallStool, false);
            register(new Bench(Material.WOOD, bench, 10.0F,
                    false, 0.25D, 1.0F), bench, false);
            register(new PaddedBench(Material.WOOD, paddedBench,
                    10.0F, false, 0.25D, 1.0F), paddedBench, true);
            register(new Bench(Material.WOOD, logBench, 10.0F,
                    false, 0.25D, 1.0F), logBench, false);
            register(new BackBench(Material.WOOD, backBench,
                    10.0F, false, 0.25D, 1.0F), backBench, false);
            register(new PaddedBackBench(Material.WOOD, paddedBackBench,
                    10.0F, false, 0.25D, 1.0F), paddedBackBench, true);
        }
    }

    private static String id(String form, String suffix) {
        return "chair_wood_ironage_" + form + "_" + suffix;
    }

    private static void register(Block block, String path, boolean padded) {
        ResourceLocation registryName = new ResourceLocation(IAFOhTheBiomesAddon.MODID, path);
        block.setRegistryName(registryName);
        block.setTranslationKey(IAFOhTheBiomesAddon.MODID + "." + path);
        block.setCreativeTab(Ironagefurniture.ironagefurnitureTab);

        ItemBlock item = padded ? new ItemBlockPaddedBench(block) : new ItemBlock(block);
        item.setRegistryName(registryName);
        item.setTranslationKey(IAFOhTheBiomesAddon.MODID + "." + path);
        item.setMaxStackSize(16);

        AddonRegistries.BLOCKS.put(path, block);
        AddonRegistries.ITEMS.put(path, item);
    }

}
