package taintedmagic.fx;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface TmFx {
    default void salisSparkle(Entity entity, int type, boolean burst) {
    }

    default void wispBurst(Level level, BlockPos pos, int count) {
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
