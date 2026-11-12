package taintedmagic.foci;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import taintedmagic.registry.ModItems;

public final class FociFeature {
    public static final DeferredItem<Item> FOCUS_TAINT_SWARM = single("focus_taint_swarm");
    public static final DeferredItem<Item> FOCUS_DARK_MATTER = single("focus_dark_matter");
    public static final DeferredItem<Item> FOCUS_MAGE_MACE = single("focus_mage_mace");
    public static final DeferredItem<Item> FOCUS_SHOCKWAVE = single("focus_shockwave");
    public static final DeferredItem<Item> FOCUS_VIS_SHARD = single("focus_vis_shard");
    public static final DeferredItem<Item> FOCUS_LUMOS = single("focus_lumos");

    private FociFeature() {
    }

    private static DeferredItem<Item> single(String name) {
        return ModItems.ITEMS.registerItem(name, Item::new, properties -> properties.stacksTo(1));
    }

    public static void load() {
    }
}
