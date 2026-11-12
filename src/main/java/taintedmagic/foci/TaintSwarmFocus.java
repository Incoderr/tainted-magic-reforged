package taintedmagic.foci;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.entity.CombatUtils;
import thaumcraft.entity.monster.taint.TaintSwarm;
import thaumcraft.registry.ModEffects;
import thaumcraft.registry.ModEntities;
import thaumcraft.wand.Wands;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.TaintedMagic;
import taintedmagic.platform.SwarmTargets;

public class TaintSwarmFocus extends TmFocus {
    private static final AspectList COST = new AspectList().add(Aspect.EARTH, 50).add(Aspect.WATER, 50);
    private static final AspectList COST_ANTIBODY = COST.copy().add(Aspect.ORDER, 10);

    public TaintSwarmFocus(Properties properties) {
        super(properties);
    }

    @Override
    public String getSortingHelper(ItemStack focus) {
        return "TAINT" + super.getSortingHelper(focus);
    }

    @Override
    public int getFocusColor(ItemStack focus) {
        return 0x9929BD;
    }

    @Override
    public @Nullable Identifier getFocusTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_taint_swarm");
    }

    @Override
    public @Nullable Identifier getDepthLayerTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_taint_swarm_depth");
    }

    @Override
    public @Nullable Identifier getOrnamentTexture(ItemStack focus) {
        return Thaumcraft.id("item/focus_whatever_orn");
    }

    @Override
    public AspectList getVisCost(ItemStack focus) {
        return isUpgradedWith(focus, FociFeature.ANTIBODY) ? COST_ANTIBODY : COST;
    }

    @Override
    public int getActivationCooldown(ItemStack focus) {
        return 3000;
    }

    @Override
    public InteractionResult onFocusRightClick(ItemStack wand, Level level, Player player, @Nullable HitResult hit) {
        Entity pointed = CombatUtils.getPointedEntity(level, player, 0.0, 32.0, 1.1F);
        if (pointed instanceof LivingEntity target) {
            if (level instanceof ServerLevel server && consume(wand, player, true)) {
                TaintSwarm swarm = new TaintSwarm(ModEntities.TAINT_SWARM.get(), server);
                Vec3 look = player.getLookAngle();
                swarm.snapTo(player.getX() + look.x / 2.0, player.getEyeY() + look.y / 2.0, player.getZ() + look.z / 2.0, player.getYRot(), player.getXRot());
                swarm.setDamageBonus(3 + Wands.getFocusPotency(wand));
                SwarmTargets.set(swarm, target);
                swarm.setSummoned(true);
                server.addFreshEntity(swarm);
                if (!isUpgradedWith(focusOf(wand), FociFeature.ANTIBODY) && server.getRandom().nextInt(3) == 0) {
                    player.addEffect(new MobEffectInstance(ModEffects.TAINT_POISON, 40, 2));
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public FocusUpgradeType @Nullable [] getPossibleUpgradesByRank(ItemStack focus, int rank) {
        return switch (rank) {
            case 1, 2, 4, 5 -> upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY);
            case 3 -> upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY, FociFeature.ANTIBODY);
            default -> null;
        };
    }
}
