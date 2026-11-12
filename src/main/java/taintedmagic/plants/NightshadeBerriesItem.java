package taintedmagic.plants;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NightshadeBerriesItem extends Item {
    public NightshadeBerriesItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (level instanceof ServerLevel server && entity instanceof Player player) {
            player.hurtServer(server, server.damageSources().source(PlantsFeature.NIGHTSHADE), Short.MAX_VALUE);
        }
        return result;
    }
}
