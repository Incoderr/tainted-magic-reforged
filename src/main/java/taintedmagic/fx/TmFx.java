package taintedmagic.fx;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface TmFx {
    default void salisSparkle(Entity entity, int type, boolean burst) {
    }

    default void wispBurst(Level level, BlockPos pos, int count) {
    }

    default void lumosPlace(Level level, BlockPos pos) {
    }

    default void lumosBreak(Level level, BlockPos pos) {
    }

    default void lumosGlow(Level level, BlockPos pos) {
    }

    default void shardCast(Level level, double x, double y, double z) {
    }

    default void diffusionBurst(Level level, double x, double y, double z) {
    }

    static TmFx get() {
        return Holder.fx;
    }

    static void set(TmFx fx) {
        Holder.fx = fx;
    }

    final class Holder {
        private static TmFx fx = new TmFx() {
        };

        private Holder() {
        }
    }
}
