package taintedmagic.client;

import net.neoforged.bus.api.IEventBus;
import taintedmagic.client.fx.TmClientFx;
import taintedmagic.fx.TmFx;
import taintedmagic.research.TmResearch;

public final class ClientFeatures {
    private ClientFeatures() {
    }

    public static void init(IEventBus modEventBus) {
        TmFx.set(new TmClientFx());
        TmResearch.setTags(TmClientConfig::researchTags);
    }
}
