package taintedmagic.armor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.GogglesItem;
import thaumcraft.item.armor.ArmorSupport;
import thaumcraft.item.gear.WarpingGear;

public class VoidMetalGogglesItem extends GogglesItem implements WarpingGear {
    public VoidMetalGogglesItem(Properties properties) {
        super(properties);
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
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        ArmorSupport.selfRepair(stack, owner);
    }
}
