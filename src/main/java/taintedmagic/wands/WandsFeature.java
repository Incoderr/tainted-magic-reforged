package taintedmagic.wands;

import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import thaumcraft.wand.WandCap;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.WandRod;
import thaumcraft.wand.Wands;
import taintedmagic.TaintedMagic;
import taintedmagic.registry.ModItems;

public final class WandsFeature {
    public static final DeferredItem<Item> WARPWOOD_ROD = part("warpwood_rod");
    public static final DeferredItem<Item> WARPWOOD_STAFF_CORE = part("warpwood_staff_core");
    public static final DeferredItem<Item> SHADOW_METAL_WAND_CAP = part("shadow_metal_wand_cap");
    public static final DeferredItem<Item> CLOTH_WAND_CAP = part("cloth_wand_cap");
    public static final DeferredItem<Item> CRIMSON_CLOTH_WAND_CAP = part("crimson_cloth_wand_cap");
    public static final DeferredItem<Item> SHADOW_CLOTH_WAND_CAP = part("shadow_cloth_wand_cap");

    public static final WandRod ROD_WARPWOOD = WandRegistry.registerRod(
        "warpwood", 250, TaintedMagic.id("warpwood_rod"), 16, WarpwoodRodUpdate.INSTANCE, true, false, false, texture("model_wand_rod_warpwood")
    );
    public static final WandRod STAFF_WARPWOOD = WandRegistry.registerRod(
        "warpwood", 500, TaintedMagic.id("warpwood_staff_core"), 20, WarpwoodRodUpdate.INSTANCE, true, true, true, texture("model_wand_rod_warpwood")
    );

    public static final WandCap CAP_SHADOW_METAL = cap("shadowmetal", 0.65F, "shadow_metal_wand_cap", 12, "model_wand_cap_shadowmetal");
    public static final WandCap CAP_CLOTH = cap("cloth", 0.97F, "cloth_wand_cap", 2, "model_wand_cap_cloth");
    public static final WandCap CAP_CRIMSON_CLOTH = cap("crimsoncloth", 0.93F, "crimson_cloth_wand_cap", 3, "model_wand_cap_crimson_cloth");
    public static final WandCap CAP_SHADOW_CLOTH = cap("shadowcloth", 0.93F, "shadow_cloth_wand_cap", 4, "model_wand_cap_shadow_cloth");

    private WandsFeature() {
    }

    private static DeferredItem<Item> part(String name) {
        return ModItems.ITEMS.registerItem(name, Item::new, properties -> properties.rarity(Rarity.UNCOMMON));
    }

    private static WandCap cap(String tag, float discount, String item, int craftCost, String texture) {
        return WandRegistry.registerCap(tag, discount, List.of(), 0.0F, TaintedMagic.id(item), craftCost, null, texture(texture));
    }

    private static net.minecraft.resources.Identifier texture(String name) {
        return TaintedMagic.id("textures/models/" + name + ".png");
    }

    public static ItemStack charged(WandRod rod, boolean sceptre) {
        ItemStack stack = Wands.create(CAP_SHADOW_METAL, rod, sceptre);
        Wands.fill(stack);
        return stack;
    }

    public static List<ItemStack> creativeWands() {
        return List.of(charged(STAFF_WARPWOOD, false), charged(ROD_WARPWOOD, false), charged(ROD_WARPWOOD, true));
    }

    public static ItemStack tabIcon() {
        return Wands.create(CAP_SHADOW_METAL, ROD_WARPWOOD, false);
    }

    public static void load() {
    }
}
