package taintedmagic.armor;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import thaumcraft.item.armor.Hover;
import taintedmagic.TaintedMagic;
import taintedmagic.fx.TmFx;

@EventBusSubscriber(modid = TaintedMagic.MODID)
public final class ArmorEvents {
    private static final AttributeModifier VOIDWALKER_STEP = new AttributeModifier(TaintedMagic.id("voidwalker_step"), 0.4, AttributeModifier.Operation.ADD_VALUE);

    private ArmorEvents() {
    }

    private static boolean movingForward(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getLastClientInput().forward() && !serverPlayer.getLastClientInput().backward();
        }
        return player.zza > 0.0F;
    }

    public static boolean wearsVoidwalker(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET).is(ArmorFeature.VOIDWALKER_BOOTS.get());
    }

    @SubscribeEvent
    static void playerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        tickVoidwalker(player);
        FlyteCharmItem.tick(player);
        if (!player.level().isClientSide() && player.tickCount % 20 == 0) {
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                repairVoidtouched(player.getInventory().getItem(slot));
            }
        }
    }

    public static void repairVoidtouched(ItemStack stack) {
        if (VoidBlood.isVoidtouched(stack) && stack.isDamaged()) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    static void tickVoidwalker(Player player) {
        boolean boots = wearsVoidwalker(player);
        boolean forward = movingForward(player);
        AttributeInstance step = player.getAttribute(Attributes.STEP_HEIGHT);
        if (step != null) {
            boolean active = step.hasModifier(VOIDWALKER_STEP.id());
            boolean want = boots && forward && !player.isShiftKeyDown();
            if (want && !active) {
                step.addTransientModifier(VOIDWALKER_STEP);
            } else if (!want && active && (!boots || player.isShiftKeyDown())) {
                step.removeModifier(VOIDWALKER_STEP.id());
            }
        }
        if (!boots) {
            return;
        }
        Vec3 motion = player.getDeltaMovement();
        double speed = Math.abs(motion.x) + Math.abs(motion.z) + Math.abs(0.5 * motion.y);
        if (player.level().isClientSide() && (speed > 0.1 || !player.onGround()) && player.getRandom().nextInt(3) == 0) {
            TmFx.get().voidwalkerTrail(player);
        }
        if (forward) {
            boolean flying = player.getAbilities().flying;
            if (player.onGround() || flying) {
                float bonus = 0.12F;
                if (VoidwalkerSashItem.wornWithSpeed(player)) {
                    bonus *= 2.0F;
                }
                player.moveRelative(flying ? bonus * 0.75F : bonus, new Vec3(0.0, 0.0, 1.0));
            } else if (Hover.isHovering(Hover.getHarness(player))) {
                player.moveRelative(0.01F, new Vec3(0.0, 0.0, 1.0));
            } else {
                player.moveRelative(player.isSprinting() ? 0.019F : 0.02F, new Vec3(0.0, 0.0, 1.0));
            }
        }
        if (player.fallDistance > 3.0) {
            player.fallDistance = 1.0;
        }
    }

    @SubscribeEvent
    static void jump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        Vec3 motion = player.getDeltaMovement();
        double y = motion.y;
        if (wearsVoidwalker(player)) {
            y *= 1.25;
        }
        if (VoidwalkerSashItem.wornWithSpeed(player)) {
            y *= 1.05;
        }
        player.setDeltaMovement(motion.x, y, motion.z);
    }

    @SubscribeEvent
    static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        FlyteCharmItem.forget(event.getEntity());
    }

    @SubscribeEvent
    static void tooltip(ItemTooltipEvent event) {
        if (VoidBlood.isVoidtouched(event.getItemStack())) {
            event.getToolTip().add(Component.translatable("taintedmagic.text.voidtouched").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
