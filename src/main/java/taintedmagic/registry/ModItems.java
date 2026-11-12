package taintedmagic.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import taintedmagic.TaintedMagic;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TaintedMagic.MODID);

    private ModItems() {
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
