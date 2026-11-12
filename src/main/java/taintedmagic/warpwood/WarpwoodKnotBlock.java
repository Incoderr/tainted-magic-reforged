package taintedmagic.warpwood;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModSounds;
import taintedmagic.fx.TmFx;

public class WarpwoodKnotBlock extends RotatedPillarBlock {
    public WarpwoodKnotBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.isClientSide()) {
            TmFx.get().wispBurst(level, pos, 15);
        }
        level.playSound(player, pos, ModSounds.CRAFTFAIL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(WarpwoodFeature.WARPWOOD_LOG.get());
    }
}
