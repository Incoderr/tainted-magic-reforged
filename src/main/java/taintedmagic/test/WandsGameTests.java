package taintedmagic.test;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.IEventBus;
import thaumcraft.aspect.Aspect;
import thaumcraft.wand.WandCap;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.WandRod;
import thaumcraft.wand.Wands;
import taintedmagic.TaintedMagic;
import taintedmagic.wands.WandsFeature;
import taintedmagic.wands.WarpwoodRodUpdate;

public final class WandsGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("wands")
        .add("parts_match_tm", WandsGameTests::partsMatchTm)
        .add("refresh_period_follows_warp", WandsGameTests::refreshPeriodFollowsWarp)
        .add("no_refresh_without_warp", WandsGameTests::noRefreshWithoutWarp)
        .add("creative_wands_are_full", WandsGameTests::creativeWandsAreFull);

    private WandsGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static void cap(GameTestHelper helper, String tag, float discount, int cost, String item) {
        WandCap cap = WandRegistry.findCap(tag);
        helper.assertTrue(cap != null, "cap " + tag + " is missing");
        helper.assertValueEqual(cap.baseCostModifier(), discount, tag + " discount");
        helper.assertValueEqual(cap.craftCost(), cost, tag + " craft cost");
        helper.assertValueEqual(cap.item(), TaintedMagic.id(item), tag + " item");
        helper.assertValueEqual(cap.research(), "CAP_" + tag, tag + " research");
    }

    private static void partsMatchTm(GameTestHelper helper) {
        cap(helper, "shadowmetal", 0.65F, 12, "shadow_metal_wand_cap");
        cap(helper, "cloth", 0.97F, 2, "cloth_wand_cap");
        cap(helper, "crimsoncloth", 0.93F, 3, "crimson_cloth_wand_cap");
        cap(helper, "shadowcloth", 0.93F, 4, "shadow_cloth_wand_cap");
        WandRod rod = WandRegistry.findRod("warpwood");
        helper.assertTrue(rod != null && rod.capacity() == 250 && rod.craftCost() == 16 && rod.glowing() && !rod.staff() && !rod.runes(), "warpwood rod");
        helper.assertValueEqual(rod.research(), "ROD_warpwood", "rod research");
        WandRod staff = WandRegistry.findRod("warpwood_staff");
        helper.assertTrue(staff != null && staff.capacity() == 500 && staff.craftCost() == 20 && staff.glowing() && staff.staff() && staff.runes(), "warpwood staff");
        helper.assertValueEqual(staff.research(), "ROD_warpwood_staff", "staff research");
        helper.assertValueEqual(staff.textureTag(), "warpwood", "staff name tag");
        helper.succeed();
    }

    private static void refreshPeriodFollowsWarp(GameTestHelper helper) {
        helper.assertValueEqual(WarpwoodRodUpdate.period(1), 10000, "period at 1 warp");
        helper.assertValueEqual(WarpwoodRodUpdate.period(3), 3333, "period at 3 warp");
        helper.assertValueEqual(WarpwoodRodUpdate.period(20000), 1, "period below one tick");
        helper.succeed();
    }

    private static void noRefreshWithoutWarp(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack wand = Wands.create(WandsFeature.CAP_SHADOW_METAL, WandsFeature.ROD_WARPWOOD, false);
        for (int tick = 0; tick < 3; tick++) {
            WarpwoodRodUpdate.INSTANCE.onUpdate(wand, player);
        }
        helper.assertValueEqual(Wands.getVis(wand, Aspect.FIRE), 0, "vis without warp");
        helper.succeed();
    }

    private static void creativeWandsAreFull(GameTestHelper helper) {
        var wands = WandsFeature.creativeWands();
        helper.assertValueEqual(wands.size(), 3, "creative wands");
        helper.assertTrue(Wands.isStaff(wands.get(0)), "first is a staff");
        helper.assertTrue(!Wands.isStaff(wands.get(1)) && !Wands.isSceptre(wands.get(1)), "second is a wand");
        helper.assertTrue(Wands.isSceptre(wands.get(2)), "third is a sceptre");
        for (ItemStack wand : wands) {
            helper.assertValueEqual(Wands.getCap(wand), WandsFeature.CAP_SHADOW_METAL, "cap");
            helper.assertValueEqual(Wands.getVis(wand, Aspect.ORDER), Wands.getMaxVis(wand), "vis is full");
        }
        helper.succeed();
    }
}
