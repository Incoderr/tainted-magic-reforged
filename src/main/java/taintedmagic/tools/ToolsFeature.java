package taintedmagic.tools;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredItem;
import thaumcraft.item.gear.TcMaterials;
import thaumcraft.platform.Registration;
import thaumcraft.platform.TcToolMaterial;
import taintedmagic.armor.ArmorFeature;
import taintedmagic.platform.TmStackKey;
import taintedmagic.registry.ModItems;
import taintedmagic.registry.TmRarity;

public final class ToolsFeature {
    public static final TcToolMaterial HOLLOW = new TcToolMaterial(200, 4.0F, -0.5F, 5, TcMaterials.REPAIRS_NONE, BlockTags.INCORRECT_FOR_STONE_TOOL);
    public static final TcToolMaterial SHADOW = new TcToolMaterial(2500, 17.0F, 6.0F, 30, ArmorFeature.REPAIRS_SHADOW_METAL, BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
    public static final TcToolMaterial PRIMAL = new TcToolMaterial(500, 16.0F, 16.75F, 30, TcMaterials.REPAIRS_NONE, BlockTags.INCORRECT_FOR_DIAMOND_TOOL);

    public static final TmStackKey<Long> KATANA_COOLDOWN = TmStackKey.register("katana_cooldown", Codec.LONG, ByteBufCodecs.VAR_LONG);
    public static final TmStackKey<Integer> DISASSEMBLER_MODE = TmStackKey.register("disassembler_mode", Codec.INT, ByteBufCodecs.VAR_INT);
    public static final TmStackKey<Integer> ENTROPY_CHARGE = TmStackKey.register("entropy_charge", Codec.INT, ByteBufCodecs.VAR_INT);

    public static final DeferredItem<ThaumicDisassemblerItem> THAUMIC_DISASSEMBLER = ModItems.ITEMS.registerItem(
        "thaumic_disassembler", ThaumicDisassemblerItem::new, properties -> properties.stacksTo(1).rarity(Rarity.UNCOMMON)
    );
    public static final DeferredItem<HollowDaggerItem> HOLLOW_DAGGER = ModItems.ITEMS.registerItem(
        "hollow_dagger", HollowDaggerItem::new, properties -> Registration.sword(properties.rarity(Rarity.UNCOMMON), HOLLOW, 4.0F, -2.4F)
    );
    public static final DeferredItem<ShadowMetalHoeItem> SHADOW_METAL_HOE = ModItems.ITEMS.registerItem(
        "shadow_metal_hoe", ShadowMetalHoeItem::new, properties -> Registration.hoe(properties.rarity(Rarity.UNCOMMON), SHADOW, -6.0F, 0.0F)
    );
    public static final DeferredItem<ShadowMetalToolItem> SHADOW_METAL_PICKAXE = shadow("shadow_metal_pickaxe", properties -> Registration.pickaxe(properties, SHADOW, 2.0F, -2.8F));
    public static final DeferredItem<ShadowMetalToolItem> SHADOW_METAL_AXE = shadow("shadow_metal_axe", properties -> Registration.axe(properties, SHADOW, 3.0F, -3.0F));
    public static final DeferredItem<ShadowMetalToolItem> SHADOW_METAL_SHOVEL = shadow("shadow_metal_shovel", properties -> Registration.shovel(properties, SHADOW, 1.0F, -3.0F));
    public static final DeferredItem<ShadowMetalToolItem> SHADOW_METAL_SWORD = shadow("shadow_metal_sword", properties -> Registration.sword(properties, SHADOW, 4.0F, -2.4F));
    public static final DeferredItem<PrimalBladeItem> PRIMAL_BLADE = ModItems.ITEMS.registerItem(
        "primal_blade", PrimalBladeItem::new, properties -> Registration.sword(properties.rarity(TmRarity.creation()), PRIMAL, 4.0F, -2.4F)
    );
    public static final DeferredItem<KatanaItem> THAUMIUM_KATANA = katana("thaumium_katana", KatanaItem.Tier.THAUMIUM);
    public static final DeferredItem<KatanaItem> VOID_METAL_KATANA = katana("void_metal_katana", KatanaItem.Tier.VOID_METAL);
    public static final DeferredItem<KatanaItem> SHADOW_METAL_KATANA = katana("shadow_metal_katana", KatanaItem.Tier.SHADOW_METAL);

    private ToolsFeature() {
    }

    private static DeferredItem<ShadowMetalToolItem> shadow(String name, Function<Item.Properties, Item.Properties> tool) {
        return ModItems.ITEMS.registerItem(name, ShadowMetalToolItem::new, properties -> tool.apply(properties.rarity(Rarity.UNCOMMON)));
    }

    private static DeferredItem<KatanaItem> katana(String name, KatanaItem.Tier tier) {
        return ModItems.ITEMS.registerItem(name, properties -> new KatanaItem(tier, properties), properties -> properties.stacksTo(1).rarity(Rarity.UNCOMMON)
            .attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, tier.damage() - 1.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build()));
    }

    public static void load() {
    }
}
