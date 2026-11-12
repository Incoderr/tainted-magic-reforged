package taintedmagic.warpwood;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class WarpwoodLeavesBlock extends LeavesBlock {
    public WarpwoodLeavesBlock(Properties properties) {
        super(AmbientLeavesBlockSoundPlayer.noAmbientSound(), properties);
    }

    @Override
    protected int getLightDampening(BlockState state) {
        return 0;
    }
}
