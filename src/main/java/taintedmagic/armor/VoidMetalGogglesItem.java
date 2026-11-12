package taintedmagic.armor;

import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.GogglesItem;
import thaumcraft.item.armor.ArmorSupport;
import thaumcraft.item.gear.WarpingGear;

public class VoidMetalGogglesItem extends GogglesItem implements WarpingGear {
    public VoidMetalGogglesItem(Properties properties) {
        super(properties);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity, net.minecraft.world.entity.EquipmentSlot slot, net.minecraft.world.item.ArmorMaterial.Layer layer, boolean innerModel) {
        return taintedmagic.platform.TmArmor.texture(ArmorFeature.VOID_METAL_GOGGLES_MATERIAL);
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 5;
    }

    @Override
    public int getVisDiscount(ItemStack stack, Player player, Aspect aspect) {
        return 12;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity owner, int slotIndex, boolean selected) {
        if (!(world instanceof ServerLevel level)) {
            return;
        }
        super.inventoryTick(stack, world, owner, slotIndex, selected);
        ArmorSupport.selfRepair(stack, owner);
    }
}
