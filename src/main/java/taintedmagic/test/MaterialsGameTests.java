package taintedmagic.test;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.saveddata.WeatherData;
import net.neoforged.bus.api.IEventBus;
import taintedmagic.materials.MaterialsFeature;
import taintedmagic.materials.SalisItem;
import taintedmagic.registry.TmRarity;

public final class MaterialsGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("materials")
        .add("rarities_match_tm", MaterialsGameTests::raritiesMatchTm)
        .add("salis_tempestas_toggles_weather", MaterialsGameTests::salisTempestasTogglesWeather)
        .add("salis_aevum_skips_to_day_or_night", MaterialsGameTests::salisAevumSkipsToDayOrNight);

    private MaterialsGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static Rarity rarity(net.minecraft.world.item.Item item) {
        return new ItemStack(item).getOrDefault(DataComponents.RARITY, Rarity.COMMON);
    }

    private static void raritiesMatchTm(GameTestHelper helper) {
        helper.assertValueEqual(TmRarity.creation().color(), ChatFormatting.GOLD, "creation rarity colour");
        helper.assertValueEqual(rarity(MaterialsFeature.SHADOW_METAL_INGOT.get()), Rarity.UNCOMMON, "shadow metal ingot");
        helper.assertValueEqual(rarity(MaterialsFeature.WARPED_SHARD.get()), Rarity.COMMON, "warped shard");
        helper.assertValueEqual(rarity(MaterialsFeature.CREATION_SHARD.get()), TmRarity.creation(), "shard of creation");
        helper.assertValueEqual(rarity(MaterialsFeature.PRIMORDIAL_NODULE.get()), Rarity.EPIC, "primordial nodule");
        helper.assertValueEqual(rarity(MaterialsFeature.CREATION_FRAGMENT.get()), TmRarity.creation(), "fragment of creation");
        helper.assertValueEqual(rarity(MaterialsFeature.VOID_BLOOD.get()), Rarity.RARE, "void blood");
        helper.assertValueEqual(rarity(MaterialsFeature.SALIS_AEVUM.get()), TmRarity.creation(), "salis aevum");
        helper.succeed();
    }

    private static void salisTempestasTogglesWeather(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        WeatherData weather = level.getWeatherData();
        boolean before = weather.isRaining();
        SalisItem.toggleWeather(level);
        helper.assertValueEqual(weather.isRaining(), !before, "rain after the first toggle");
        helper.assertValueEqual(weather.getRainTime(), before ? 24000 : 0, "rain time after the first toggle");
        SalisItem.toggleWeather(level);
        helper.assertValueEqual(weather.isRaining(), before, "rain after the second toggle");
        helper.succeed();
    }

    private static void salisAevumSkipsToDayOrNight(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Holder<WorldClock> clock = level.dimensionTypeRegistration().value().defaultClock().orElseThrow();
        var clocks = level.getServer().clockManager();
        long before = clocks.getInstance(clock).totalTicks();
        SalisItem.skipToDayOrNight(level);
        long after = clocks.getInstance(clock).totalTicks();
        clocks.setTotalTicks(clock, before);
        helper.assertTrue(after > before, "time did not move forward");
        helper.assertTrue(after % 24000 == 14000 || after % 24000 == 0, "time stopped at " + after % 24000);
        helper.succeed();
    }
}
