package taintedmagic.research;

import java.util.function.BooleanSupplier;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.crafting.TcStack;
import thaumcraft.research.ResearchCategories;
import thaumcraft.research.ResearchPage;
import taintedmagic.StartupConfig;
import taintedmagic.TaintedMagic;

public final class TmResearch {
    public static final String CATEGORY = "TAINTEDMAGIC";

    private static BooleanSupplier tags = () -> true;

    private TmResearch() {
    }

    public static void init() {
        ResearchCategories.registerCategory(CATEGORY, TaintedMagic.id("textures/misc/tab_tm.png"), background());
    }

    static ResourceLocation background() {
        return StartupConfig.customResearchTabBack()
            ? TaintedMagic.id("textures/gui/gui_tm_researchback.png")
            : Thaumcraft.id("textures/gui/gui_researchback.png");
    }

    public static void setTags(BooleanSupplier supplier) {
        tags = supplier;
    }

    static boolean tags() {
        return tags.getAsBoolean();
    }

    public static AspectList primals(int amount) {
        return new AspectList()
            .add(Aspect.FIRE, amount)
            .add(Aspect.WATER, amount)
            .add(Aspect.EARTH, amount)
            .add(Aspect.AIR, amount)
            .add(Aspect.ORDER, amount)
            .add(Aspect.ENTROPY, amount);
    }

    public static TmResearchItem item(String key, AspectList tags, int column, int row, TcStack icon, int complexity, int warp) {
        return new TmResearchItem(key, tags, column, row, complexity, icon).warp(warp);
    }

    public static TmResearchItem item(String key, AspectList tags, int column, int row, ResourceLocation icon, int complexity, int warp) {
        return new TmResearchItem(key, tags, column, row, complexity, icon).warp(warp);
    }

    public static TmLinkedResearchItem link(String key, String original, String originalCategory, int column, int row, ResourceLocation icon) {
        return new TmLinkedResearchItem(key, original, originalCategory, column, row, icon);
    }

    public static ResearchPage text(String page) {
        return ResearchPage.text(page);
    }

    public static ResearchPage recipe(String... recipes) {
        return ResearchPage.crafting(recipes);
    }

    public static ResearchPage arcane(String... recipes) {
        return ResearchPage.arcane(recipes);
    }

    public static ResearchPage infusion(String... recipes) {
        return ResearchPage.infusion(recipes);
    }

    public static ResearchPage crucible(String... recipes) {
        return ResearchPage.crucible(recipes);
    }
}
