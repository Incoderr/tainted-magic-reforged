package taintedmagic.foci;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.aspect.AspectList;
import thaumcraft.wand.WandItem;
import thaumcraft.wand.Wands;
import thaumcraft.wand.focus.FocusItem;
import thaumcraft.wand.focus.FocusUpgradeType;

public abstract class TmFocus extends FocusItem {
    protected TmFocus(Properties properties) {
        super(properties);
    }

    protected static ItemStack focusOf(ItemStack wand) {
        return Wands.getFocusItem(wand);
    }

    protected boolean consume(ItemStack wand, Player player, boolean doit) {
        AspectList cost = getVisCost(focusOf(wand));
        return wand.getItem() instanceof WandItem item && cost != null && item.consumeAllVis(wand, player, cost, doit, false);
    }

    protected static FocusUpgradeType[] upgrades(FocusUpgradeType... types) {
        return types;
    }
}
