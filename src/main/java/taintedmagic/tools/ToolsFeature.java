package taintedmagic.tools;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import taintedmagic.registry.ModItems;

public final class ToolsFeature {
    public static final DeferredItem<Item> THAUMIC_DISASSEMBLER = single("thaumic_disassembler");
    public static final DeferredItem<Item> HOLLOW_DAGGER = single("hollow_dagger");
    public static final DeferredItem<Item> SHADOW_METAL_HOE = single("shadow_metal_hoe");
    public static final DeferredItem<Item> SHADOW_METAL_PICKAXE = single("shadow_metal_pickaxe");
    public static final DeferredItem<Item> SHADOW_METAL_AXE = single("shadow_metal_axe");
    public static final DeferredItem<Item> SHADOW_METAL_SHOVEL = single("shadow_metal_shovel");
    public static final DeferredItem<Item> SHADOW_METAL_SWORD = single("shadow_metal_sword");
    public static final DeferredItem<Item> PRIMAL_BLADE = single("primal_blade");
    public static final DeferredItem<Item> THAUMIUM_KATANA = single("thaumium_katana");
    public static final DeferredItem<Item> VOID_METAL_KATANA = single("void_metal_katana");
    public static final DeferredItem<Item> SHADOW_METAL_KATANA = single("shadow_metal_katana");

    private ToolsFeature() {
    }

    private static DeferredItem<Item> single(String name) {
        return ModItems.ITEMS.registerItem(name, Item::new, properties -> properties.stacksTo(1));
    }

    public static void load() {
    }
}
