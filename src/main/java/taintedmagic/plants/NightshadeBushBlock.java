package taintedmagic.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.block.PlantSoil;

public class NightshadeBushBlock extends VegetationBlock {
    private static final VoxelShape SHAPE = Block.box(4.8, 0.0, 4.8, 11.2, 9.6, 11.2);

    public NightshadeBushBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return PlantSoil.PLAINS.canSustain(state, level, pos);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (level instanceof ServerLevel server && entity instanceof LivingEntity living) {
            living.hurtServer(server, server.damageSources().source(PlantsFeature.NIGHTSHADE), 1.0F);
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 140, 0));
        }
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }
}
