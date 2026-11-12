package taintedmagic.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import taintedmagic.plants.PlantsFeature;

public final class PlantsGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("plants")
        .add("berries_kill", PlantsGameTests::berriesKill)
        .add("funguar_gives_effects", PlantsGameTests::funguarGivesEffects)
        .add("bush_poisons", PlantsGameTests::bushPoisons);

    private PlantsGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static void berriesKill(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack stack = new ItemStack(PlantsFeature.NIGHTSHADE_BERRIES.get());
        stack.finishUsingItem(helper.getLevel(), player);
        helper.assertTrue(player.isDeadOrDying(), "player survived the berries");
        helper.assertTrue(player.getLastDamageSource() != null && player.getLastDamageSource().is(PlantsFeature.NIGHTSHADE), "wrong damage type");
        helper.succeed();
    }

    private static void funguarGivesEffects(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        new ItemStack(PlantsFeature.MAGIC_FUNGUAR.get()).finishUsingItem(helper.getLevel(), player);
        var regeneration = player.getEffect(MobEffects.REGENERATION);
        var speed = player.getEffect(MobEffects.MOVEMENT_SPEED);
        helper.assertTrue(regeneration != null && regeneration.getAmplifier() == 1, "regeneration II missing");
        helper.assertTrue(speed != null && speed.getAmplifier() == 2, "speed III missing");
        helper.succeed();
    }

    private static void bushPoisons(GameTestHelper helper) {
        BlockPos pos = new BlockPos(2, 1, 2);
        helper.setBlock(pos.below(), Blocks.DIRT);
        helper.setBlock(pos, PlantsFeature.NIGHTSHADE_BUSH.get());
        Pig pig = helper.spawn(EntityType.PIG, pos);
        helper.succeedWhen(() -> helper.assertTrue(pig.hasEffect(MobEffects.POISON), "pig is not poisoned"));
    }
}
