package taintedmagic.armor;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.item.bauble.VisAmuletItem;
import thaumcraft.platform.Baubles;
import thaumcraft.wand.VisStorage;
import thaumcraft.wand.WandItem;
import thaumcraft.wand.WandManager;

public final class TmVis {
    private TmVis() {
    }

    public static boolean consumeFromInventory(Player player, AspectList cost, boolean doit) {
        for (int slot = 0; slot < Baubles.slotCount(); slot++) {
            ItemStack stack = Baubles.get(player, slot);
            if (stack.getItem() instanceof VisAmuletItem && consumeFromAmulet(stack, player, cost, doit)) {
                return true;
            }
        }
        for (int slot = 0; slot < Inventory.INVENTORY_SIZE; slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof WandItem wand && wand.consumeAllVis(stack, player, cost, doit, true)) {
                return true;
            }
        }
        return false;
    }

    private static boolean consumeFromAmulet(ItemStack stack, Player player, AspectList aspects, boolean doit) {
        AspectList cost = new AspectList();
        for (Aspect aspect : aspects.getAspects()) {
            cost.add(aspect, (int) (aspects.getAmount(aspect) * (1.0F - WandManager.getTotalVisDiscount(player, aspect))));
        }
        for (Aspect aspect : cost.getAspects()) {
            if (VisStorage.getVis(stack, aspect) < cost.getAmount(aspect)) {
                return false;
            }
        }
        if (doit && !player.level().isClientSide()) {
            for (Aspect aspect : cost.getAspects()) {
                VisStorage.storeVis(stack, aspect, VisStorage.getVis(stack, aspect) - cost.getAmount(aspect));
            }
        }
        return true;
    }
}
