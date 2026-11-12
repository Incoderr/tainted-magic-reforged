package taintedmagic.research;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;
import thaumcraft.aspect.AspectList;
import thaumcraft.research.ResearchCategories;
import thaumcraft.research.ResearchItem;
import thaumcraft.research.ResearchPage;

public class TmLinkedResearchItem extends ResearchItem {
    private final String original;

    public TmLinkedResearchItem(String key, String original, String originalCategory, int column, int row, Identifier icon) {
        super(key, TmResearch.CATEGORY, new AspectList(), column, row, 1, icon);
        this.original = original;
        ResearchItem item = original();
        if (!originalCategory.equals(item.category())) {
            throw new IllegalStateException("Research " + original + " is in " + item.category() + ", not " + originalCategory);
        }
        List<String> siblings = new ArrayList<>(item.siblings());
        siblings.add(key);
        item.setSiblings(siblings.toArray(String[]::new));
        if (item.isSecondary()) {
            setSecondary();
        }
        setStub();
        setHidden();
    }

    public ResearchItem original() {
        ResearchItem item = ResearchCategories.getResearch(original);
        if (item == null) {
            throw new IllegalStateException("Research " + original + " for " + key() + " is not registered");
        }
        return item;
    }

    @Override
    public String nameKey() {
        return original().nameKey();
    }

    @Override
    public String textKey() {
        return original().textKey();
    }

    @Override
    public List<ResearchPage> pages() {
        return original().pages();
    }
}
