package taintedmagic.foci;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.entity.CombatUtils;
import thaumcraft.wand.Wands;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.TaintedMagic;
import taintedmagic.foci.entity.HomingShard;
import taintedmagic.fx.TmFx;
import taintedmagic.registry.ModSounds;

public class VisShardFocus extends TmFocus {
    private static final AspectList COST = new AspectList().add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10).add(Aspect.AIR, 10);
    private static final AspectList COST_PERSISTENT = COST.copy().add(Aspect.WATER, 10);

    public VisShardFocus(Properties properties) {
        super(properties);
    }

    @Override
    public String getSortingHelper(ItemStack focus) {
        return "SHARD" + super.getSortingHelper(focus);
    }

    @Override
    public int getFocusColor(ItemStack focus) {
        return 0x9929BD;
    }

    @Override
    public @Nullable Identifier getFocusTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_vis_shard");
    }

    @Override
    public int getActivationCooldown(ItemStack focus) {
        return 300;
    }

    @Override
    public AspectList getVisCost(ItemStack focus) {
        return isUpgradedWith(focus, FociFeature.PERSISTENT) ? COST_PERSISTENT : COST;
    }

    @Override
    public InteractionResult onFocusRightClick(ItemStack wand, Level level, Player player, @Nullable HitResult hit) {
        Entity look = CombatUtils.getPointedEntity(level, player, 0.0, 32.0, 1.1F);
        if (look instanceof LivingEntity target) {
            if (consume(wand, player, !level.isClientSide())) {
                Vec3 view = player.getLookAngle();
                double x = player.getX() + view.x / 2.0;
                double y = player.getEyeY() + view.y / 2.0;
                double z = player.getZ() + view.z / 2.0;
                if (level instanceof ServerLevel server) {
                    HomingShard shard = HomingShard.create(server, player, target, Wands.getFocusPotency(wand), isUpgradedWith(focusOf(wand), FociFeature.PERSISTENT));
                    server.addFreshEntity(shard);
                    server.playSound(null, x, y, z, ModSounds.SHARD.get(), SoundSource.PLAYERS, 0.3F, 1.1F + server.getRandom().nextFloat() * 0.1F);
                } else {
                    TmFx.get().shardCast(level, x, y, z);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public FocusUpgradeType @Nullable [] getPossibleUpgradesByRank(ItemStack focus, int rank) {
        return switch (rank) {
            case 1, 2, 3, 4 -> upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY);
            case 5 -> upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY, FociFeature.PERSISTENT);
            default -> null;
        };
    }
}
