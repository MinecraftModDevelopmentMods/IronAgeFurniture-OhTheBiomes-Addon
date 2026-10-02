package zone.moddev.mc.iafbygaddon.init;

import zone.moddev.mc.iafbygaddon.IAFOhTheBiomesAddon;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Blocks.BackBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Bench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Chair;
import zone.moddev.mc.ironagefurniture.api.Blocks.PaddedBackBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.PaddedBench;
import zone.moddev.mc.ironagefurniture.api.Blocks.Stool;
import zone.moddev.mc.ironagefurniture.api.Blocks.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.Blocks.WingbackChair;
import zone.moddev.mc.ironagefurniture.api.Blocks.ThroneChair;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockWoodBed;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockBed;
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
            register(new ShieldChair(Material.WOOD, shield, 10.0F, 1.0F), shield, false);
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

            String wingback = id("wingback", suffix);
            String throne = id("throne", suffix);
            registerUpholstered(new WingbackChair(Material.WOOD, wingback, 10.0F, 1.0F), wingback, true);
            registerUpholstered(new ThroneChair(Material.WOOD, throne, 10.0F, 1.0F), throne, true);

            String wooden = "bed_wood_foot_" + suffix;
            String woodenDouble = "bed_wood_foot_left_" + suffix;
            registerUpholstered(new MultiBlockWoodBed(Material.WOOD, wooden, 10.0F, 3.0F, false), wooden, true);
            registerUpholstered(new MultiBlockWoodBed(Material.WOOD, woodenDouble, 10.0F, 6.0F, true), woodenDouble, true);

            String canopy = "bed_canopy_foot_lower_" + suffix;
            String canopyLeft = "bed_canopy_foot_left_lower_" + suffix;
            String canopyRight = "bed_canopy_foot_right_lower_" + suffix;
            MultiBlockBed single = new MultiBlockBed(Material.WOOD, canopy, 10.0F, 3.0F, MultiBlockBed.SINGLE_SIDE);
            MultiBlockBed left = new MultiBlockBed(Material.WOOD, canopyLeft, 10.0F, 6.0F, MultiBlockBed.LEFT_SIDE);
            MultiBlockBed right = new MultiBlockBed(Material.WOOD, canopyRight, 10.0F, 6.0F, MultiBlockBed.RIGHT_SIDE);
            registerUpholstered(single, canopy, true);
            registerUpholstered(left, canopyLeft, true);
            registerUpholstered(right, canopyRight, false);
            single.setSingleBlock(single);
            left.setDoubleBlocks(left, right);
            right.setDoubleBlocks(left, right);
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

    private static void registerUpholstered(Block block, String path, boolean visible) {
        ResourceLocation name = new ResourceLocation(IAFOhTheBiomesAddon.MODID, path);
        block.setRegistryName(name);
        block.setTranslationKey(IAFOhTheBiomesAddon.MODID + "." + path);
        AddonRegistries.BLOCKS.put(path, block);
        // The right canopy partner is placed by the visible left item, never independently.
        block.setCreativeTab(null);
        if (visible) {
            block.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
            ItemBlock item = new ItemBlockColouredFurniture(block);
            item.setRegistryName(name);
            item.setTranslationKey(IAFOhTheBiomesAddon.MODID + "." + path);
            item.setMaxStackSize(16);
            AddonRegistries.ITEMS.put(path, item);
        }
    }

}
