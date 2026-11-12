package taintedmagic.platform;

import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import taintedmagic.TaintedMagic;

public final class TmRegistration {
    private TmRegistration() {
    }

    public static final class Items extends DeferredRegister.Items {
        public Items() {
            super(TaintedMagic.MODID);
        }

        public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> factory, UnaryOperator<Item.Properties> properties) {
            return register(name, () -> factory.apply(properties.apply(new Item.Properties())));
        }

        public DeferredItem<BlockItem> registerSimpleBlockItem(DeferredBlock<? extends Block> block, UnaryOperator<Item.Properties> properties) {
            return registerSimpleBlockItem(block, properties.apply(new Item.Properties()));
        }
    }

    public static final class Blocks extends DeferredRegister.Blocks {
        public Blocks() {
            super(TaintedMagic.MODID);
        }

        public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory, UnaryOperator<BlockBehaviour.Properties> properties) {
            return register(name, () -> factory.apply(properties.apply(BlockBehaviour.Properties.of())));
        }
    }

    public static final class Entities {
        private final DeferredRegister<EntityType<?>> register = DeferredRegister.create(Registries.ENTITY_TYPE, TaintedMagic.MODID);

        public <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> builder) {
            return register.register(name, () -> builder.apply(EntityType.Builder.of(factory, category)).build(name));
        }

        public void register(net.neoforged.bus.api.IEventBus bus) {
            register.register(bus);
        }
    }
}