package taintedmagic.foci;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.fx.Fx;
import thaumcraft.wand.Wands;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.TaintedMagic;
import taintedmagic.registry.ModSounds;

public class ShockwaveFocus extends TmFocus {
    private static final AspectList COST = new AspectList().add(Aspect.AIR, 150).add(Aspect.ENTROPY, 100);

    public ShockwaveFocus(Properties properties) {
        super(properties);
    }

    @Override
    public String getSortingHelper(ItemStack focus) {
        return "SHOCKWAVE" + super.getSortingHelper(focus);
    }

    @Override
    public int getFocusColor(ItemStack focus) {
        return 0xB0B7C4;
    }

    @Override
    public @Nullable Identifier getFocusTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_shockwave");
    }

    @Override
    public @Nullable Identifier getDepthLayerTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_shockwave_depth");
    }

    @Override
    public AspectList getVisCost(ItemStack focus) {
        return COST;
    }

    @Override
    public int getActivationCooldown(ItemStack focus) {
        return 10000;
    }

    @Override
    public void addFocusInformation(ItemStack stack, Consumer<Component> builder) {
        super.addFocusInformation(stack, builder);
        builder.accept(Component.literal(" "));
        int radius = 15 + getUpgradeLevel(stack, FocusUpgradeType.ENLARGE);
        builder.accept(Component.literal("+" + radius + " ").append(Component.translatable("taintedmagic.text.radius")).withStyle(ChatFormatting.BLUE));
    }

    public static AABB area(Player player, int enlarge) {
        double range = 15.0 + enlarge;
        return new AABB(player.getX(), player.getY(), player.getZ(), player.getX() + 1, player.getY() + 1, player.getZ() + 1).inflate(range);
    }

    @Override
    public InteractionResult onFocusRightClick(ItemStack wand, Level level, Player player, @Nullable HitResult hit) {
        if (!consume(wand, player, !level.isClientSide())) {
            return InteractionResult.PASS;
        }
        int potency = Wands.getFocusPotency(wand);
        int enlarge = Wands.getFocusEnlarge(wand);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area(player, enlarge))) {
            if (entity == player || !entity.isAlive() || entity.isInvulnerable()) {
                continue;
            }
            if (level instanceof ServerLevel server) {
                if (player.distanceTo(entity) < 7.0F) {
                    entity.hurtServer(server, server.damageSources().indirectMagic(player, player), 2.0F + potency);
                }
                Vec3 away = entity.position().subtract(player.position()).normalize();
                entity.push(away.x * (5.0 + potency), 1.5 + potency * 0.1, away.z * (5.0 + potency));
                entity.needsSync = true;
            } else {
                Fx.get().shockZap(level, player, entity);
                RandomSource random = level.getRandom();
                for (int index = 0; index < 5; index++) {
                    float x = (float) entity.getX() + (random.nextFloat() - random.nextFloat()) * 0.6F;
                    float y = (float) entity.getY() + (random.nextFloat() - random.nextFloat()) * 0.6F;
                    float z = (float) entity.getZ() + (random.nextFloat() - random.nextFloat()) * 0.6F;
                    Fx.get().sparkle(level, x, y, z, 2.0F + random.nextFloat(), 2, 0.05F + random.nextFloat() * 0.05F);
                }
            }
        }
        if (level instanceof ServerLevel server) {
            server.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SHOCKWAVE.get(), SoundSource.PLAYERS, 5.0F, 1.5F * (float) Math.random());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public FocusUpgradeType @Nullable [] getPossibleUpgradesByRank(ItemStack focus, int rank) {
        return rank >= 1 && rank <= 5 ? upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.ENLARGE, FocusUpgradeType.POTENCY) : null;
    }
}
