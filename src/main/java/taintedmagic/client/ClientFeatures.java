package taintedmagic.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import thaumcraft.client.render.entity.eldritch.EldritchOrbRenderer;
import taintedmagic.client.foci.ParticleSpriteRenderer;
import taintedmagic.client.fx.TmClientFx;
import taintedmagic.foci.FociFeature;
import taintedmagic.fx.TmFx;
import taintedmagic.research.TmResearch;

public final class ClientFeatures {
    private ClientFeatures() {
    }

    public static void init(IEventBus modEventBus) {
        TmFx.set(new TmClientFx());
        TmResearch.setTags(TmClientConfig::researchTags);
        modEventBus.addListener(ClientFeatures::registerRenderers);
        taintedmagic.client.armor.ArmorClient.init(modEventBus);
        taintedmagic.client.tools.KatanaClient.init(modEventBus);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FociFeature.DARK_MATTER.get(), EldritchOrbRenderer::new);
        event.registerEntityRenderer(FociFeature.HOMING_SHARD.get(), ParticleSpriteRenderer::shard);
        event.registerEntityRenderer(FociFeature.DIFFUSION_ENTITY.get(), ParticleSpriteRenderer::diffusion);
    }
}
