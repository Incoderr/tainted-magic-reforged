package taintedmagic.client.foci;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import taintedmagic.foci.entity.Diffusion;
import taintedmagic.foci.entity.HomingShard;

public final class ParticleSpriteRenderer<T extends Entity> extends EntityRenderer<T, ParticleSpriteRenderer.State> {
    private static final float CELL = 0.0625F;

    private ParticleSpriteRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    public static ParticleSpriteRenderer<HomingShard> shard(EntityRendererProvider.Context context) {
        return new ParticleSpriteRenderer<>(context);
    }

    public static ParticleSpriteRenderer<Diffusion> diffusion(EntityRendererProvider.Context context) {
        return new ParticleSpriteRenderer<>(context);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(T entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.age = entity.tickCount;
        state.shard = entity instanceof HomingShard;
        state.strength = entity instanceof HomingShard shard ? shard.getStrength() : 0;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.shard) {
            float u0 = (8 + state.age % 8) / 16.0F;
            float v0 = 0.25F;
            int color = argb(1.0F, 0.405F, 0.075F, 0.525F);
            Billboards.submit(poseStack, collector, camera, TcRenderTypes.translucentNoDepth(ParticleFx.PARTICLES), u0, u0 + CELL, v0, v0 + CELL, 0.4F + 0.1F * state.strength, color, 0xF000F0);
        } else {
            float u0 = state.age % 13 / 16.0F;
            float v0 = 0.1875F;
            float alpha = Math.max(0.0F, (Diffusion.LIFETIME - state.age) / 40.0F);
            int color = argb(alpha, 0.1F, 0.1F, 0.1F);
            Billboards.submit(poseStack, collector, camera, TcRenderTypes.translucent(ParticleFx.PARTICLES), u0, u0 + CELL, v0, v0 + CELL, 0.5F, color, state.lightCoords);
        }
        super.submit(state, poseStack, collector, camera);
    }

    private static int argb(float alpha, float red, float green, float blue) {
        return (int) (alpha * 255.0F) << 24 | (int) (red * 255.0F) << 16 | (int) (green * 255.0F) << 8 | (int) (blue * 255.0F);
    }

    public static final class State extends EntityRenderState {
        int age;
        boolean shard;
        int strength;
    }
}
