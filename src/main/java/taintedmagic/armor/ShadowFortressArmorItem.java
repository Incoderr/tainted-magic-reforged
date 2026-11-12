package taintedmagic.armor;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.armor.ArmorSupport;
import thaumcraft.item.armor.FortressArmorItem;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.wand.VisDiscountGear;

public class ShadowFortressArmorItem extends FortressArmorItem implements WarpingGear, VisDiscountGear {
    public ShadowFortressArmorItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 5;
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
}
