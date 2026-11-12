package taintedmagic.tools;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.item.gear.Repairable;
import thaumcraft.registry.ModItems;
import taintedmagic.materials.MaterialsFeature;

public class HollowDaggerItem extends Item implements Repairable {
    public HollowDaggerItem(Properties properties) {
        super(properties);
    }

    public static boolean fillPhial(Player player) {
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.is(ModItems.GLASS_PHIAL.get())) {
                stack.shrink(1);
                ItemStack blood = new ItemStack(MaterialsFeature.CRIMSON_BLOOD.get());
                inventory.placeItemBackInInventory(blood, net.minecraft.util.Prediction.SERVER_ONLY);
                return true;
            }
        }
        return false;
    }
}
