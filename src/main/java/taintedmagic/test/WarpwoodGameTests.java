package taintedmagic.test;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import thaumcraft.registry.ModBlocks;
import taintedmagic.warpwood.WarpwoodFeature;
import taintedmagic.warpwood.WarpwoodTreeGenerator;

public final class WarpwoodGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("warpwood")
        .add("fertilizer_turns_silverwood_sapling", WarpwoodGameTests::fertilizerTurnsSilverwoodSapling)
        .add("fertilizer_ignores_greatwood_sapling", WarpwoodGameTests::fertilizerIgnoresGreatwoodSapling)
        .add("knot_is_hard", WarpwoodGameTests::knotIsHard);
    private static final TmGameTestSuite TREE = new TmGameTestSuite("warpwood_tree")
        .structure("test/tall")
        .add("grows_on_dirt", WarpwoodGameTests::treeGrowsOnDirt);

    private WarpwoodGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
        TREE.register(modEventBus);
    }

    private static ItemStack fertilize(GameTestHelper helper, BlockPos pos) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack stack = new ItemStack(WarpwoodFeature.WARP_FERTILIZER.get(), 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        BlockPos absolute = helper.absolutePos(pos);
        stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false)));
        return stack;
    }

    private static void fertilizerTurnsSilverwoodSapling(GameTestHelper helper) {
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos.below(), Blocks.DIRT);
        helper.setBlock(pos, ModBlocks.SILVERWOOD_SAPLING.get());
        ItemStack stack = fertilize(helper, pos);
        helper.assertBlockPresent(WarpwoodFeature.WARPWOOD_SAPLING.get(), pos);
        helper.assertValueEqual(stack.getCount(), 1, "fertilizer left");
        helper.succeed();
    }

    private static void fertilizerIgnoresGreatwoodSapling(GameTestHelper helper) {
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos.below(), Blocks.DIRT);
        helper.setBlock(pos, ModBlocks.GREATWOOD_SAPLING.get());
        ItemStack stack = fertilize(helper, pos);
        helper.assertBlockPresent(ModBlocks.GREATWOOD_SAPLING.get(), pos);
        helper.assertValueEqual(stack.getCount(), 2, "fertilizer left");
        helper.succeed();
    }

    private static void treeGrowsOnDirt(GameTestHelper helper) {
        BlockPos pos = new BlockPos(5, 1, 5);
        helper.setBlock(pos.below(), Blocks.DIRT);
        boolean grown = new WarpwoodTreeGenerator(true, 7, 5).generate(helper.getLevel(), new Random(42L), helper.absolutePos(pos));
        helper.assertTrue(grown, "tree did not grow");
        helper.assertTrue(
            helper.getBlockState(pos).is(WarpwoodFeature.WARPWOOD_LOG.get()) || helper.getBlockState(pos).is(WarpwoodFeature.WARPWOOD_KNOT.get()),
            "trunk is missing"
        );
        helper.assertBlockPresent(WarpwoodFeature.WARPWOOD_LOG.get(), pos.east(2));
        helper.assertBlockPresent(WarpwoodFeature.WARPWOOD_LOG.get(), pos.below().west(2));
        helper.succeed();
    }

    private static void knotIsHard(GameTestHelper helper) {
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos, WarpwoodFeature.WARPWOOD_KNOT.get());
        float speed = helper.getBlockState(pos).getDestroySpeed(helper.getLevel(), helper.absolutePos(pos));
        helper.assertValueEqual(speed, 25.0F, "knot hardness");
        helper.succeed();
    }
}
