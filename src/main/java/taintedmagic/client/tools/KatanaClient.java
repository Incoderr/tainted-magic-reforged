package taintedmagic.client.tools;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.awt.Color;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import thaumcraft.client.platform.GuiBlit;
import thaumcraft.client.platform.HudVisibility;
import taintedmagic.TaintedMagic;
import taintedmagic.platform.ModAttachments;
import taintedmagic.tools.KatanaItem;
import taintedmagic.tools.ToolsFeature;

public final class KatanaClient {
    private static final ModelLayerLocation KATANA_LAYER = new ModelLayerLocation(TaintedMagic.id("katana"), "main");
    private static final ModelLayerLocation SAYA_LAYER = new ModelLayerLocation(TaintedMagic.id("saya"), "main");
    private static final ResourceLocation CIRCLE = TaintedMagic.id("textures/misc/circle.png");
    private static final ResourceLocation SCRIPT = ResourceLocation.fromNamespaceAndPath("thaumcraft", "textures/misc/script.png");
    private static final String[] KATANAS = {"thaumium_katana", "void_metal_katana", "shadow_metal_katana"};
    private static ModelPart katana;
    private static ModelPart saya;

    private KatanaClient() {
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(KatanaClient::registerLayerDefinitions);
        modEventBus.addListener(KatanaClient::addLayers);
        modEventBus.addListener(KatanaClient::registerGuiLayers);
        modEventBus.addListener(KatanaClient::registerModels);
        modEventBus.addListener(KatanaClient::modifyModels);
        modEventBus.addListener(KatanaClient::setup);
    }

    private static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (Item item : new Item[] {ToolsFeature.THAUMIUM_KATANA.get(), ToolsFeature.VOID_METAL_KATANA.get(), ToolsFeature.SHADOW_METAL_KATANA.get()}) {
                ItemProperties.register(item, TaintedMagic.id("charging"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            }
        });
    }

    private static ModelResourceLocation sheathed(String name) {
        return ModelResourceLocation.standalone(TaintedMagic.id("item/" + name + "_sheathed"));
    }

    private static void registerModels(ModelEvent.RegisterAdditional event) {
        for (String name : KATANAS) {
            event.register(sheathed(name));
        }
    }

    private static void modifyModels(ModelEvent.ModifyBakingResult event) {
        var models = event.getModels();
        for (String name : KATANAS) {
            ModelResourceLocation key = ModelResourceLocation.inventory(TaintedMagic.id(name));
            BakedModel bare = models.get(key);
            BakedModel sheath = models.get(sheathed(name));
            if (bare != null && sheath != null) {
                models.put(key, new SheathedModel(bare, sheath));
            }
        }
    }

    private static final class SheathedModel extends BakedModelWrapper<BakedModel> {
        private final BakedModel sheath;

        SheathedModel(BakedModel bare, BakedModel sheath) {
            super(bare);
            this.sheath = sheath;
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext context, PoseStack poseStack, boolean leftHand) {
            if (context == ItemDisplayContext.GUI || context == ItemDisplayContext.GROUND || context == ItemDisplayContext.FIXED) {
                return sheath.applyTransform(context, poseStack, leftHand);
            }
            return super.applyTransform(context, poseStack, leftHand);
        }
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
        for (PlayerSkin.Model skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new HipLayer(renderer));
            }
        }
    }

    public static ResourceLocation texture(ItemStack stack) {
        String name = stack.getItem() instanceof KatanaItem item ? switch (item.tier()) {
            case THAUMIUM -> "thaumium";
            case VOID_METAL -> "voidmetal";
            case SHADOW_METAL -> "shadowmetal";
        } : "thaumium";
        return TaintedMagic.id("textures/models/model_katana_" + name + ".png");
    }

    private static final class HipLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
        HipLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int light, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (player.isInvisible()) {
                return;
            }
            if (player.getData(ModAttachments.FLYTE_CHARM)) {
                circle(poseStack, buffers, ageInTicks);
            }
            ItemStack stack = player.getData(ModAttachments.HIP_KATANA);
            if (stack.isEmpty()) {
                return;
            }
            VertexConsumer buffer = buffers.getBuffer(RenderType.entityCutoutNoCull(texture(stack)));
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(Axis.XP.rotationDegrees(55.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.translate(-0.6F, 2.25F, 1.25F);
            saya.render(poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            if (player.getMainHandItem().getItem() != stack.getItem()) {
                katana.render(poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            }
            poseStack.popPose();
        }
    }

    private static void circle(PoseStack poseStack, MultiBufferSource buffers, float age) {
        poseStack.pushPose();
        poseStack.mulPose(new org.joml.Quaternionf().rotationAxis((float) Math.toRadians(45.0), -0.70710677F, 0.0F, -0.70710677F));
        poseStack.translate(0.0F, -0.5F, -0.2F);
        poseStack.scale(0.4F, 0.4F, 0.4F);
        poseStack.mulPose(Axis.YP.rotationDegrees(age));
        VertexConsumer buffer = buffers.getBuffer(RenderType.entityTranslucent(CIRCLE));
        PoseStack.Pose pose = poseStack.last();
        int color = 0xCCFFFFFF;
        int light = 0xF000F0;
        float[][] corners = {{-1.0F, -1.0F, 0.0F, 0.0F}, {-1.0F, 1.0F, 0.0F, 1.0F}, {1.0F, 1.0F, 1.0F, 1.0F}, {1.0F, -1.0F, 1.0F, 0.0F}};
        for (float[] corner : corners) {
            buffer.addVertex(pose, corner[0], 0.0F, corner[1]).setColor(color).setUv(corner[2], corner[3]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        }
        poseStack.popPose();
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, TaintedMagic.id("katana_hud"), KatanaClient::renderHud);
    }

    private static void renderHud(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        var player = minecraft.player;
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
        RenderSystem.enableBlend();
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0F);
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
        graphics.pose().popPose();
        RenderSystem.disableBlend();
    }

    private static int argb(float red, float green, float blue, float alpha) {
        int a = (int) (Mth.clamp(alpha, 0.0F, 1.0F) * 255.0F);
        return a << 24 | (int) (red * 255.0F) << 16 | (int) (green * 255.0F) << 8 | (int) (blue * 255.0F);
    }
}
