package taintedmagic.armor;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.item.gear.WarpingGear;

public class FlyteCharmItem extends Item implements WarpingGear {
    public static final AspectList COST_FLIGHT = new AspectList().add(Aspect.AIR, 15);
    public static final AspectList COST_GLIDE = new AspectList().add(Aspect.AIR, 5);

    private static final Set<UUID> FLYING_CLIENT = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> FLYING_SERVER = ConcurrentHashMap.newKeySet();

    public FlyteCharmItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 5;
    }

    private static Set<UUID> flying(Player player) {
        return player.level().isClientSide() ? FLYING_CLIENT : FLYING_SERVER;
    }

    public static boolean carries(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).getItem() instanceof FlyteCharmItem) {
                return true;
            }
        }
        return false;
    }

    public static boolean canFly(Player player) {
        return carries(player) && TmVis.consumeFromInventory(player, COST_FLIGHT, false);
    }

    public static boolean isFlyer(Player player) {
        return flying(player).contains(player.getUUID());
    }

    public static void tick(Player player) {
        Set<UUID> flyers = flying(player);
        UUID id = player.getUUID();
        boolean before = player.getAbilities().mayfly;
        boolean wasFlying = player.getAbilities().flying;
        if (flyers.contains(id)) {
            if (canFly(player)) {
                player.getAbilities().mayfly = true;
                boolean isFlying = player.getAbilities().flying;
                TmVis.consumeFromInventory(player, COST_FLIGHT, isFlying);
                if (!isFlying && player.isShiftKeyDown() && !player.onGround() && player.fallDistance > 0.5 && TmVis.consumeFromInventory(player, COST_GLIDE, true)) {
                    double speed = 0.1;
                    Vec3 motion = player.getDeltaMovement();
                    double angle = Math.toRadians(player.getYHeadRot() + 90.0F);
                    player.setDeltaMovement(motion.x + Math.cos(angle) * speed, -speed, motion.z + Math.sin(angle) * speed);
                }
            } else {
                if (!player.getAbilities().instabuild && !player.isSpectator()) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                }
                flyers.remove(id);
            }
        } else if (canFly(player)) {
            flyers.add(id);
            player.getAbilities().mayfly = true;
        }
        if (!player.level().isClientSide() && (before != player.getAbilities().mayfly || wasFlying != player.getAbilities().flying)) {
            player.onUpdateAbilities();
        }
    }

    public static void forget(Player player) {
        FLYING_SERVER.remove(player.getUUID());
    }

    public static void forgetClient() {
        FLYING_CLIENT.clear();
    }
}
