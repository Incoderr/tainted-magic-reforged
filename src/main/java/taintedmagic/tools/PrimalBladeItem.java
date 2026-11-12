package taintedmagic.tools;

import net.minecraft.world.InteractionResultHolder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import thaumcraft.item.gear.Repairable;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.registry.ModSounds;

public class PrimalBladeItem extends Item implements Repairable, WarpingGear {
    public PrimalBladeItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 5;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity mob, LivingEntity attacker) {
        mob.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1), attacker);
        mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 1), attacker);
        attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), ModSounds.SWING.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity owner, int slotIndex, boolean selected) {
        if (!(world instanceof ServerLevel level)) {
            return;
        }
        if (stack.isDamaged() && owner.tickCount % 20 == 0) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int ticksRemaining) {
        if (user.tickCount % 10 == 0) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.BRAIN.get(), SoundSource.PLAYERS, 0.05F, 0.5F);
        }
        pull(level, user);
    }

    public static void pull(Level level, LivingEntity user) {
        AABB area = new AABB(user.getX(), user.getY(), user.getZ(), user.getX() + 1, user.getY() + 1, user.getZ() + 1).inflate(15.0);
        for (Entity entity : level.getEntities(user, area)) {
            if (level instanceof ServerLevel server && entity.isAlive() && !entity.isInvulnerable() && user.distanceTo(entity) < 2.0F) {
                entity.hurt(server.damageSources().indirectMagic(user, user), 3.0F);
            }
            double x = (user.getX() + 0.5 - entity.getX()) / 20.0;
            double y = (user.getY() + 0.5 - entity.getY()) / 20.0;
            double z = (user.getZ() + 0.5 - entity.getZ()) / 20.0;
            double length = Math.sqrt(x * x + y * y + z * z);
            double strength = 1.0 - length;
            if (strength > 0.0 && length > 0.0) {
                strength *= strength;
                entity.setDeltaMovement(entity.getDeltaMovement().add(new Vec3(x / length * strength * 0.2, y / length * strength * 0.3, z / length * strength * 0.2)));
                entity.hurtMarked = true;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> builder, TooltipFlag flag) {
        builder.add(Component.translatable("taintedmagic.text.sapprimal").withStyle(ChatFormatting.GOLD));
    }
}
