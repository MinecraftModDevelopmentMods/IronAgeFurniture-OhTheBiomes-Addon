package zone.moddev.mc.iafbygaddon.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityShieldChair;

/** Verifies actual loaded recipes, new structures, colour drops and shield NBT. */
public final class PhaseFourProbe {
    private PhaseFourProbe() { }
    public static void verify(MinecraftServer server) {
        verifyRecipes();
        WorldServer world = server.getWorld(0);
        EntityPlayer player = FakePlayerFactory.getMinecraft(world);
        String[] forms = {"chair_wood_ironage_wingback_", "chair_wood_ironage_throne_",
                "bed_wood_foot_", "bed_wood_foot_left_", "bed_canopy_foot_lower_", "bed_canopy_foot_left_lower_"};
        int woods = 0;
        for (Block bed : ForgeRegistries.BLOCKS) {
            ResourceLocation name = bed.getRegistryName();
            if (!"iafbygaddon".equals(name.getNamespace()) || !name.getPath().startsWith("bed_wood_foot_byg_")) continue;
            String suffix = name.getPath().substring("bed_wood_foot_".length());
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                String recipeId = name.getPath() + (colour == UpholsteryColour.RED ? "" : "_" + colour.getSerializedName());
                IRecipe wooden = recipe(recipeId);
                ItemStack planks = wooden.getIngredients().get(1).getMatchingStacks()[0].copy();
                ItemStack vanilla = new ItemStack(Items.BED, 1, colour.getCarpetMetadata());
                craft(wooden, grid(vanilla, planks), bed, colour);
                IRecipe canopy = recipe("bed_canopy_foot_lower_" + suffix + "_" + colour.getSerializedName());
                ItemStack source = UpholsteryColourHelper.createStack(bed, 1, colour);
                source.setItemDamage((colour.getItemMetadata() + 1) % 16);
                Block canopyBlock = block("bed_canopy_foot_lower_" + suffix);
                craft(canopy, grid(source, planks), canopyBlock, colour);
                craft(canopy, grid(new ItemStack(bed, 1, colour.getItemMetadata()), planks), canopyBlock, colour);
                check(!canopy.matches(grid(source, new ItemStack(Blocks.DIRT)), null), "Wrong canopy wood accepted");
                InventoryCrafting extra = grid(source, planks);
                extra.setInventorySlotContents(4, new ItemStack(Items.STICK));
                check(!canopy.matches(extra, null), "Extra canopy ingredient accepted");
                ItemStack invalid = source.copy();
                invalid.getTagCompound().setString("Color", "invalid");
                if (colour == UpholsteryColour.RED) craft(canopy, grid(invalid, planks), canopyBlock, colour);
                IRecipe pair = recipe("bed_wood_foot_left_" + suffix);
                craft(pair, grid(source, source), block("bed_wood_foot_left_" + suffix), colour);
                ItemStack other = UpholsteryColourHelper.createStack(bed, 1,
                        UpholsteryColour.values()[(colour.ordinal() + 1) % 16]);
                check(!pair.matches(grid(source, other), null), "Mixed-colour double bed accepted");
                IRecipe recolour = recipe(name.getPath() + "_recolour");
                craft(recolour, grid(other, new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata())), bed, colour);
                for (int form = 0; form < forms.length; ++form) {
                    Block furniture = block(forms[form] + suffix);
                    BlockPos pos = new BlockPos(320 + woods * 5, 70, 32 + form * 100 + colour.getItemMetadata() * 5);
                    EnumFacing facing = EnumFacing.byHorizontalIndex(colour.getItemMetadata() % 4);
                    if (world.isAirBlock(pos)) {
                        for (int x = -2; x <= 2; ++x) for (int z = -2; z <= 2; ++z)
                            world.setBlockState(pos.add(x, -1, z), Blocks.STONE.getDefaultState(), 2);
                        world.setBlockState(pos, furniture.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 2);
                        furniture.onBlockPlacedBy(world, pos, world.getBlockState(pos), player,
                                UpholsteryColourHelper.createStack(furniture, 1, colour));
                    }
                    check(world.getBlockState(pos).getBlock() == furniture, "Structure ID lost");
                    check(UpholsteryColourHelper.getColour(world, pos) == colour, "Structure colour lost");
                    check(world.getBlockState(pos).getValue(BlockHorizontal.FACING) == facing, "Structure facing lost");
                    ItemStack pick = furniture.getPickBlock(world.getBlockState(pos), null, world, pos, player);
                    check(UpholsteryColourHelper.getColour(pick) == colour, "Structure pick lost colour");
                    List<ItemStack> drops = furniture.getDrops(world, pos, world.getBlockState(pos), 0);
                    check(drops.size() == 1 && UpholsteryColourHelper.getColour(drops.get(0)) == colour,
                            "Structure drop lost colour");
                    check(furniture.getFlammability(world, pos, EnumFacing.UP) == 20, "New wood is not flammable");
                    if (colour == UpholsteryColour.PINK) {
                        verifyHarvest(world, player, furniture, colour, pos.add(0, 0, 1800));
                    }
                }
            }
            verifyShield(world, player, suffix, woods);
            ++woods;
        }
        check(woods == 27, "Missing Phase 4 woods");
        LogManager.getLogger().info("IAF BYG PHASE FOUR PROBE PASSED: 27 woods, 6 forms, 16 colours, shields and crafting");
    }
    public static void verifyRecipes() {
        Map<String, String> groups = new HashMap<String, String>();
        int count = 0;
        for (IRecipe recipe : CraftingManager.REGISTRY) {
            ResourceLocation name = recipe.getRegistryName();
            if (!"iafbygaddon".equals(name.getNamespace())) continue;
            ItemStack output = recipe.getRecipeOutput();
            check(!output.isEmpty(), "Empty recipe preview " + name);
            String identity = output.getItem().getRegistryName() + "/" + output.getMetadata();
            String previous = groups.put(recipe.getGroup(), identity);
            check(!recipe.getGroup().isEmpty() && (previous == null || previous.equals(identity)), "Recipe-book group combines different furniture " + name);
            try (InputStreamReader reader = new InputStreamReader(PhaseFourProbe.class.getClassLoader().getResourceAsStream(
                    "assets/iafbygaddon/advancements/recipes/" + name.getPath() + ".json"), StandardCharsets.UTF_8)) {
                JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
                ItemPredicate predicate = ItemPredicate.deserialize(json.getAsJsonObject("criteria")
                        .getAsJsonObject("has_ingredient").getAsJsonObject("conditions").getAsJsonArray("items").get(0));
                check(!predicate.test(new ItemStack(Blocks.CRAFTING_TABLE)), "Crafting table unlocks " + name);
            } catch (java.io.IOException error) { throw new IllegalStateException(name.toString(), error); }
            ++count;
        }
        check(count == 2943, "Expected 2943 loaded recipes, found " + count);
    }
    private static void verifyShield(WorldServer world, EntityPlayer player, String suffix, int index) {
        Block chair = block("chair_wood_ironage_shield_" + suffix);
        ItemStack shield = new ItemStack(Items.SHIELD, 1, 71);
        shield.setStackDisplayName("BYG shield test");
        shield.addEnchantment(Enchantments.UNBREAKING, 2);
        NBTTagCompound pattern = new NBTTagCompound();
        pattern.setInteger("Base", 6);
        shield.getTagCompound().setTag("BlockEntityTag", pattern);
        IRecipe recipe = recipe(chair.getRegistryName().getPath());
        ItemStack result = recipe.getCraftingResult(grid(shield, new ItemStack(block("chair_wood_ironage_classic_" + suffix))));
        check(ItemStack.areItemStacksEqual(shield, ShieldChairItemData.getShield(result)), "Crafted shield data lost");
        BlockPos pos = new BlockPos(320 + index * 5, 70, 660);
        if (world.isAirBlock(pos)) {
            world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(pos, chair.getDefaultState(), 2);
            chair.onBlockPlacedBy(world, pos, world.getBlockState(pos), player, result);
        }
        TileEntityShieldChair tile = (TileEntityShieldChair) world.getTileEntity(pos);
        check(ItemStack.areItemStacksEqual(shield, tile.getShield()), "Saved shield changed");
        check(ItemStack.areItemStacksEqual(shield, ShieldChairItemData.getShield(
                chair.getPickBlock(world.getBlockState(pos), null, world, pos, player))), "Picked shield data lost");
        List<ItemStack> drops = chair.getDrops(world, pos, world.getBlockState(pos), 0);
        check(drops.size() == 2 && ItemStack.areItemStacksEqual(shield, drops.get(1)), "Dropped shield data lost");
    }
    private static void craft(IRecipe recipe, InventoryCrafting grid, Block block, UpholsteryColour colour) {
        check(recipe.matches(grid, null), "Recipe failed " + recipe.getRegistryName() + " " + colour);
        ItemStack result = CraftingManager.findMatchingResult(grid, null);
        check(result.getItem() == Item.getItemFromBlock(block) && result.getCount() == 1
                && UpholsteryColourHelper.getColour(result) == colour && result.hasTagCompound()
                && colour.getSerializedName().equals(result.getTagCompound().getString("Color")), "Crafting lost colour " + recipe.getRegistryName());
    }
    private static void verifyHarvest(WorldServer world, EntityPlayer player, Block block,
            UpholsteryColour colour, BlockPos pos) {
        for (int x = -2; x <= 2; ++x) for (int z = -2; z <= 2; ++z)
            world.setBlockState(pos.add(x, -1, z), Blocks.STONE.getDefaultState(), 2);
        world.setBlockState(pos, block.getDefaultState(), 2);
        block.onBlockPlacedBy(world, pos, world.getBlockState(pos), player,
                UpholsteryColourHelper.createStack(block, 1, colour));
        IBlockState state = world.getBlockState(pos);
        net.minecraft.tileentity.TileEntity tile = world.getTileEntity(pos);
        check(block.removedByPlayer(state, world, pos, player, true), "Mining rejected " + block.getRegistryName());
        block.harvestBlock(world, player, pos, state, tile, new ItemStack(Items.DIAMOND_AXE));
        List<EntityItem> drops = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(3));
        int count = 0;
        for (EntityItem entity : drops) {
            ItemStack stack = entity.getItem();
            if (stack.getItem() == Item.getItemFromBlock(block)) {
                check(UpholsteryColourHelper.getColour(stack) == colour, "Mining reset colour " + block.getRegistryName());
                count += stack.getCount();
            }
            entity.setDead();
        }
        check(count == 1 && world.isAirBlock(pos), "Mining did not remove one whole structure " + block.getRegistryName());
    }
    private static Block block(String path) { return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("iafbygaddon", path)); }
    private static IRecipe recipe(String path) {
        IRecipe recipe = CraftingManager.REGISTRY.getObject(new ResourceLocation("iafbygaddon", path));
        check(recipe != null, "Missing recipe " + path);
        return recipe;
    }
    private static InventoryCrafting grid(ItemStack first, ItemStack second) {
        InventoryCrafting grid = new InventoryCrafting(new Container() {
            @Override public boolean canInteractWith(EntityPlayer player) { return false; }
        }, 3, 3);
        grid.setInventorySlotContents(0, first.copy());
        grid.setInventorySlotContents(8, second.copy());
        return grid;
    }
    private static void check(boolean condition, String message) { RuntimeProbe.check(condition, message); }
}
