package taintedmagic.tools;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import taintedmagic.TaintedMagic;

@EventBusSubscriber(modid = TaintedMagic.MODID)
public final class ToolsEvents {
    private ToolsEvents() {
    }

    @SubscribeEvent
    static void incoming(LivingIncomingDamageEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player && event.getSource().is(DamageTypes.PLAYER_ATTACK)) {
            ItemStack held = player.getMainHandItem();
            if (held.getItem() instanceof ThaumicDisassemblerItem) {
                event.setAmount(ThaumicDisassemblerItem.attackDamage(held));
                if (ThaumicDisassemblerItem.charge(held) > 0) {
                    ThaumicDisassemblerItem.consume(held, ThaumicDisassemblerItem.ENTROPY_USAGE_HIT);
                }
            }
        }
    }

    public static ItemStack hipKatana(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof KatanaItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    static void playerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        ItemStack found = hipKatana(player);
        ItemStack current = player.getData(taintedmagic.platform.ModAttachments.HIP_KATANA);
        if (found.getItem() != current.getItem() || KatanaItem.inscription(found) != KatanaItem.inscription(current)) {
            player.setData(taintedmagic.platform.ModAttachments.HIP_KATANA, found.copyWithCount(1));
        }
    }

    @SubscribeEvent
    static void hurt(LivingDamageEvent.Post event) {
        if (!event.getEntity().level().isClientSide()
            && event.getSource().getEntity() instanceof Player player
            && event.getSource().getDirectEntity() == player
            && player.getMainHandItem().getItem() instanceof HollowDaggerItem) {
            HollowDaggerItem.fillPhial(player);
        }
    }
}
