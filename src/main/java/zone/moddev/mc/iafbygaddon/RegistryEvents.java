package zone.moddev.mc.iafbygaddon;

import zone.moddev.mc.iafbygaddon.init.AddonRegistries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Registers all generated furniture under the add-on namespace. */
@Mod.EventBusSubscriber(modid = IAFOhTheBiomesAddon.MODID)
public final class RegistryEvents {
    private RegistryEvents() {
        throw new IllegalAccessError("This class cannot be instantiated");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                AddonRegistries.BLOCKS.values().toArray(
                        new Block[AddonRegistries.BLOCKS.size()]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                AddonRegistries.ITEMS.values().toArray(
                        new Item[AddonRegistries.ITEMS.size()]));
    }
}
