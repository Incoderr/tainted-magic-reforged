package taintedmagic.armor;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.armor.ArmorSupport;
import thaumcraft.item.armor.FortressArmorItem;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.wand.VisDiscountGear;

public class ShadowFortressArmorItem extends FortressArmorItem implements WarpingGear, VisDiscountGear {
    public ShadowFortressArmorItem(thaumcraft.platform.TcArmorMaterial material, thaumcraft.platform.ArmorSlot slot, Properties properties) {
        super(material, slot, properties);
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> builder, TooltipFlag flag) {
        ArmorSupport.visDiscountLine(builder, getVisDiscount(stack, null, null));
        super.appendHoverText(stack, context, builder, flag);
    }
}
