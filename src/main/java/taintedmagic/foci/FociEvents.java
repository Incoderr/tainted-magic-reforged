package taintedmagic.foci;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import thaumcraft.wand.WandItem;
import thaumcraft.wand.Wands;
import thaumcraft.wand.focus.FocusItem;
import taintedmagic.TaintedMagic;

@EventBusSubscriber(modid = TaintedMagic.MODID)
public final class FociEvents {
    public static final double STAFF_MACE_BONUS = 5.0;
    private static final List<Predicate<Player>> LUMOS_SOURCES = new ArrayList<>();

    static {
        LUMOS_SOURCES.add(player -> Wands.isWand(player.getMainHandItem()) && Wands.getFocus(player.getMainHandItem()) instanceof LumosFocus);
    }

    private FociEvents() {
    }

    public static void addLumosSource(Predicate<Player> source) {
        LUMOS_SOURCES.add(source);
    }

    @SubscribeEvent
    static void playerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        applyNightVision(player);
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            updateMace(player.getInventory().getItem(slot));
        }
    }

    public static boolean isLumosVision(MobEffectInstance effect) {
        return effect != null && effect.isAmbient() && effect.getAmplifier() == 0;
    }

    public static void applyNightVision(Player player) {
        boolean lumos = LUMOS_SOURCES.stream().anyMatch(source -> source.test(player));
        MobEffectInstance current = player.getEffect(MobEffects.NIGHT_VISION);
        boolean fromLumos = current != null && isLumosVision(current);
        if (lumos) {
            if (current == null || fromLumos && player.tickCount % 20 == 0) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 260, 0, true, true));
            }
        } else if (fromLumos) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }

    public static void updateMace(ItemStack stack) {
        if (!Wands.isWand(stack)) {
            return;
        }
        if (Wands.getFocus(stack) instanceof MageMaceFocus) {
            double mace = MageMaceFocus.damage(Wands.getFocusItem(stack), Wands.getFocusPotency(stack));
            double amount = Wands.isStaff(stack) ? STAFF_MACE_BONUS + mace : mace;
            ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, amount, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
            if (!modifiers.equals(stack.get(DataComponents.ATTRIBUTE_MODIFIERS))) {
                stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
            }
        } else if (hasMaceModifier(stack)) {
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, stack.getPrototype().get(DataComponents.ATTRIBUTE_MODIFIERS));
        }
    }

    public static boolean hasMaceModifier(ItemStack stack) {
        return !java.util.Objects.equals(stack.get(DataComponents.ATTRIBUTE_MODIFIERS), stack.getPrototype().get(DataComponents.ATTRIBUTE_MODIFIERS));
    }

    @SubscribeEvent
    static void incomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }
        ItemStack held = player.getMainHandItem();
        if (held.getItem() instanceof WandItem wand && Wands.getFocus(held) instanceof FocusItem focus && focus instanceof MageMaceFocus) {
            if (!wand.consumeAllVis(held, player, focus.getVisCost(Wands.getFocusItem(held)), true, false)) {
                event.setCanceled(true);
            }
        }
    }
}
