package taintedmagic.client.foci;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import taintedmagic.foci.entity.Diffusion;
import taintedmagic.foci.entity.HomingShard;

public final class ParticleSpriteRenderer<T extends Entity> extends EntityRenderer<T> {
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
    public ResourceLocation getTextureLocation(T entity) {
        return ParticleFx.PARTICLES;
    }

    @Override
    public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light) {
        int age = entity.tickCount;
        if (entity instanceof HomingShard shard) {
            float u0 = (8 + age % 8) / 16.0F;
            float v0 = 0.25F;
            int color = argb(1.0F, 0.405F, 0.075F, 0.525F);
            Billboards.submit(poseStack, buffers, this.entityRenderDispatcher.camera, TcRenderTypes.translucentNoDepth(ParticleFx.PARTICLES), u0, u0 + CELL, v0, v0 + CELL, 0.4F + 0.1F * shard.getStrength(), color, 0xF000F0);
        } else {
            float u0 = age % 13 / 16.0F;
            float v0 = 0.1875F;
            float alpha = Math.max(0.0F, (Diffusion.LIFETIME - age) / 40.0F);
            int color = argb(alpha, 0.1F, 0.1F, 0.1F);
            Billboards.submit(poseStack, buffers, this.entityRenderDispatcher.camera, TcRenderTypes.translucent(ParticleFx.PARTICLES), u0, u0 + CELL, v0, v0 + CELL, 0.5F, color, light);
        }
        super.render(entity, yaw, partialTick, poseStack, buffers, light);
    }

    private static int argb(float alpha, float red, float green, float blue) {
        return (int) (alpha * 255.0F) << 24 | (int) (red * 255.0F) << 16 | (int) (green * 255.0F) << 8 | (int) (blue * 255.0F);
    }
}