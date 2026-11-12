package taintedmagic.test;

import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.bus.api.IEventBus;
import thaumcraft.Thaumcraft;
import thaumcraft.research.ResearchCategories;
import thaumcraft.research.ResearchCategory;
import taintedmagic.StartupConfig;
import taintedmagic.TaintedMagic;
import taintedmagic.research.TmResearch;

public final class CoreGameTests {
    private static final TmGameTestSuite SUITE = new TmGameTestSuite("core")
        .add("research_category_registered", CoreGameTests::researchCategoryRegistered);

    private CoreGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static void researchCategoryRegistered(GameTestHelper helper) {
        ResearchCategory category = ResearchCategories.get(TmResearch.CATEGORY);
        helper.assertTrue(category != null, "category " + TmResearch.CATEGORY + " is missing");
        helper.assertValueEqual(category.icon(), TaintedMagic.id("textures/misc/tab_tm.png"), "category icon");
        helper.assertValueEqual(
            category.background(),
            StartupConfig.customResearchTabBack() ? TaintedMagic.id("textures/gui/gui_tm_researchback.png") : Thaumcraft.id("textures/gui/gui_researchback.png"),
            "category background"
        );
        helper.succeed();
    }
}
