package taintedmagic.foci;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.registry.ModSounds;
import thaumcraft.research.ResearchManager;
import thaumcraft.research.Warp;
import thaumcraft.research.WarpType;
import thaumcraft.wand.Wands;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.TaintedMagic;
import taintedmagic.foci.entity.DarkMatter;
import taintedmagic.foci.entity.Diffusion;

public class DarkMatterFocus extends TmFocus {
    private static final AspectList COST = new AspectList().add(Aspect.ENTROPY, 150).add(Aspect.FIRE, 100);
    private static final AspectList COST_SANITY = COST.copy().add(Aspect.ORDER, 50);
    private static final AspectList COST_CORROSIVE = COST.copy().add(Aspect.WATER, 50);

    public DarkMatterFocus(Properties properties) {
        super(properties);
    }

    @Override
    public String getSortingHelper(ItemStack focus) {
        return "ELDRITCH" + super.getSortingHelper(focus);
    }

    @Override
    public int getFocusColor(ItemStack focus) {
        return 0x000018;
    }

    @Override
    public @Nullable Identifier getFocusTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_dark_matter");
    }

    @Override
    public @Nullable Identifier getDepthLayerTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_dark_matter_depth");
    }

    @Override
    public @Nullable Identifier getOrnamentTexture(ItemStack focus) {
        return Thaumcraft.id("item/focus_warding_orn");
    }

    @Override
    public AspectList getVisCost(ItemStack focus) {
        AspectList cost = isUpgradedWith(focus, FociFeature.SANITY) ? COST_SANITY : isUpgradedWith(focus, FociFeature.CORROSIVE) ? COST_CORROSIVE : COST;
        if (isUpgradedWith(focus, FociFeature.DIFFUSION)) {
            cost = cost.copy();
            for (Aspect aspect : cost.getAspects()) {
                cost.reduce(aspect, (int) (cost.getAmount(aspect) * 0.9));
            }
        }
        return cost;
    }

    @Override
    public int getActivationCooldown(ItemStack focus) {
        return isUpgradedWith(focus, FociFeature.DIFFUSION) ? -1 : 1000;
    }

    @Override
    public boolean isVisCostPerTick(ItemStack focus) {
        return isUpgradedWith(focus, FociFeature.DIFFUSION);
    }

    @Override
    public WandFocusAnimation getAnimation(ItemStack focus) {
        return isUpgradedWith(focus, FociFeature.DIFFUSION) ? WandFocusAnimation.CHARGE : WandFocusAnimation.WAVE;
    }

    @Override
    public InteractionResult onFocusRightClick(ItemStack wand, Level level, Player player, @Nullable HitResult hit) {
        ItemStack focus = focusOf(wand);
        if (isUpgradedWith(focus, FociFeature.DIFFUSION)) {
            return InteractionResult.CONSUME;
        }
        if (level instanceof ServerLevel server && consume(wand, player, true)) {
            server.addFreshEntity(DarkMatter.create(server, player, 16.0F + Wands.getFocusPotency(wand), Wands.getFocusEnlarge(wand), isUpgradedWith(focus, FociFeature.CORROSIVE)));
            if (!isUpgradedWith(focus, FociFeature.SANITY) && server.getRandom().nextInt(20) == 0 && player instanceof ServerPlayer serverPlayer) {
                Warp.add(serverPlayer, 1, WarpType.STICKY);
            }
            server.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ELDRITCH_GUARDIAN_ATTACK.get(), SoundSource.PLAYERS, 0.4F, 1.0F + server.getRandom().nextFloat() * 0.1F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUsingFocusTick(ItemStack wand, Player player, int count) {
        if (!consume(wand, player, false)) {
            player.stopUsingItem();
            return;
        }
        if (!(player.level() instanceof ServerLevel server)) {
            return;
        }
        ItemStack focus = focusOf(wand);
        if (player.tickCount % 15 == 0) {
            server.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.WIND.get(), SoundSource.PLAYERS, 0.3F + 0.2F * (float) Math.random(), 0.75F + 0.25F * (float) Math.random());
        }
        if (consume(wand, player, true)) {
            float scatter = isUpgradedWith(focus, FocusUpgradeType.ENLARGE) ? 12.0F + Wands.getFocusEnlarge(wand) : 9.0F;
            for (int index = 0; index < 2 + Wands.getFocusPotency(wand); index++) {
                server.addFreshEntity(Diffusion.create(server, player, scatter, 12.0F + Wands.getFocusPotency(wand), isUpgradedWith(focus, FociFeature.CORROSIVE)));
            }
            if (!isUpgradedWith(focus, FociFeature.SANITY) && server.getRandom().nextInt(1000) == 0 && player instanceof ServerPlayer serverPlayer) {
                Warp.add(serverPlayer, 1, WarpType.STICKY);
            }
        }
    }

    @Override
    public FocusUpgradeType @Nullable [] getPossibleUpgradesByRank(ItemStack focus, int rank) {
        return switch (rank) {
            case 1, 2, 4 -> upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY, FocusUpgradeType.ENLARGE);
            case 3 -> upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY, FocusUpgradeType.ENLARGE, FociFeature.CORROSIVE, FociFeature.SANITY);
            case 5 -> upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY, FocusUpgradeType.ENLARGE, FociFeature.DIFFUSION);
            default -> null;
        };
    }

    @Override
    public boolean canApplyUpgrade(ItemStack focus, Player player, FocusUpgradeType type, int rank) {
        return type != FociFeature.DIFFUSION || ResearchManager.isComplete(player, "DIFFUSIONUPGRADE");
    }
}
