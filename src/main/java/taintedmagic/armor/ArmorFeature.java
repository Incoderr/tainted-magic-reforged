package taintedmagic.armor;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import taintedmagic.registry.ModItems;

public final class ArmorFeature {
    public static final DeferredItem<Item> WARPED_GOGGLES = single("warped_goggles");
    public static final DeferredItem<Item> VOID_METAL_GOGGLES = single("void_metal_goggles");
    public static final DeferredItem<Item> VOID_FORTRESS_HELMET = single("void_fortress_helmet");
    public static final DeferredItem<Item> VOID_FORTRESS_CHESTPLATE = single("void_fortress_chestplate");
    public static final DeferredItem<Item> VOID_FORTRESS_LEGGINGS = single("void_fortress_leggings");
    public static final DeferredItem<Item> SHADOW_FORTRESS_HELMET = single("shadow_fortress_helmet");
    public static final DeferredItem<Item> SHADOW_FORTRESS_CHESTPLATE = single("shadow_fortress_chestplate");
    public static final DeferredItem<Item> SHADOW_FORTRESS_LEGGINGS = single("shadow_fortress_leggings");
    public static final DeferredItem<Item> VOIDWALKER_BOOTS = single("voidwalker_boots");
    public static final DeferredItem<Item> VOIDWALKER_SASH = single("voidwalker_sash");
    public static final DeferredItem<Item> LUMOS_RING = single("lumos_ring");
    public static final DeferredItem<Item> FLYTE_CHARM = single("flyte_charm");
    public static final DeferredItem<Item> GATE_KEY = single("gate_key");

    private ArmorFeature() {
    }

    private static DeferredItem<Item> single(String name) {
        return ModItems.ITEMS.registerItem(name, Item::new, properties -> properties.stacksTo(1));
    }

    public static void load() {
    }
}
