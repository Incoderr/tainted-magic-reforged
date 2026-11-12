package taintedmagic.armor;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import thaumcraft.item.gear.TcMaterials;
import thaumcraft.platform.ArmorSlot;
import thaumcraft.platform.Registration;
import thaumcraft.platform.TcArmorMaterial;
import taintedmagic.TaintedMagic;
import taintedmagic.platform.TmArmor;
import taintedmagic.platform.TmStackKey;
import taintedmagic.registry.ModItems;
import taintedmagic.registry.TmRarity;

public final class ArmorFeature {
    public static final TagKey<Item> REPAIRS_SHADOW_METAL = TagKey.create(Registries.ITEM, TaintedMagic.id("repairs_shadow_metal"));

    public static final TcArmorMaterial WARPED = material(500, 4, 0, 0, 0, 20, REPAIRS_SHADOW_METAL, "warped_goggles");
    public static final TcArmorMaterial VOID_METAL_GOGGLES_MATERIAL = material(25, 1, 3, 2, 1, 25, net.neoforged.neoforge.common.Tags.Items.INGOTS_GOLD, "void_metal_goggles");
    public static final TcArmorMaterial VOIDWALKER = material(300, 0, 0, 0, 4, 30, TcMaterials.REPAIRS_NONE, "voidwalker_boots");
    public static final TcArmorMaterial VOID_FORTRESS = material(300, 4, 8, 6, 0, 30, TcMaterials.REPAIRS_VOID_METAL, "void_fortress");
    public static final TcArmorMaterial SHADOW_FORTRESS = material(3000, 4, 10, 6, 0, 30, REPAIRS_SHADOW_METAL, "shadow_fortress");

    public static final TmStackKey<Boolean> VOIDTOUCHED = TmStackKey.register("voidtouched", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final TmStackKey<Boolean> SASH_SPEED = TmStackKey.register("sash_speed", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final TmStackKey<GateKeyItem.Binding> GATE_BINDING = TmStackKey.register("gate_binding", GateKeyItem.Binding.CODEC, GateKeyItem.Binding.STREAM_CODEC);

    public static final DeferredItem<WarpedGogglesItem> WARPED_GOGGLES = goggles("warped_goggles", WarpedGogglesItem::new, WARPED, ArmorSlot.HELMET, Rarity.RARE);
    public static final DeferredItem<VoidMetalGogglesItem> VOID_METAL_GOGGLES = goggles("void_metal_goggles", VoidMetalGogglesItem::new, VOID_METAL_GOGGLES_MATERIAL, ArmorSlot.HELMET, Rarity.RARE);
    public static final DeferredItem<VoidFortressArmorItem> VOID_FORTRESS_HELMET = armor("void_fortress_helmet", VoidFortressArmorItem::new, VOID_FORTRESS, ArmorSlot.HELMET, Rarity.RARE);
    public static final DeferredItem<VoidFortressArmorItem> VOID_FORTRESS_CHESTPLATE = armor("void_fortress_chestplate", VoidFortressArmorItem::new, VOID_FORTRESS, ArmorSlot.CHESTPLATE, Rarity.RARE);
    public static final DeferredItem<VoidFortressArmorItem> VOID_FORTRESS_LEGGINGS = armor("void_fortress_leggings", VoidFortressArmorItem::new, VOID_FORTRESS, ArmorSlot.LEGGINGS, Rarity.RARE);
    public static final DeferredItem<ShadowFortressArmorItem> SHADOW_FORTRESS_HELMET = armor("shadow_fortress_helmet", ShadowFortressArmorItem::new, SHADOW_FORTRESS, ArmorSlot.HELMET, Rarity.EPIC);
    public static final DeferredItem<ShadowFortressArmorItem> SHADOW_FORTRESS_CHESTPLATE = armor("shadow_fortress_chestplate", ShadowFortressArmorItem::new, SHADOW_FORTRESS, ArmorSlot.CHESTPLATE, Rarity.EPIC);
    public static final DeferredItem<ShadowFortressArmorItem> SHADOW_FORTRESS_LEGGINGS = armor("shadow_fortress_leggings", ShadowFortressArmorItem::new, SHADOW_FORTRESS, ArmorSlot.LEGGINGS, Rarity.EPIC);
    public static final DeferredItem<VoidwalkerBootsItem> VOIDWALKER_BOOTS = armor("voidwalker_boots", VoidwalkerBootsItem::new, VOIDWALKER, ArmorSlot.BOOTS, Rarity.EPIC);

    public static final DeferredItem<VoidwalkerSashItem> VOIDWALKER_SASH = ModItems.ITEMS.registerItem(
        "voidwalker_sash", VoidwalkerSashItem::new, properties -> properties.stacksTo(1).rarity(Rarity.EPIC)
    );
    public static final DeferredItem<LumosRingItem> LUMOS_RING = ModItems.ITEMS.registerItem(
        "lumos_ring", LumosRingItem::new, properties -> properties.stacksTo(1).rarity(Rarity.UNCOMMON)
    );
    public static final DeferredItem<FlyteCharmItem> FLYTE_CHARM = ModItems.ITEMS.registerItem(
        "flyte_charm", FlyteCharmItem::new, properties -> properties.stacksTo(1).rarity(TmRarity.creation())
    );
    public static final DeferredItem<GateKeyItem> GATE_KEY = ModItems.ITEMS.registerItem(
        "gate_key", GateKeyItem::new, properties -> properties.stacksTo(1).rarity(TmRarity.creation())
    );

    private ArmorFeature() {
    }

    private static TcArmorMaterial material(int durability, int helmet, int chest, int legs, int boots, int enchantability, TagKey<Item> repair, String asset) {
        return new TcArmorMaterial(durability, helmet, chest, legs, boots, enchantability, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, repair, TaintedMagic.id(asset));
    }

    private static <T extends Item> DeferredItem<T> armor(String name, TmArmor.Factory<T> factory, TcArmorMaterial material, ArmorSlot slot, Rarity rarity) {
        return ModItems.ITEMS.registerItem(name, properties -> factory.create(material, slot, properties), properties -> Registration.armor(properties.rarity(rarity), material, slot));
    }

    private static <T extends Item> DeferredItem<T> goggles(String name, Function<Item.Properties, T> factory, TcArmorMaterial material, ArmorSlot slot, Rarity rarity) {
        return ModItems.ITEMS.registerItem(name, factory, properties -> TmArmor.fixedAttributes(Registration.armor(properties.rarity(rarity), material, slot), material, slot));
    }

    public static void load() {
        for (TcArmorMaterial material : new TcArmorMaterial[] {WARPED, VOID_METAL_GOGGLES_MATERIAL, VOIDWALKER, VOID_FORTRESS, SHADOW_FORTRESS}) {
            TmArmor.material(material);
        }
        taintedmagic.foci.FociEvents.addLumosSource(LumosRingItem::worn);
    }
}
