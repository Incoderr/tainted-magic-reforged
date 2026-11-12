package taintedmagic.test;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.platform.Baubles;
import thaumcraft.wand.VisDiscountGear;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.Wands;
import taintedmagic.armor.ArmorEvents;
import taintedmagic.armor.ArmorFeature;
import taintedmagic.armor.FlyteCharmItem;
import taintedmagic.armor.GateKeyItem;
import taintedmagic.armor.VoidBlood;
import taintedmagic.armor.VoidwalkerSashItem;
import taintedmagic.foci.FociEvents;
import taintedmagic.materials.MaterialsFeature;

public final class ArmorGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("armor")
        .add("stats_match_tm", ArmorGameTests::statsMatchTm)
        .add("warp_and_discounts", ArmorGameTests::warpAndDiscounts)
        .add("void_blood_touches_armor", ArmorGameTests::voidBloodTouchesArmor)
        .add("voidtouched_repairs", ArmorGameTests::voidtouchedRepairs)
        .add("sash_speed_defaults_on", ArmorGameTests::sashSpeedDefaultsOn)
        .add("gate_key_needs_space", ArmorGameTests::gateKeyNeedsSpace)
        .add("flyte_charm_allows_flight", ArmorGameTests::flyteCharmAllowsFlight)
        .add("lumos_ring_gives_night_vision", ArmorGameTests::lumosRingGivesNightVision);

    private ArmorGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static double armor(Item item) {
        double total = 0.0;
        for (var entry : new ItemStack(item).getAttributeModifiers().modifiers()) {
            if (entry.attribute().is(Attributes.ARMOR)) {
                total += entry.modifier().amount();
            }
        }
        return total;
    }

    private static void stat(GameTestHelper helper, Item item, int durability, double defense) {
        helper.assertValueEqual(new ItemStack(item).getMaxDamage(), durability, item + " durability");
        helper.assertValueEqual(armor(item), defense, item + " armor");
    }

    private static void statsMatchTm(GameTestHelper helper) {
        stat(helper, ArmorFeature.WARPED_GOGGLES.get(), 5500, 4.0);
        stat(helper, ArmorFeature.VOID_METAL_GOGGLES.get(), 275, 1.0);
        stat(helper, ArmorFeature.VOID_FORTRESS_HELMET.get(), 3300, 4.0);
        stat(helper, ArmorFeature.VOID_FORTRESS_CHESTPLATE.get(), 4800, 8.0);
        stat(helper, ArmorFeature.VOID_FORTRESS_LEGGINGS.get(), 4500, 6.0);
        stat(helper, ArmorFeature.SHADOW_FORTRESS_HELMET.get(), 33000, 4.0);
        stat(helper, ArmorFeature.SHADOW_FORTRESS_CHESTPLATE.get(), 48000, 10.0);
        stat(helper, ArmorFeature.SHADOW_FORTRESS_LEGGINGS.get(), 45000, 6.0);
        stat(helper, ArmorFeature.VOIDWALKER_BOOTS.get(), 3900, 4.0);
        helper.succeed();
    }

    private static void warpAndDiscounts(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        int[][] expected = {{1, 5}, {5, 12}, {3, 5}, {5, 5}, {5, 5}};
        Item[] items = {
            ArmorFeature.WARPED_GOGGLES.get(), ArmorFeature.VOID_METAL_GOGGLES.get(), ArmorFeature.VOID_FORTRESS_HELMET.get(),
            ArmorFeature.SHADOW_FORTRESS_HELMET.get(), ArmorFeature.VOIDWALKER_BOOTS.get()
        };
        for (int index = 0; index < items.length; index++) {
            ItemStack stack = new ItemStack(items[index]);
            helper.assertValueEqual(((WarpingGear) items[index]).getWarp(stack, player), expected[index][0], items[index] + " warp");
            helper.assertValueEqual(((VisDiscountGear) items[index]).getVisDiscount(stack, player, null), expected[index][1], items[index] + " discount");
        }
        helper.assertValueEqual(((WarpingGear) ArmorFeature.VOIDWALKER_SASH.get()).getWarp(ItemStack.EMPTY, player), 2, "sash warp");
        helper.assertValueEqual(((WarpingGear) ArmorFeature.FLYTE_CHARM.get()).getWarp(ItemStack.EMPTY, player), 5, "charm warp");
        helper.assertValueEqual(((WarpingGear) ArmorFeature.GATE_KEY.get()).getWarp(ItemStack.EMPTY, player), 3, "key warp");
        helper.succeed();
    }

    private static void voidBloodTouchesArmor(GameTestHelper helper) {
        ItemStack blood = new ItemStack(MaterialsFeature.VOID_BLOOD.get());
        CraftingInput input = CraftingInput.of(2, 1, List.of(blood, new ItemStack(Items.IRON_HELMET)));
        helper.assertTrue(VoidBlood.INSTANCE.matches(input, helper.getLevel()), "void blood recipe did not match");
        ItemStack result = VoidBlood.INSTANCE.assemble(input);
        helper.assertTrue(result.is(Items.IRON_HELMET) && VoidBlood.isVoidtouched(result), "result is not voidtouched");
        CraftingInput again = CraftingInput.of(2, 1, List.of(blood, result));
        helper.assertTrue(VoidBlood.INSTANCE.assemble(again).isEmpty(), "voidtouched armor was touched again");
        CraftingInput sword = CraftingInput.of(2, 1, List.of(blood, new ItemStack(Items.IRON_SWORD)));
        helper.assertTrue(!VoidBlood.INSTANCE.matches(sword, helper.getLevel()), "a sword matched");
        helper.succeed();
    }

    private static void voidtouchedRepairs(GameTestHelper helper) {
        ItemStack stack = new ItemStack(Items.IRON_CHESTPLATE);
        VoidBlood.VOIDTOUCHED.set(stack, true);
        stack.setDamageValue(10);
        ArmorEvents.repairVoidtouched(stack);
        helper.assertValueEqual(stack.getDamageValue(), 9, "voidtouched repair");
        helper.succeed();
    }

    private static void sashSpeedDefaultsOn(GameTestHelper helper) {
        ItemStack sash = new ItemStack(ArmorFeature.VOIDWALKER_SASH.get());
        helper.assertTrue(VoidwalkerSashItem.isSpeedEnabled(sash), "speed is off by default");
        ArmorFeature.SASH_SPEED.set(sash, false);
        helper.assertTrue(!VoidwalkerSashItem.isSpeedEnabled(sash), "speed did not turn off");
        helper.succeed();
    }

    private static void gateKeyNeedsSpace(GameTestHelper helper) {
        BlockPos target = helper.absolutePos(new BlockPos(3, 1, 3));
        String dimension = helper.getLevel().dimension().identifier().toString();
        GateKeyItem.Binding binding = new GateKeyItem.Binding(target.getX(), target.getY(), target.getZ(), dimension, 10);
        helper.assertTrue(GateKeyItem.canTeleport(helper.getLevel(), binding), "free space rejected");
        helper.setBlock(new BlockPos(3, 2, 3), Blocks.STONE);
        helper.assertTrue(!GateKeyItem.canTeleport(helper.getLevel(), binding), "blocked space accepted");
        GateKeyItem.Binding nether = new GateKeyItem.Binding(target.getX(), target.getY(), target.getZ(), "minecraft:the_nether", 10);
        helper.assertTrue(!GateKeyItem.canTeleport(helper.getLevel(), nether), "other dimension accepted");
        helper.succeed();
    }

    private static void flyteCharmAllowsFlight(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(ArmorFeature.FLYTE_CHARM.get()));
        ItemStack wand = Wands.create(WandRegistry.CAP_GOLD, WandRegistry.ROD_GREATWOOD, false);
        Wands.fill(wand);
        player.getInventory().setItem(1, wand);
        FlyteCharmItem.tick(player);
        helper.assertTrue(player.getAbilities().mayfly, "charm did not allow flight");
        player.getInventory().setItem(0, ItemStack.EMPTY);
        FlyteCharmItem.tick(player);
        helper.assertTrue(!player.getAbilities().mayfly, "flight stayed without the charm");
        FlyteCharmItem.forget(player);
        helper.succeed();
    }

    private static void lumosRingGivesNightVision(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Baubles.set(player, 1, new ItemStack(ArmorFeature.LUMOS_RING.get()));
        FociEvents.applyNightVision(player);
        helper.assertTrue(FociEvents.isLumosVision(player.getEffect(MobEffects.NIGHT_VISION)), "ring gave no night vision");
        helper.succeed();
    }
}
