package taintedmagic.platform;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import thaumcraft.platform.ArmorSlot;
import thaumcraft.platform.Registration;
import thaumcraft.platform.TcArmorMaterial;

public final class TmArmor {
    private TmArmor() {
    }

    public interface Factory<T extends Item> {
        T create(TcArmorMaterial material, ArmorSlot slot, Item.Properties properties);
    }

    public static Holder<ArmorMaterial> material(TcArmorMaterial material) {
        return Registration.armorMaterial(material);
    }

    public static ArmorItem.Type type(ArmorSlot slot) {
        return Registration.armorType(slot);
    }

    public static Item.Properties fixedAttributes(Item.Properties properties, TcArmorMaterial material, ArmorSlot slot) {
        ArmorItem.Type type = Registration.armorType(slot);
        ResourceLocation id = ResourceLocation.withDefaultNamespace("armor." + type.getName());
        return properties.attributes(ItemAttributeModifiers.builder()
            .add(Attributes.ARMOR, new AttributeModifier(id, material.defense(slot), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
            .build());
    }

    public static ResourceLocation texture(TcArmorMaterial material) {
        ResourceLocation asset = material.assetId();
        return ResourceLocation.fromNamespaceAndPath(asset.getNamespace(), "textures/models/armor/" + asset.getPath() + "_layer_1.png");
    }
}