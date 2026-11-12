package taintedmagic.armor;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.armor.ArmorSupport;
import thaumcraft.item.armor.FortressArmorItem;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.wand.VisDiscountGear;

public class VoidFortressArmorItem extends FortressArmorItem implements WarpingGear, VisDiscountGear {
    public VoidFortressArmorItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 3;
    }

    @Override
    public int getVisDiscount(ItemStack stack, Player player, Aspect aspect) {
        return 5;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        ArmorSupport.visDiscountLine(builder, getVisDiscount(stack, null, null));
        super.appendHoverText(stack, context, display, builder, flag);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        ArmorSupport.selfRepair(stack, owner);
    }
}
