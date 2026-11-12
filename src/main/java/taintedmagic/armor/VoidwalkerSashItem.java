package taintedmagic.armor;

import net.minecraft.world.InteractionResultHolder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import thaumcraft.item.gear.RunicArmor;
import thaumcraft.item.gear.RunicShield;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.BaubleType;
import thaumcraft.platform.Baubles;

public class VoidwalkerSashItem extends Item implements Bauble, RunicArmor, WarpingGear {
    public VoidwalkerSashItem(Properties properties) {
        super(properties);
    }

    public static boolean isSpeedEnabled(ItemStack stack) {
        return ArmorFeature.SASH_SPEED.getOrDefault(stack, true);
    }

    public static boolean wornWithSpeed(Player player) {
        ItemStack belt = Baubles.get(player, 3);
        return belt.getItem() instanceof VoidwalkerSashItem && isSpeedEnabled(belt);
    }

    public static Component modeMessage(ItemStack stack) {
        return isSpeedEnabled(stack)
            ? Component.translatable("taintedmagic.text.sash.speed.on").withStyle(ChatFormatting.GREEN)
            : Component.translatable("taintedmagic.text.sash.speed.off").withStyle(ChatFormatting.RED);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.BELT;
    }

    @Override
    public int getRunicCharge(ItemStack stack) {
        return 20;
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 2;
    }

    @Override
    public void onEquipped(ItemStack stack, LivingEntity wearer) {
        if (wearer instanceof Player player) {
            RunicShield.markDirty(player);
        }
    }

    @Override
    public void onUnequipped(ItemStack stack, LivingEntity wearer) {
        if (wearer instanceof Player player) {
            RunicShield.markDirty(player);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && player.isShiftKeyDown()) {
            ArmorFeature.SASH_SPEED.set(stack, !isSpeedEnabled(stack));
            player.displayClientMessage(modeMessage(stack), true);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> builder, TooltipFlag flag) {
        builder.add(modeMessage(stack));
        builder.add(Component.literal(" "));
    }
}
