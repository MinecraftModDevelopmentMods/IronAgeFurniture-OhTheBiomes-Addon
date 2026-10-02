package zone.moddev.mc.iafbygaddon.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockUpholsteredFurniture;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

/** Keeps colour names in the add-on's locale files while sharing IAF's item format. */
public final class ItemBlockColouredFurniture extends ItemBlockUpholsteredFurniture {
    public ItemBlockColouredFurniture(Block block) { super(block); }

    @Override public String getItemStackDisplayName(ItemStack stack) {
        String colour = UpholsteryColourHelper.getColour(stack).getSerializedName();
        return I18n.translateToLocal(getTranslationKey() + ("red".equals(colour) ? "" : "." + colour) + ".name");
    }
}
