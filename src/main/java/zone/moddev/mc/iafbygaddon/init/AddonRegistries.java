package zone.moddev.mc.iafbygaddon.init;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/** Deterministic add-on-owned block and item registration collections. */
public final class AddonRegistries {
    public static final Map<String, Block> BLOCKS = new LinkedHashMap<String, Block>();
    public static final Map<String, Item> ITEMS = new LinkedHashMap<String, Item>();

    private AddonRegistries() {
        throw new IllegalAccessError("This class cannot be instantiated");
    }
}
