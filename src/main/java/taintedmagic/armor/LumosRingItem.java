package taintedmagic.armor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.BaubleType;
import thaumcraft.platform.Baubles;

public class LumosRingItem extends Item implements Bauble {
    public LumosRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.RING;
    }

    public static boolean worn(Player player) {
        return Baubles.get(player, 1).getItem() instanceof LumosRingItem || Baubles.get(player, 2).getItem() instanceof LumosRingItem;
    }
}
