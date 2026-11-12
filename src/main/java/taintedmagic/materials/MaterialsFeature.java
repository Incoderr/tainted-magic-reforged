package taintedmagic.materials;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import taintedmagic.registry.ModItems;
import taintedmagic.registry.TmRarity;

public final class MaterialsFeature {
    public static final DeferredItem<Item> SHADOW_METAL_INGOT = simple("shadow_metal_ingot", () -> Rarity.UNCOMMON);
    public static final DeferredItem<Item> SHADOW_CLOTH = simple("shadow_cloth", () -> Rarity.UNCOMMON);
    public static final DeferredItem<Item> CRIMSON_CLOTH = simple("crimson_cloth", () -> Rarity.UNCOMMON);
    public static final DeferredItem<Item> WARPED_SHARD = simple("warped_shard", () -> Rarity.COMMON);
    public static final DeferredItem<Item> TAINTED_SHARD = simple("tainted_shard", () -> Rarity.COMMON);
    public static final DeferredItem<CreationShardItem> CREATION_SHARD = ModItems.ITEMS.registerItem(
        "creation_shard", CreationShardItem::new, properties -> properties.rarity(TmRarity.creation())
    );
    public static final DeferredItem<Item> THAUMIC_ALLOY_PLATING = simple("thaumic_alloy_plating", () -> Rarity.UNCOMMON);
    public static final DeferredItem<Item> CRIMSON_PLATING = simple("crimson_plating", () -> Rarity.UNCOMMON);
    public static final DeferredItem<Item> SHADOW_METAL_NUGGET = simple("shadow_metal_nugget", () -> Rarity.UNCOMMON);
    public static final DeferredItem<Item> PRIMORDIAL_NODULE = simple("primordial_nodule", () -> Rarity.EPIC);
    public static final DeferredItem<Item> PRIMORDIAL_MOTE = simple("primordial_mote", () -> Rarity.EPIC);
    public static final DeferredItem<Item> CREATION_FRAGMENT = simple("creation_fragment", TmRarity::creation);

    public static final DeferredItem<Item> VOID_BLOOD = blood("void_blood", Rarity.RARE);
    public static final DeferredItem<Item> CRIMSON_BLOOD = blood("crimson_blood", Rarity.UNCOMMON);

    public static final DeferredItem<SalisItem> SALIS_TEMPESTAS = ModItems.ITEMS.registerItem(
        "salis_tempestas", properties -> new SalisItem(SalisItem.Kind.TEMPESTAS, properties), properties -> properties.rarity(TmRarity.creation())
    );
    public static final DeferredItem<SalisItem> SALIS_AEVUM = ModItems.ITEMS.registerItem(
        "salis_aevum", properties -> new SalisItem(SalisItem.Kind.AEVUM, properties), properties -> properties.rarity(TmRarity.creation())
    );

    private MaterialsFeature() {
    }

    private static DeferredItem<Item> simple(String name, Supplier<Rarity> rarity) {
        return ModItems.ITEMS.registerItem(name, Item::new, properties -> properties.rarity(rarity.get()));
    }

    private static DeferredItem<Item> blood(String name, Rarity rarity) {
        return ModItems.ITEMS.registerItem(
            name,
            Item::new,
            properties -> properties.rarity(rarity).craftRemainder(thaumcraft.registry.ModItems.GLASS_PHIAL.get())
        );
    }

    public static void load() {
    }
}
