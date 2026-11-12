package taintedmagic.armor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.item.GogglesItem;
import thaumcraft.item.gear.WarpingGear;

public class WarpedGogglesItem extends GogglesItem implements WarpingGear {
    public WarpedGogglesItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 1;
    }
}
