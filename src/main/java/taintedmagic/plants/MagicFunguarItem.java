package taintedmagic.plants;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.aspect.Aspect;
import thaumcraft.research.ResearchManager;

public class MagicFunguarItem extends Item {
    public MagicFunguarItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide() && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
            List<Aspect> primals = Aspect.getPrimalAspects();
            Aspect aspect = primals.get(level.getRandom().nextInt(primals.size()));
            if (player instanceof ServerPlayer serverPlayer) {
                ResearchManager.addAspectPool(serverPlayer, aspect, 1);
            }
        }
        return result;
    }
}
