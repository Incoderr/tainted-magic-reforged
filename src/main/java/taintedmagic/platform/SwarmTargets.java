package taintedmagic.platform;

import java.lang.reflect.Field;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.entity.monster.taint.TaintSwarm;

public final class SwarmTargets {
    private static final Field TARGET = field();

    private SwarmTargets() {
    }

    private static Field field() {
        try {
            Field field = TaintSwarm.class.getDeclaredField("target");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException exception) {
            throw new IllegalStateException("TaintSwarm.target is missing", exception);
        }
    }

    public static void set(TaintSwarm swarm, LivingEntity target) {
        try {
            TARGET.set(swarm, target);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public static LivingEntity get(TaintSwarm swarm) {
        try {
            return (LivingEntity) TARGET.get(swarm);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
