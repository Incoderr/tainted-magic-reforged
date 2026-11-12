package taintedmagic.client.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.awt.Color;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.GuiBlit;
import thaumcraft.client.platform.HudVisibility;
import thaumcraft.client.platform.TcRenderTypes;
import taintedmagic.TaintedMagic;
import taintedmagic.platform.ModAttachments;
import taintedmagic.tools.KatanaItem;

public final class KatanaClient {
    private static final ModelLayerLocation KATANA_LAYER = new ModelLayerLocation(TaintedMagic.id("katana"), "main");
    private static final ModelLayerLocation SAYA_LAYER = new ModelLayerLocation(TaintedMagic.id("saya"), "main");
    private static final Identifier CIRCLE = TaintedMagic.id("textures/misc/circle.png");
    private static final Identifier SCRIPT = Thaumcraft.id("textures/misc/script.png");
    private static ModelPart katana;
    private static ModelPart saya;

    private KatanaClient() {
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(KatanaClient::registerLayerDefinitions);
        modEventBus.addListener(KatanaClient::addLayers);
        modEventBus.addListener(KatanaClient::registerGuiLayers);
    }

    private static LayerDefinition katanaLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 48.0F, 4.0F, new CubeDeformation(-0.75F)), PartPose.offset(0.0F, -40.0F, 0.0F));
        PartDefinition grip = root.addOrReplaceChild("grip1", CubeListBuilder.create().texOffs(0, 52).addBox(-2.5F, 0.0F, -3.5F, 5.0F, 1.0F, 7.0F), PartPose.offset(0.0F, -40.0F, 0.0F));
        grip.addOrReplaceChild("grip2", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -12.0F, -1.5F, 2.0F, 12.0F, 3.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 64);
    }

    private static LayerDefinition sayaLayer() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("saya", CubeListBuilder.create().texOffs(10, 0).addBox(-1.0F, 0.5F, -2.0F, 2.0F, 48.0F, 4.0F), PartPose.offset(0.0F, -40.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 64);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(KATANA_LAYER, KatanaClient::katanaLayer);
        event.registerLayerDefinition(SAYA_LAYER, KatanaClient::sayaLayer);
    }

    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        katana = event.getEntityModels().bakeLayer(KATANA_LAYER);
        saya = event.getEntityModels().bakeLayer(SAYA_LAYER);
        for (PlayerModelType skin : event.getSkins()) {
            AvatarRenderer<AbstractClientPlayer> renderer = event.getPlayerRenderer(skin);
            if (renderer != null) {
                renderer.addLayer(new HipLayer(renderer));
            }
        }
    }

    public static Identifier texture(ItemStack stack) {
        String name = stack.getItem() instanceof KatanaItem item ? switch (item.tier()) {
            case THAUMIUM -> "thaumium";
            case VOID_METAL -> "voidmetal";
            case SHADOW_METAL -> "shadowmetal";
        } : "thaumium";
        return TaintedMagic.id("textures/models/model_katana_" + name + ".png");
    }

    private static final class HipLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
        HipLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
            super(renderer);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
            if (state.isInvisible || Minecraft.getInstance().level == null) {
                return;
            }
            Entity entity = Minecraft.getInstance().level.getEntity(state.id);
            if (!(entity instanceof Player player)) {
                return;
            }
            if (player.getData(ModAttachments.FLYTE_CHARM)) {
                circle(poseStack, collector, state.ageInTicks);
            }
            ItemStack stack = player.getData(ModAttachments.HIP_KATANA);
            if (stack.isEmpty()) {
                return;
            }
            var type = TcRenderTypes.entityCutoutNoCull(texture(stack));
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.rotateDegrees(Axis.XP, 55.0F);
            poseStack.rotateDegrees(Axis.YP, 180.0F);
            poseStack.translate(-0.6F, 2.25F, 1.25F);
            collector.submitModelPart(saya, poseStack, type, lightCoords, OverlayTexture.NO_OVERLAY, null);
            if (player.getMainHandItem().getItem() != stack.getItem()) {
                collector.submitModelPart(katana, poseStack, type, lightCoords, OverlayTexture.NO_OVERLAY, null);
            }
            poseStack.popPose();
        }
    }

    private static void circle(PoseStack poseStack, SubmitNodeCollector collector, float age) {
        poseStack.pushPose();
        poseStack.rotate(new org.joml.Quaternionf().rotationAxis((float) Math.toRadians(45.0), -0.70710677F, 0.0F, -0.70710677F));
        poseStack.translate(0.0F, -0.5F, -0.2F);
        poseStack.scale(0.4F, 0.4F, 0.4F);
        poseStack.rotateDegrees(Axis.YP, age);
        collector.submitCustomGeometry(poseStack, TcRenderTypes.entityTranslucent(CIRCLE), (pose, buffer) -> {
            thaumcraft.client.platform.EntityQuadSink sink = new thaumcraft.client.platform.EntityQuadSink(pose, buffer);
            int color = 0xCCFFFFFF;
            int light = 0xF000F0;
            sink.vertex(-1.0F, 0.0F, -1.0F, 0.0F, 0.0F, color, light);
            sink.vertex(-1.0F, 0.0F, 1.0F, 0.0F, 1.0F, color, light);
            sink.vertex(1.0F, 0.0F, 1.0F, 1.0F, 1.0F, color, light);
            sink.vertex(1.0F, 0.0F, -1.0F, 1.0F, 0.0F, color, light);
        });
        poseStack.popPose();
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, TaintedMagic.id("katana_hud"), KatanaClient::renderHud);
    }

    private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.level == null || HudVisibility.hidden()) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof KatanaItem)) {
            return;
        }
        long cooldown = KatanaItem.cooldown(stack, minecraft.level);
        int using = player.isUsingItem() && player.getUseItem() == stack ? player.getTicksUsingItem() : 0;
        float overlay = Math.min(cooldown > 0 ? (float) cooldown / KatanaItem.COOLDOWN_TICKS : (float) using / KatanaItem.CHARGE_TICKS, 1.0F);
        float scale = 0.315F;
        float age = player.tickCount + deltaTracker.getGameTimeDeltaPartialTick(false);
        int x = (int) (graphics.guiWidth() / 2.0F / scale + 30.0F);
        int y = (int) (graphics.guiHeight() / scale - (player.isCreative() ? 100.0F : 150.0F));
        graphics.pose().pushMatrix();
        graphics.pose().scale(scale, scale);
        for (int rune = 0; rune < 16; rune++) {
            float red = Mth.sin((age + rune * 5) / 5.0F) * 0.1F + 0.8F;
            float green = Mth.sin((age + rune * 5) / 7.0F) * 0.1F + 0.7F;
            float alpha = Mth.sin((age + rune * 5) / 10.0F) * 0.3F;
            GuiBlit.blit(graphics, SCRIPT, x + rune * 16, y, rune * 16, 0, 16, 16, 256, 16, argb(red, green, 0.4F, alpha + 0.7F));
            if (Math.ceil(16 * overlay) > rune) {
                float hue = cooldown > 0 ? (16.0F - rune) / 16.0F * 0.3F : rune / 16.0F * 0.3F;
                int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F) & 0xFFFFFF;
                int a = (int) (Mth.clamp(0.9F + alpha, 0.0F, 1.0F) * 255.0F);
                GuiBlit.blit(graphics, SCRIPT, x + rune * 16, y, rune * 16, 0, 16, 16, 256, 16, a << 24 | rgb);
            }
        }
        graphics.pose().popMatrix();
    }

    private static int argb(float red, float green, float blue, float alpha) {
        int a = (int) (Mth.clamp(alpha, 0.0F, 1.0F) * 255.0F);
        return a << 24 | (int) (red * 255.0F) << 16 | (int) (green * 255.0F) << 8 | (int) (blue * 255.0F);
    }
}
