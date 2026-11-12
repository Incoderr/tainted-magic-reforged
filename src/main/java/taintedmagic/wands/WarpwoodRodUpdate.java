package taintedmagic.wands;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.aspect.Aspect;
import thaumcraft.registry.ModEffects;
import thaumcraft.research.Warp;
import thaumcraft.research.WarpType;
import thaumcraft.wand.RodUpdate;
import thaumcraft.wand.Wands;
import taintedmagic.Config;

public final class WarpwoodRodUpdate implements RodUpdate {
    public static final WarpwoodRodUpdate INSTANCE = new WarpwoodRodUpdate();

    private WarpwoodRodUpdate() {
    }

    public static int period(int permanentWarp) {
        float period = (float) Config.warpWandRefreshBase() / permanentWarp;
        return period < 1.0F ? 1 : Math.round(period);
    }

    @Override
    public void onUpdate(ItemStack stack, Player player) {
        if (player.hasEffect(ModEffects.WARP_WARD)) {
            return;
        }
        int permanent = Warp.get(player, WarpType.PERMANENT);
        if (permanent == 0) {
            return;
        }
        if (player.tickCount % period(permanent) == 0) {
            for (Aspect primal : Aspect.getPrimalAspects()) {
                Wands.addVis(stack, primal, 1, true);
            }
        }
    }
}
