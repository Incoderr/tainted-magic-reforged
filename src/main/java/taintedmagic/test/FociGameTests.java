package taintedmagic.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.entity.monster.taint.TaintSwarm;
import thaumcraft.registry.ModEntities;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.Wands;
import thaumcraft.wand.focus.FocusItem;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.foci.DarkMatterFocus;
import taintedmagic.foci.FociEvents;
import taintedmagic.foci.FociFeature;
import taintedmagic.foci.entity.HomingShard;
import taintedmagic.platform.SwarmTargets;

public final class FociGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("foci")
        .add("upgrades_use_tm_ids", FociGameTests::upgradesUseTmIds)
        .add("dark_matter_costs", FociGameTests::darkMatterCosts)
        .add("diffusion_only_at_rank_five", FociGameTests::diffusionOnlyAtRankFive)
        .add("lumos_places_light", FociGameTests::lumosPlacesLight)
        .add("mage_mace_sets_damage", FociGameTests::mageMaceSetsDamage)
        .add("lumos_gives_night_vision", FociGameTests::lumosGivesNightVision)
        .add("homing_shard_hits_target", FociGameTests::homingShardHitsTarget)
        .add("swarm_target_is_set", FociGameTests::swarmTargetIsSet);

    private FociGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static ItemStack wand(ItemStack focus, boolean staff) {
        ItemStack wand = Wands.create(WandRegistry.CAP_GOLD, staff ? WandRegistry.STAFF_GREATWOOD : WandRegistry.ROD_GREATWOOD, false);
        Wands.setFocus(wand, focus);
        Wands.fill(wand);
        return wand;
    }

    private static void upgradesUseTmIds(GameTestHelper helper) {
        String[] names = {"sanity", "antibody", "corrosive", "persistent", "diffusion"};
        for (int index = 0; index < names.length; index++) {
            FocusUpgradeType type = FocusUpgradeType.byId(64 + index);
            helper.assertTrue(type != null && type.name().equals(names[index]), "upgrade " + (64 + index) + " is not " + names[index]);
        }
        helper.succeed();
    }

    private static int amount(AspectList list, Aspect aspect) {
        return list.getAmount(aspect);
    }

    private static void darkMatterCosts(GameTestHelper helper) {
        DarkMatterFocus focus = FociFeature.FOCUS_DARK_MATTER.get();
        ItemStack plain = new ItemStack(focus);
        helper.assertValueEqual(amount(focus.getVisCost(plain), Aspect.ENTROPY), 150, "entropy");
        ItemStack sanity = new ItemStack(focus);
        focus.applyUpgrade(sanity, FociFeature.SANITY, 3);
        helper.assertValueEqual(amount(focus.getVisCost(sanity), Aspect.ORDER), 50, "sanity order");
        ItemStack diffusion = new ItemStack(focus);
        focus.applyUpgrade(diffusion, FociFeature.CORROSIVE, 3);
        focus.applyUpgrade(diffusion, FociFeature.DIFFUSION, 5);
        AspectList cost = focus.getVisCost(diffusion);
        helper.assertValueEqual(amount(cost, Aspect.ENTROPY), 15, "diffusion entropy");
        helper.assertValueEqual(amount(cost, Aspect.FIRE), 10, "diffusion fire");
        helper.assertValueEqual(amount(cost, Aspect.WATER), 5, "diffusion corrosive water");
        helper.assertTrue(focus.isVisCostPerTick(diffusion) && focus.getActivationCooldown(diffusion) == -1, "diffusion is a channel");
        helper.succeed();
    }

    private static void diffusionOnlyAtRankFive(GameTestHelper helper) {
        FocusItem focus = FociFeature.FOCUS_DARK_MATTER.get();
        ItemStack stack = new ItemStack(focus);
        for (int rank = 1; rank <= 4; rank++) {
            for (FocusUpgradeType type : focus.getPossibleUpgradesByRank(stack, rank)) {
                helper.assertTrue(type != FociFeature.DIFFUSION, "diffusion offered at rank " + rank);
            }
        }
        helper.assertTrue(java.util.List.of(focus.getPossibleUpgradesByRank(stack, 5)).contains(FociFeature.DIFFUSION), "diffusion missing at rank 5");
        helper.succeed();
    }

    private static void lumosPlacesLight(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack wand = wand(new ItemStack(FociFeature.FOCUS_LUMOS.get()), false);
        player.setItemInHand(InteractionHand.MAIN_HAND, wand);
        helper.setBlock(new BlockPos(2, 1, 2), net.minecraft.world.level.block.Blocks.STONE);
        BlockPos clicked = helper.absolutePos(new BlockPos(2, 1, 2));
        FociFeature.FOCUS_LUMOS.get().onFocusRightClick(wand, helper.getLevel(), player, new BlockHitResult(Vec3.atCenterOf(clicked), Direction.UP, clicked, false));
        helper.assertBlockPresent(FociFeature.LUMOS.get(), new BlockPos(2, 2, 2));
        helper.assertTrue(Wands.getVis(wand, Aspect.FIRE) < Wands.getMaxVis(wand), "no vis was spent");
        helper.succeed();
    }

    private static double damage(ItemStack stack) {
        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        double total = 0.0;
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
                total += entry.modifier().amount();
            }
        }
        return total;
    }

    private static void mageMaceSetsDamage(GameTestHelper helper) {
        ItemStack wand = wand(new ItemStack(FociFeature.FOCUS_MAGE_MACE.get()), false);
        FociEvents.updateMace(wand);
        helper.assertValueEqual(damage(wand), 8.0, "wand mace damage");
        ItemStack staff = wand(new ItemStack(FociFeature.FOCUS_MAGE_MACE.get()), true);
        FociEvents.updateMace(staff);
        helper.assertValueEqual(damage(staff), 13.0, "staff mace damage");
        Wands.setFocus(wand, ItemStack.EMPTY);
        FociEvents.updateMace(wand);
        helper.assertTrue(!FociEvents.hasMaceModifier(wand), "mace modifier was not removed");
        helper.succeed();
    }

    private static void lumosGivesNightVision(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, wand(new ItemStack(FociFeature.FOCUS_LUMOS.get()), false));
        FociEvents.applyNightVision(player);
        helper.assertTrue(FociEvents.isLumosVision(player.getEffect(MobEffects.NIGHT_VISION)), "lumos night vision missing");
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        FociEvents.applyNightVision(player);
        helper.assertTrue(player.getEffect(MobEffects.NIGHT_VISION) == null, "lumos night vision kept");
        helper.succeed();
    }

    private static void homingShardHitsTarget(GameTestHelper helper) {
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(5, 1, 5));
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.snapTo(helper.absoluteVec(new Vec3(1.5, 1.0, 1.5)));
        HomingShard shard = HomingShard.create(helper.getLevel(), player, pig, 4, false);
        helper.getLevel().addFreshEntity(shard);
        float health = pig.getHealth();
        helper.succeedWhen(() -> helper.assertTrue(pig.getHealth() < health, "pig was not hit"));
    }

    private static void swarmTargetIsSet(GameTestHelper helper) {
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(1, 1, 1));
        TaintSwarm swarm = helper.spawn(ModEntities.TAINT_SWARM.get(), new BlockPos(4, 2, 4));
        SwarmTargets.set(swarm, pig);
        helper.assertTrue(SwarmTargets.get(swarm) == pig, "swarm target was not set");
        swarm.discard();
        helper.succeed();
    }
}
