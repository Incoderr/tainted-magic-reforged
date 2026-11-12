package taintedmagic.warpwood;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.block.MagicalSaplingBlock;
import thaumcraft.registry.ModSounds;
import taintedmagic.fx.TmFx;

public class WarpFertilizerItem extends Item {
    public WarpFertilizerItem(Properties properties) {
        super(properties);
    }

    public static boolean isSilverwoodSapling(BlockState state) {
        return state.getBlock() instanceof MagicalSaplingBlock sapling && sapling.isSilverwood();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (player != null && !player.mayUseItemAt(pos, context.getClickedFace(), stack)) {
            return InteractionResult.FAIL;
        }
        if (!isSilverwoodSapling(level.getBlockState(pos))) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            TmFx.get().wispBurst(level, pos, 10 + level.getRandom().nextInt(11));
        }
        level.playSound(player, pos, ModSounds.ROOTS.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!level.isClientSide()) {
            level.setBlock(pos, WarpwoodFeature.WARPWOOD_SAPLING.get().defaultBlockState(), Block.UPDATE_ALL);
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
