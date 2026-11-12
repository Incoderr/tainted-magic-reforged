package taintedmagic.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.item.gear.Repairable;

public class ShadowMetalHoeItem extends Item implements Repairable {
    public ShadowMetalHoeItem(Properties properties) {
        super(properties);
    }

    public static Block toggled(BlockState state) {
        if (state.is(Blocks.FARMLAND)) {
            return Blocks.DIRT;
        }
        if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)) {
            return Blocks.FARMLAND;
        }
        return null;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Block result = toggled(level.getBlockState(pos));
        if (result == null || player != null && !player.mayUseItemAt(pos, context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(pos, result.defaultBlockState());
            level.playSound(null, pos, SoundEvents.HOE_TILL.value(), SoundSource.BLOCKS, 1.0F, 0.8F);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
