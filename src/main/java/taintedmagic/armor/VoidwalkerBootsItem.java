package taintedmagic.armor;

import net.minecraft.world.level.Level;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import javax.annotation.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.item.armor.ArmorSupport;
import thaumcraft.item.gear.Repairable;
import thaumcraft.item.gear.RunicArmor;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.wand.VisDiscountGear;

public class VoidwalkerBootsItem extends net.minecraft.world.item.ArmorItem implements VisDiscountGear, WarpingGear, RunicArmor, Repairable {
    public VoidwalkerBootsItem(thaumcraft.platform.TcArmorMaterial material, thaumcraft.platform.ArmorSlot slot, Properties properties) {
        super(taintedmagic.platform.TmArmor.material(material), taintedmagic.platform.TmArmor.type(slot), properties);
    }

    @Override
    public int getRunicCharge(ItemStack stack) {
        return 0;
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
