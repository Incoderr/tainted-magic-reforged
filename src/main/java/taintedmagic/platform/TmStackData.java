package taintedmagic.platform;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import taintedmagic.TaintedMagic;

public final class TmStackData {
    private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TaintedMagic.MODID);

    private TmStackData() {
    }

    public static <T> TmStackKey<T> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return new TmStackKey<>(COMPONENTS.registerComponentType(name, builder -> builder.persistent(codec).networkSynchronized(streamCodec)));
    }

    public static <T> TmStackKey<T> registerTransient(String name) {
        return new TmStackKey<>(COMPONENTS.<T>registerComponentType(name, builder -> builder));
    }

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
