package zone.moddev.mc.iafbygaddon.test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import zone.moddev.mc.ironagefurniture.api.PaddedBenchColourHelper;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Enumerations.BenchType;
import zone.moddev.mc.ironagefurniture.api.Blocks.BackBench;

/** Creates and reloads only disposable test worlds, including the old add-on format. */
@Mod(modid = RuntimeProbe.MODID, name = "IAF BYG runtime tests", version = "1",
        dependencies = "required-after:ironagefurniture;required-after:iafbygaddon")
public final class RuntimeProbe {
    public static final String MODID = "iafbygruntimeprobe";
    @Mod.EventHandler public void started(FMLServerStartedEvent event) throws Exception {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        verifyLegacyFurniture(server);
        if (!Boolean.getBoolean("iafbyg.probe.phase3")) PhaseFourProbe.verify(server);
        Files.write(Paths.get("iafbyg-probe-pass.txt"), "PASS\n".getBytes(StandardCharsets.UTF_8));
        LogManager.getLogger().info("IAF BYG RUNTIME PROBE PASSED");
        server.initiateShutdown();
    }

    private static void verifyLegacyFurniture(MinecraftServer server) {
        WorldServer world = server.getWorld(0);
        EntityPlayer player = FakePlayerFactory.getMinecraft(world);
        int woodIndex = 0;
        for (Block block : ForgeRegistries.BLOCKS) {
            ResourceLocation name = block.getRegistryName();
            if (!"iafbygaddon".equals(name.getNamespace())
                    || !(name.getPath().startsWith("chair_wood_ironage_bench_padded_single_")
                        || name.getPath().startsWith("chair_wood_ironage_bench_back_padded_single_"))) continue;
            for (PaddedBenchColour colour : PaddedBenchColour.values()) {
                BlockPos pos = new BlockPos(32 + woodIndex * 4, 70, 32 + colour.getItemMetadata() * 4);
                EnumFacing facing = EnumFacing.byHorizontalIndex(colour.getItemMetadata() % 4);
                if (world.isAirBlock(pos)) {
                    world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
                    net.minecraft.item.ItemBlock item = (net.minecraft.item.ItemBlock)Item.getItemFromBlock(block);
                    check(item.placeBlockAt(PaddedBenchColourHelper.createStack(block, 1, colour),
                            player, world, pos, EnumFacing.UP, 0.5F, 0.5F, 0.5F,
                            block.getDefaultState().withProperty(BlockHorizontal.FACING, facing)),
                            "Could not place legacy bench");
                    world.setBlockState(pos.up(3), Blocks.CHEST.getDefaultState(), 2);
                    TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos.up(3));
                    chest.setInventorySlotContents(0, PaddedBenchColourHelper.createStack(block, 3, colour));
                    // Old damage-zero stacks, with no NBT, must remain red.
                    chest.setInventorySlotContents(1, new ItemStack(block, 2, 0));
                    NBTTagCompound nested = new NBTTagCompound();
                    net.minecraft.nbt.NBTTagList contents = new net.minecraft.nbt.NBTTagList();
                    contents.appendTag(PaddedBenchColourHelper.createStack(block, 4, colour).writeToNBT(new NBTTagCompound()));
                    nested.setTag("Items", contents);
                    ItemStack box = new ItemStack(Blocks.WHITE_SHULKER_BOX);
                    NBTTagCompound boxTag = new NBTTagCompound();
                    boxTag.setTag("BlockEntityTag", nested);
                    box.setTagCompound(boxTag);
                    chest.setInventorySlotContents(2, box);
                }
                if (block instanceof BackBench) {
                    BenchType joined = BenchType.values()[colour.getItemMetadata() / 4];
                    if (Boolean.getBoolean("iafbyg.probe.phase3")) {
                        world.setBlockState(pos, world.getBlockState(pos).withProperty(BackBench.TYPE, joined), 2);
                    }
                    check(world.getBlockState(pos).getValue(BackBench.TYPE) == joined, "Connected back-bench state changed");
                }
                check(world.getBlockState(pos).getBlock() == block, "Legacy ID changed: " + name);
                check(world.getBlockState(pos).getValue(BlockHorizontal.FACING) == facing, "Legacy facing changed");
                check(PaddedBenchColourHelper.getColour(world, pos) == colour, "Legacy colour changed: " + name);
                ItemStack pick = block.getPickBlock(world.getBlockState(pos), null, world, pos, player);
                check(pick.getItem() == Item.getItemFromBlock(block)
                        && PaddedBenchColourHelper.getColour(pick) == colour, "Pick colour changed");
                check(PaddedBenchColourHelper.getColour(block.getDrops(world, pos, world.getBlockState(pos), 0).get(0)) == colour,
                        "Padded bench drop lost colour");
                TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos.up(3));
                check(PaddedBenchColourHelper.getColour(chest.getStackInSlot(0)) == colour, "Stored colour changed");
                check(PaddedBenchColourHelper.getColour(chest.getStackInSlot(1)) == PaddedBenchColour.RED,
                        "Red-only item changed");
                ItemStack nested = new ItemStack(chest.getStackInSlot(2).getTagCompound()
                        .getCompoundTag("BlockEntityTag").getTagList("Items", 10).getCompoundTagAt(0));
                check(PaddedBenchColourHelper.getColour(nested) == colour && nested.getCount() == 4,
                        "Nested item changed");
            }
            ++woodIndex;
        }
        check(woodIndex == 54, "Expected all 27 woods and both padded forms");
        int plainForms = 0;
        for (Block block : ForgeRegistries.BLOCKS) {
            String path = block.getRegistryName().getPath();
            if (!"iafbygaddon".equals(block.getRegistryName().getNamespace()) || !path.matches(
                    "chair_wood_ironage_(classic|shield|stool_short|stool_tall|bench_single|bench_log_single|bench_back_single)_byg_.*")) continue;
            BlockPos pos = new BlockPos(32 + plainForms * 4, 74, 720);
            EnumFacing facing = EnumFacing.byHorizontalIndex(plainForms % 4);
            if (world.isAirBlock(pos)) {
                world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
                world.setBlockState(pos, block.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 2);
            }
            check(world.getBlockState(pos).getBlock() == block, "Legacy furniture ID changed " + path);
            check(world.getBlockState(pos).getValue(BlockHorizontal.FACING) == facing, "Legacy furniture facing changed");
            if (!Boolean.getBoolean("iafbyg.probe.phase3") && path.startsWith("chair_wood_ironage_shield_")) {
                ItemStack picked = block.getPickBlock(world.getBlockState(pos), null, world, pos, player);
                check(zone.moddev.mc.ironagefurniture.api.ShieldChairItemData.getShield(picked).getItem() == Items.SHIELD,
                        "Old shield chair lost its plain shield");
            }
            ++plainForms;
        }
        check(plainForms == 189, "Expected all seven unpadded forms for 27 woods");
        LogManager.getLogger().info("IAF BYG LEGACY SAVE PROBE PASSED: 54 forms x 16 colours, stored and nested items");
    }
    public static void check(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
