package taintedmagic.client.fx;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import thaumcraft.client.fx.SparkleFx;
import thaumcraft.client.fx.WispFx;
import thaumcraft.client.platform.WorldFxRenderer;
import taintedmagic.fx.TmFx;

public final class TmClientFx implements TmFx {
    @Override
    public void salisSparkle(Entity entity, int type, boolean burst) {
        RandomSource random = entity.level().getRandom();
        double theta = Math.random() * Math.PI;
        double phi = Math.random() * Math.PI * 2.0;
        double x = Math.cos(phi) * Math.sin(theta) * 0.25;
        double y = Math.sin(phi) * Math.sin(theta) * 0.25;
        double z = Math.cos(theta) * 0.25;
        SparkleFx fx = new SparkleFx(
            random,
            entity.getX() + x,
            entity.getBoundingBox().maxY + y,
            entity.getZ() + z,
            1.75F,
            type,
            burst ? 30 + random.nextInt(5) : 3 + random.nextInt(2)
        );
        fx.gravity(burst ? 0.0F : -0.1F);
        if (burst) {
            fx.motion(x * 3.0, y * 3.0, z * 3.0);
        }
        WorldFxRenderer.add(fx);
    }

    @Override
    public void wispBurst(Level level, BlockPos pos, int count) {
        RandomSource random = level.getRandom();
        for (int index = 0; index < count; index++) {
            double x = pos.getX() + 0.5 + (Math.random() - Math.random()) * 0.5;
            double y = pos.getY() + 0.5 + (Math.random() - Math.random()) * 0.5;
            double z = pos.getZ() + 0.5 + (Math.random() - Math.random()) * 0.5;
            WispFx fx = new WispFx(random, x, y, z, 0.25F + (float) Math.random() * 0.25F, 5, null);
            fx.gravity(0.01F);
            fx.motion((x - pos.getX() - 0.5) * 0.025, 0.0, (z - pos.getZ() - 0.5) * 0.025);
            WorldFxRenderer.add(fx);
        }
    }
}
