package taintedmagic.test;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import thaumcraft.infusion.InfusionKeys;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.registry.ModItems;
import taintedmagic.materials.MaterialsFeature;
import taintedmagic.tools.HollowDaggerItem;
import taintedmagic.tools.KatanaItem;
import taintedmagic.tools.ShadowMetalHoeItem;
import taintedmagic.tools.ThaumicDisassemblerItem;
import taintedmagic.tools.ToolsFeature;

public final class ToolsGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("tools")
        .add("stats_match_tm", ToolsGameTests::statsMatchTm)
        .add("hoe_toggles_farmland", ToolsGameTests::hoeTogglesFarmland)
        .add("dagger_fills_phial", ToolsGameTests::daggerFillsPhial)
        .add("katana_inscription_and_cooldown", ToolsGameTests::katanaInscriptionAndCooldown)
        .add("disassembler_modes_and_charge", ToolsGameTests::disassemblerModesAndCharge);

    private ToolsGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static double damage(Item item) {
        double total = 1.0;
        for (var entry : new ItemStack(item).getAttributeModifiers().modifiers()) {
            if (entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
                total += entry.modifier().amount();
            }
        }
        return total;
    }

    private static void stat(GameTestHelper helper, Item item, int durability, double attack) {
        helper.assertValueEqual(new ItemStack(item).getMaxDamage(), durability, item + " durability");
        helper.assertTrue(Math.abs(damage(item) - attack) < 1.0E-4, item + " damage " + damage(item));
    }

    private static void statsMatchTm(GameTestHelper helper) {
        stat(helper, ToolsFeature.SHADOW_METAL_SWORD.get(), 2500, 11.0);
        stat(helper, ToolsFeature.SHADOW_METAL_PICKAXE.get(), 2500, 9.0);
        stat(helper, ToolsFeature.HOLLOW_DAGGER.get(), 200, 4.5);
        stat(helper, ToolsFeature.PRIMAL_BLADE.get(), 500, 21.75);
        stat(helper, ToolsFeature.THAUMIUM_KATANA.get(), 0, 14.25);
        stat(helper, ToolsFeature.SHADOW_METAL_KATANA.get(), 0, 20.75);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        int[] warp = {0, 3, 7};
        Item[] katanas = {ToolsFeature.THAUMIUM_KATANA.get(), ToolsFeature.VOID_METAL_KATANA.get(), ToolsFeature.SHADOW_METAL_KATANA.get()};
        for (int index = 0; index < katanas.length; index++) {
            helper.assertValueEqual(((WarpingGear) katanas[index]).getWarp(ItemStack.EMPTY, player), warp[index], katanas[index] + " warp");
        }
        helper.assertValueEqual(((WarpingGear) ToolsFeature.PRIMAL_BLADE.get()).getWarp(ItemStack.EMPTY, player), 5, "primal warp");
        helper.succeed();
    }

    private static void hoeTogglesFarmland(GameTestHelper helper) {
        helper.assertTrue(ShadowMetalHoeItem.toggled(Blocks.DIRT.defaultBlockState()) == Blocks.FARMLAND, "dirt");
        helper.assertTrue(ShadowMetalHoeItem.toggled(Blocks.FARMLAND.defaultBlockState()) == Blocks.DIRT, "farmland");
        helper.assertTrue(ShadowMetalHoeItem.toggled(Blocks.STONE.defaultBlockState()) == null, "stone");
        helper.succeed();
    }

    private static void daggerFillsPhial(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.assertTrue(!HollowDaggerItem.fillPhial(player), "filled without a phial");
        player.getInventory().setItem(3, new ItemStack(ModItems.GLASS_PHIAL.get(), 2));
        helper.assertTrue(HollowDaggerItem.fillPhial(player), "phial not filled");
        helper.assertValueEqual(player.getInventory().getItem(3).getCount(), 1, "phials left");
        helper.assertValueEqual(player.getInventory().countItem(MaterialsFeature.CRIMSON_BLOOD.get()), 1, "crimson blood");
        helper.succeed();
    }

    private static void katanaInscriptionAndCooldown(GameTestHelper helper) {
        ItemStack katana = new ItemStack(ToolsFeature.VOID_METAL_KATANA.get());
        helper.assertValueEqual(KatanaItem.inscription(katana), -1, "blank inscription");
        InfusionKeys.TAGS.set(katana, Map.of("inscription", "2"));
        helper.assertValueEqual(KatanaItem.inscription(katana), 2, "inscription");
        long now = helper.getLevel().getGameTime();
        ToolsFeature.KATANA_COOLDOWN.set(katana, now + 500);
        helper.assertValueEqual(KatanaItem.cooldown(katana, helper.getLevel()), (long) KatanaItem.COOLDOWN_TICKS, "clamped cooldown");
        ToolsFeature.KATANA_COOLDOWN.set(katana, now - 5);
        helper.assertValueEqual(KatanaItem.cooldown(katana, helper.getLevel()), 0L, "expired cooldown");
        helper.succeed();
    }

    private static void disassemblerModesAndCharge(GameTestHelper helper) {
        ItemStack tool = new ItemStack(ToolsFeature.THAUMIC_DISASSEMBLER.get());
        var stone = Blocks.STONE.defaultBlockState();
        ThaumicDisassemblerItem item = ToolsFeature.THAUMIC_DISASSEMBLER.get();
        helper.assertValueEqual(item.getDestroySpeed(tool, stone), 1.0F, "empty speed");
        helper.assertValueEqual(ThaumicDisassemblerItem.attackDamage(tool), 4.0F, "empty damage");
        ThaumicDisassemblerItem.setCharge(tool, 1000);
        helper.assertValueEqual(item.getDestroySpeed(tool, stone), 20.0F, "normal speed");
        ToolsFeature.DISASSEMBLER_MODE.set(tool, 2);
        helper.assertValueEqual(item.getDestroySpeed(tool, stone), 128.0F, "fast speed");
        helper.assertValueEqual(ThaumicDisassemblerItem.attackDamage(tool), 20.0F, "charged damage");
        helper.assertTrue(!item.isCorrectToolForDrops(tool, Blocks.BEDROCK.defaultBlockState()) && item.isCorrectToolForDrops(tool, Blocks.OBSIDIAN.defaultBlockState()), "harvest");
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.setBlock(new BlockPos(1, 1, 1), Blocks.DIRT);
        BlockPos dirt = helper.absolutePos(new BlockPos(1, 1, 1));
        helper.assertTrue(ThaumicDisassemblerItem.till(tool, player, helper.getLevel(), dirt, Direction.UP), "did not till");
        helper.assertBlockPresent(Blocks.FARMLAND, new BlockPos(1, 1, 1));
        helper.assertValueEqual(ThaumicDisassemblerItem.charge(tool), 1000 - ThaumicDisassemblerItem.ENTROPY_USAGE_HOE, "hoe cost");
        helper.succeed();
    }
}
