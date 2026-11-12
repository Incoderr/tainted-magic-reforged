package taintedmagic.wands;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import taintedmagic.registry.ModItems;

public final class WandsFeature {
    public static final DeferredItem<Item> WARPWOOD_ROD = ModItems.ITEMS.registerSimpleItem("warpwood_rod");
    public static final DeferredItem<Item> WARPWOOD_STAFF_CORE = ModItems.ITEMS.registerSimpleItem("warpwood_staff_core");
    public static final DeferredItem<Item> SHADOW_METAL_WAND_CAP = ModItems.ITEMS.registerSimpleItem("shadow_metal_wand_cap");
    public static final DeferredItem<Item> CLOTH_WAND_CAP = ModItems.ITEMS.registerSimpleItem("cloth_wand_cap");
    public static final DeferredItem<Item> CRIMSON_CLOTH_WAND_CAP = ModItems.ITEMS.registerSimpleItem("crimson_cloth_wand_cap");
    public static final DeferredItem<Item> SHADOW_CLOTH_WAND_CAP = ModItems.ITEMS.registerSimpleItem("shadow_cloth_wand_cap");

    private WandsFeature() {
    }

    public static void load() {
    }
}
