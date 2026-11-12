package taintedmagic.client.armor;

import java.awt.Color;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import javax.annotation.Nullable;
import thaumcraft.client.render.armor.ArmorKind;
import thaumcraft.client.render.armor.TcArmorModel;
import thaumcraft.item.armor.ArmorSupport;
import taintedmagic.TaintedMagic;
import taintedmagic.armor.ArmorFeature;
import taintedmagic.armor.FlyteCharmItem;
import taintedmagic.armor.GateKeyItem;

public final class ArmorClient {
    private static final Map<EquipmentSlot, TcArmorModel> FORTRESS = new EnumMap<>(EquipmentSlot.class);

    private ArmorClient() {
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ArmorClient::registerExtensions);
        modEventBus.addListener(ArmorClient::registerTints);
        NeoForge.EVENT_BUS.addListener(ArmorClient::loggingOut);
    }

    private static @Nullable TcArmorModel fortress(ItemStack stack) {
        if (FORTRESS.isEmpty()) {
            var models = Minecraft.getInstance().getEntityModels();
            FORTRESS.put(EquipmentSlot.HEAD, new TcArmorModel(models.bakeLayer(ArmorKind.FORTRESS.inner()), ArmorKind.FORTRESS, EquipmentSlot.HEAD));
            FORTRESS.put(EquipmentSlot.CHEST, new TcArmorModel(models.bakeLayer(ArmorKind.FORTRESS.outer()), ArmorKind.FORTRESS, EquipmentSlot.CHEST));
            FORTRESS.put(EquipmentSlot.LEGS, new TcArmorModel(models.bakeLayer(ArmorKind.FORTRESS.inner()), ArmorKind.FORTRESS, EquipmentSlot.LEGS));
        }
        EquipmentSlot slot = ArmorSupport.slotOf(stack);
        return slot == null ? null : FORTRESS.get(slot);
    }

    private static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                TcArmorModel model = fortress(stack);
                return model == null ? original : model;
            }

            @Override
            public void setupModelAnimations(LivingEntity entity, ItemStack stack, EquipmentSlot slot, Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
                if (model instanceof TcArmorModel armor) {
                    armor.animateArmor(entity, limbSwing, limbSwingAmount, ageInTicks);
                }
            }
        },
            ArmorFeature.VOID_FORTRESS_HELMET.get(),
            ArmorFeature.VOID_FORTRESS_CHESTPLATE.get(),
            ArmorFeature.VOID_FORTRESS_LEGGINGS.get(),
            ArmorFeature.SHADOW_FORTRESS_HELMET.get(),
            ArmorFeature.SHADOW_FORTRESS_CHESTPLATE.get(),
            ArmorFeature.SHADOW_FORTRESS_LEGGINGS.get()
        );
    }

    private static void registerTints(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 1 ? gateKeyColor(stack) : -1, ArmorFeature.GATE_KEY.get());
    }

    private static void loggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        FlyteCharmItem.forgetClient();
    }

    public static int gateKeyColor(ItemStack stack) {
        GateKeyItem.Binding binding = GateKeyItem.binding(stack);
        if (binding == null || binding.colour() == 0) {
            return 0xFFFFFFFF;
        }
        var player = Minecraft.getInstance().player;
        float brightness = player != null ? 0.2F * (float) Math.sin(player.tickCount / 10.0) + 0.8F : 1.0F;
        return 0xFF000000 | Color.HSBtoRGB(binding.colour() / 360.0F, 1.0F, brightness);
    }
}