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

    private static SparkleFx sparkle(RandomSource random, double x, double y, double z, int type, int multiplier, float gravity) {
        SparkleFx fx = new SparkleFx(random, x, y, z, 1.75F, type, multiplier);
        fx.gravity(gravity);
        WorldFxRenderer.add(fx);
        return fx;
    }

    @Override
    public void lumosPlace(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        for (int index = 0; index < 9; index++) {
            sparkle(random, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat(), pos.getZ() + random.nextFloat(), 6, 3 + random.nextInt(3), 0.1F);
        }
    }

    @Override
    public void lumosBreak(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        for (int index = 0; index < 9; index++) {
            SparkleFx fx = sparkle(random, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat(), pos.getZ() + random.nextFloat(), 6, 3 + random.nextInt(3), 0.5F);
            fx.motion(random.nextGaussian() * 0.1, random.nextGaussian() * 0.1, random.nextGaussian() * 0.1);
        }
    }

    @Override
    public void lumosGlow(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        for (int index = 0; index < 2; index++) {
            sparkle(
                random,
                pos.getX() + 0.5 + random.nextGaussian() * 0.1,
                pos.getY() + 0.5 + random.nextGaussian() * 0.1,
                pos.getZ() + 0.5 + random.nextGaussian() * 0.1,
                6,
                3 + random.nextInt(2),
                -0.5F
            );
        }
    }

    @Override
    public void shardCast(Level level, double x, double y, double z) {
        RandomSource random = level.getRandom();
        for (int index = 0; index < 18; index++) {
            sparkle(random, x + random.nextFloat(), y + random.nextFloat(), z + random.nextFloat(), 0, 3 + random.nextInt(3), 0.1F);
        }
    }

    @Override
    public void diffusionBurst(Level level, double x, double y, double z) {
        RandomSource random = level.getRandom();
        float dx = (random.nextFloat() - random.nextFloat()) * 0.3F;
        float dy = (random.nextFloat() - random.nextFloat()) * 0.3F;
        float dz = (random.nextFloat() - random.nextFloat()) * 0.3F;
        WispFx fx = new WispFx(random, x + dx, y + dy, z + dz, 0.3F, 5, null).moveTo(x + dx * 8.0F, y + dy * 8.0F, z + dz * 8.0F).shrink(true);
        fx.gravity(0.02F);
        WorldFxRenderer.add(fx);
    }

    @Override
    public void gateSparkle(Level level, double x, double y, double z) {
        RandomSource random = level.getRandom();
        sparkle(random, x + 0.33F * random.nextGaussian(), y + 0.5 + random.nextFloat(), z + 0.33F * random.nextGaussian(), 6, 3 + random.nextInt(3), 0.1F);
    }

    @Override
    public void voidwalkerTrail(Entity entity) {
        RandomSource random = entity.level().getRandom();
        WispFx fx = new WispFx(
            random,
            entity.getX() + (Math.random() - Math.random()) * 0.5,
            entity.getBoundingBox().minY + 0.05 + (Math.random() - Math.random()) * 0.1,
            entity.getZ() + (Math.random() - Math.random()) * 0.5,
            0.6F,
            0,
            null
        ).shrink(true);
        fx.color(0.5F, 0.0F, 0.6F);
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
