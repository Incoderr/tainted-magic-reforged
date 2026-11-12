package taintedmagic.research;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;
import thaumcraft.aspect.AspectList;
import thaumcraft.crafting.TcStack;
import thaumcraft.research.ResearchItem;
import thaumcraft.research.ResearchPage;

public class TmResearchItem extends ResearchItem {
    public static final String TAG_PREFIX = "taintedmagic.research_tag.";
    private static final String PAGE_PREFIX = "tc.research_page.";

    public TmResearchItem(String key, AspectList tags, int column, int row, int complexity, TcStack icon) {
        super(key, TmResearch.CATEGORY, tags, column, row, complexity, icon);
    }

    public TmResearchItem(String key, AspectList tags, int column, int row, int complexity, Identifier icon) {
        super(key, TmResearch.CATEGORY, tags, column, row, complexity, icon);
    }

    TmResearchItem warp(int warp) {
        if (warp > 0) {
            setWarp(warp);
        }
        return this;
    }

    @Override
    public String textKey() {
        return TmResearch.tags() ? TAG_PREFIX + key() : super.textKey();
    }

    @Override
    public ResearchItem setPages(ResearchPage... pages) {
        ResearchPage[] converted = new ResearchPage[pages.length];
        for (int index = 0; index < pages.length; index++) {
            ResearchPage page = pages[index];
            if (page instanceof ResearchPage.Text text && !text.text().contains(".")) {
                page = ResearchPage.text(PAGE_PREFIX + key() + "." + text.text());
            }
            converted[index] = page;
        }
        return super.setPages(converted);
    }

    @Override
    public ResearchItem registerResearchItem() {
        if (pages().stream().anyMatch(page -> page instanceof ResearchPage.Infusion) && !parentsHidden().contains("INFUSION")) {
            List<String> hidden = new ArrayList<>(parentsHidden());
            hidden.add("INFUSION");
            setParentsHidden(hidden.toArray(String[]::new));
        }
        return super.registerResearchItem();
    }
}
