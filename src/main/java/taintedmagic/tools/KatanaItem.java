package taintedmagic.tools;

import net.minecraft.world.InteractionResultHolder;
import java.util.List;
import java.util.Map;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import thaumcraft.entity.CombatUtils;
import thaumcraft.entity.projectile.ExplosiveOrb;
import thaumcraft.fx.Fx;
import thaumcraft.infusion.InfusionKeys;
import thaumcraft.item.gear.Repairable;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.registry.ModSounds;

public class KatanaItem extends Item implements Repairable, WarpingGear {
    public static final int CHARGE_TICKS = 20;
    public static final int COOLDOWN_TICKS = 140;

    public enum Tier {
        THAUMIUM(14.25F, 0),
        VOID_METAL(17.5F, 3),
        SHADOW_METAL(20.75F, 7);

        private final float damage;
        private final int warp;

        Tier(float damage, int warp) {
            this.damage = damage;
            this.warp = warp;
        }

        public float damage() {
            return damage;
        }
    }

    private final Tier tier;

    public KatanaItem(Tier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public Tier tier() {
        return tier;
    }

    public static int inscription(ItemStack stack) {
        Map<String, String> tags = InfusionKeys.TAGS.get(stack);
        if (tags == null || !tags.containsKey("inscription")) {
            return -1;
        }
        try {
            return Integer.parseInt(tags.get("inscription"));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static long cooldown(ItemStack stack, Level level) {
        long remaining = ToolsFeature.KATANA_COOLDOWN.getOrDefault(stack, 0L) - level.getGameTime();
        return Math.max(0L, Math.min(COOLDOWN_TICKS, remaining));
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return tier.warp;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (cooldown(player.getItemInHand(hand), level) > 0) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int ticksRemaining) {
        if (user.tickCount % 5 == 0) {
            float pitch = 0.75F + (float) Math.random() * 0.25F;
            level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.WIND.get(), SoundSource.PLAYERS, pitch * 0.1F, pitch);
        }
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity mob, LivingEntity attacker) {
        if (tier != Tier.THAUMIUM) {
            mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60), attacker);
        }
        if (tier == Tier.SHADOW_METAL) {
            mob.addEffect(new MobEffectInstance(MobEffects.HUNGER, 120), attacker);
        }
        switch (inscription(stack)) {
            case 0 -> mob.igniteForSeconds(3.0F);
            case 1 -> mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 140), attacker);
            case 2 -> mob.addEffect(new MobEffectInstance(MobEffects.WITHER, 60), attacker);
            default -> {
            }
        }
        swingSound(attacker);
    }

    private static void swingSound(LivingEntity entity) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.SWING.get(), SoundSource.PLAYERS, 0.5F + (float) Math.random(), 0.5F + (float) Math.random());
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player) || getUseDuration(stack, entity) - timeLeft < CHARGE_TICKS) {
            return;
        }
        int inscription = inscription(stack);
        if (inscription < 0 || player.isShiftKeyDown()) {
            if (level instanceof ServerLevel server) {
                chargedStrike(server, player);
            }
            swingSound(player);
            return;
        }
        if (level instanceof ServerLevel server) {
            switch (inscription) {
                case 0 -> fireball(server, player);
                case 1 -> thunder(server, player);
                case 2 -> heal(server, player);
                default -> {
                }
            }
            ToolsFeature.KATANA_COOLDOWN.set(stack, level.getGameTime() + COOLDOWN_TICKS);
        } else if (inscription == 1) {
            for (LivingEntity target : targets(level, player, 10.0)) {
                Fx.get().shockZap(level, player, target);
            }
        } else if (inscription == 2) {
            for (int index = 0; index < 18; index++) {
                Fx.get().sparkle(level, (float) (player.getX() + level.getRandom().nextGaussian() * 0.25), (float) (player.getY() + 1.0 + level.getRandom().nextGaussian() * 0.5), (float) (player.getZ() + level.getRandom().nextGaussian() * 0.25), 1.5F, 5, 0.02F);
            }
        }
        return;
    }

    private void chargedStrike(ServerLevel level, Player player) {
        double reach = player.isCreative() ? 6.0 : 3.0;
        Entity target = CombatUtils.getPointedEntity(level, player, 0.0, reach, 0.0F);
        if (!(target instanceof LivingEntity)) {
            return;
        }
        float multiplier = level.getRandom().nextInt(10) == 0 ? 2.5F : 1.5F;
        target.hurt(level.damageSources().playerAttack(player), tier.damage * multiplier);
    }

    public static java.util.List<LivingEntity> targets(Level level, Player player, double range) {
        AABB area = new AABB(player.getX(), player.getY(), player.getZ(), player.getX() + 1, player.getY() + 1, player.getZ() + 1).inflate(range);
        return level.getEntitiesOfClass(LivingEntity.class, area, entity -> entity != player && entity.isAlive() && !entity.isInvulnerable());
    }

    private void fireball(ServerLevel level, Player player) {
        ExplosiveOrb orb = ExplosiveOrb.create(level, player);
        orb.strength = tier.damage * 0.25F;
        Vec3 motion = orb.getDeltaMovement();
        orb.setPos(orb.position().add(motion));
        level.addFreshEntity(orb);
    }

    private void thunder(ServerLevel level, Player player) {
        for (LivingEntity entity : targets(level, player, 10.0)) {
            entity.hurt(level.damageSources().indirectMagic(player, player), tier.damage * 0.25F);
            Vec3 away = entity.position().subtract(player.position()).normalize();
            entity.push(away.x * 5.0, 1.5, away.z * 5.0);
            entity.hurtMarked = true;
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), taintedmagic.registry.ModSounds.SHOCKWAVE.get(), SoundSource.PLAYERS, 5.0F, 1.5F * (float) Math.random());
    }

    public static void heal(ServerLevel level, Player player) {
        player.heal((player.getMaxHealth() - player.getHealth()) * 0.5F);
        for (LivingEntity entity : targets(level, player, 5.0)) {
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1), player);
        }
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 1));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.WAND.get(), SoundSource.PLAYERS, 1.0F, 0.9F + (float) Math.random() * 0.1F);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> builder, TooltipFlag flag) {
        if (tier == Tier.VOID_METAL) {
            builder.add(Component.translatable("enchantment.special.sapless").withStyle(ChatFormatting.GOLD));
        } else if (tier == Tier.SHADOW_METAL) {
            builder.add(Component.translatable("enchantment.special.sapgreat").withStyle(ChatFormatting.GOLD));
        }
        int inscription = inscription(stack);
        if (inscription >= 0) {
            builder.add(Component.translatable("taintedmagic.text.katana.inscription." + inscription).withStyle(ChatFormatting.GOLD));
        }
    }
}
