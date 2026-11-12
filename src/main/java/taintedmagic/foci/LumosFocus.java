package taintedmagic.foci;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import javax.annotation.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.registry.ModSounds;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.TaintedMagic;
import taintedmagic.fx.TmFx;

public class LumosFocus extends TmFocus {
    private static final AspectList COST = new AspectList().add(Aspect.AIR, 10).add(Aspect.FIRE, 25);

    public LumosFocus(Properties properties) {
        super(properties);
    }

    @Override
    public String getSortingHelper(ItemStack focus) {
        return "LUMOS" + super.getSortingHelper(focus);
    }

    @Override
    public int getFocusColor(ItemStack focus) {
        return 0xC9D2F8;
    }

    @Override
    public @Nullable ResourceLocation getFocusTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_lumos");
    }

    @Override
    public AspectList getVisCost(ItemStack focus) {
        return COST;
    }

    @Override
    public int getActivationCooldown(ItemStack focus) {
        return 1000;
    }

    @Override
    public InteractionResult onFocusRightClick(ItemStack wand, Level level, Player player, @Nullable HitResult hit) {
        if (hit instanceof BlockHitResult block) {
            BlockPos pos = block.getBlockPos();
            if (!level.getBlockState(pos).canBeReplaced()) {
                pos = pos.relative(block.getDirection());
            }
            if (level.getBlockState(pos).canBeReplaced()
                && player.mayUseItemAt(pos, block.getDirection(), wand)
                && level.mayInteract(player, pos)
                && consume(wand, player, !level.isClientSide())) {
                if (level.isClientSide()) {
                    TmFx.get().lumosPlace(level, pos);
                } else {
                    level.setBlock(pos, FociFeature.LUMOS.get().defaultBlockState(), Block.UPDATE_ALL);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ICE.get(), SoundSource.PLAYERS, 0.3F, 1.1F + level.getRandom().nextFloat() * 0.1F);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @Nullable public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack focus, int rank) {
        return rank >= 1 && rank <= 5 ? upgrades(FocusUpgradeType.FRUGAL) : null;
    }
}
