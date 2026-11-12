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
        return player.level().isClientSide() && player.zza > 0.0F;
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
            player.fallDistance = 1.0F;
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
    static void damage(net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre event) {
        double delta = specialArmorDelta(event.getEntity(), event.getSource());
        if (delta != 0.0) {
            event.setNewDamage((float) (event.getNewDamage() * (1.0 - delta)));
        }
    }

    public static double specialArmorDelta(net.minecraft.world.entity.LivingEntity entity, net.minecraft.world.damagesource.DamageSource source) {
        boolean magic = source.is(net.minecraft.world.damagesource.DamageTypes.MAGIC) || source.is(net.minecraft.world.damagesource.DamageTypes.INDIRECT_MAGIC);
        boolean fire = source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE) || source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION);
        boolean bypass = source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR);
        double delta = 0.0;
        if (entity.getItemBySlot(EquipmentSlot.FEET).is(ArmorFeature.VOIDWALKER_BOOTS.get())) {
            double defense = ArmorFeature.VOIDWALKER.bootsDefense();
            double tm = magic || fire ? defense / 80.0 : bypass ? 0.0 : defense / 90.0;
            double vanilla = bypass ? 0.0 : defense / 25.0;
            delta += tm - vanilla;
        }
        if (bypass && !magic && !fire) {
            return delta;
        }
        int pieces = 0;
        int masks = 0;
        double shadow = 0.0;
        for (EquipmentSlot slot : new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS}) {
            ItemStack piece = entity.getItemBySlot(slot);
            if (piece.getItem() instanceof thaumcraft.item.armor.FortressArmorItem) {
                pieces++;
                if (thaumcraft.item.armor.FortressArmorItem.hasMask(piece)) {
                    masks++;
                }
                if (piece.getItem() instanceof ShadowFortressArmorItem) {
                    shadow += ArmorFeature.SHADOW_FORTRESS.defense(switch (slot) {
                        case HEAD -> thaumcraft.platform.ArmorSlot.HELMET;
                        case CHEST -> thaumcraft.platform.ArmorSlot.CHESTPLATE;
                        default -> thaumcraft.platform.ArmorSlot.LEGGINGS;
                    });
                }
            }
        }
        if (shadow > 0.0) {
            double divisor = magic ? 35.0 : fire ? 20.0 : 25.0;
            double tmSet = 0.75 + 0.15 * pieces + 0.05 * masks;
            delta += shadow / divisor * (tmSet - thaumcraft.item.armor.ArmorEvents.fortressSet(entity));
        }
        return delta;
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
