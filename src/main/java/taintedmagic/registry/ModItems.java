package taintedmagic.registry;

import net.neoforged.bus.api.IEventBus;
import taintedmagic.platform.TmRegistration;
import taintedmagic.TaintedMagic;

public final class ModItems {
    public static final TmRegistration.Items ITEMS = new TmRegistration.Items();

    private ModItems() {
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
