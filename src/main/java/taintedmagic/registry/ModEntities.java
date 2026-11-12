package taintedmagic.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import taintedmagic.platform.TmRegistration;
import taintedmagic.TaintedMagic;

public final class ModEntities {
    private static final TmRegistration.Entities ENTITIES = new TmRegistration.Entities();

    private ModEntities() {
    }

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> projectile(String name, EntityType.EntityFactory<T> factory, float size) {
        return ENTITIES.registerEntityType(name, factory, MobCategory.MISC, builder -> builder.sized(size, size).clientTrackingRange(4).updateInterval(10));
    }

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
