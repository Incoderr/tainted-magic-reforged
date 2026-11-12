package taintedmagic.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.crafting.CraftingRecipeRef;
import thaumcraft.crafting.CrucibleRecipe;
import thaumcraft.crafting.InfusionRecipe;
import thaumcraft.crafting.ShapedArcaneRecipe;
import thaumcraft.crafting.ShapelessArcaneRecipe;
import thaumcraft.crafting.TcIngredient;
import thaumcraft.crafting.TcRecipe;
import thaumcraft.crafting.TcRecipes;
import thaumcraft.crafting.TcStack;
import thaumcraft.research.ResearchCategories;
import thaumcraft.research.ResearchItem;
import thaumcraft.research.ResearchPage;
import taintedmagic.research.TmLinkedResearchItem;
import taintedmagic.research.TmResearch;

public final class ResearchTableGameTests {
    private static final String RESEARCH_TABLE = "tools/tm_research.json";
    private static final String RECIPE_TABLE = "tools/tm_recipes.json";
    private static final String PAGE_PREFIX = "tc.research_page.";

    private static final TmGameTestSuite SUITE = new TmGameTestSuite("research_table")
        .add("research_matches_tm_table", ResearchTableGameTests::researchMatchesTable)
        .add("research_pages_resolve", ResearchTableGameTests::researchPagesResolve)
        .add("parents_exist", ResearchTableGameTests::parentsExist)
        .add("linked_research_uses_original", ResearchTableGameTests::linkedResearchUsesOriginal)
        .add("recipes_match_tm_table", ResearchTableGameTests::recipesMatchTable);

    private ResearchTableGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        SUITE.register(modEventBus);
    }

    private static void researchMatchesTable(GameTestHelper helper) {
        JsonArray table = load(RESEARCH_TABLE);
        List<String> problems = new ArrayList<>();
        Set<String> expectedKeys = new HashSet<>();
        for (JsonElement element : table) {
            JsonObject expected = element.getAsJsonObject();
            String key = expected.get("key").getAsString();
            expectedKeys.add(key);
            ResearchItem actual = ResearchCategories.getResearch(key);
            if (actual == null) {
                problems.add(key + ": TM research is not registered");
                continue;
            }
            compare(expected, actual, problems);
        }
        for (ResearchItem research : ResearchCategories.allResearch()) {
            if (TmResearch.CATEGORY.equals(research.category()) && !expectedKeys.contains(research.key())) {
                problems.add(research.key() + ": registered but absent from the TM table");
            }
        }
        helper.assertTrue(problems.isEmpty(), problems.size() + " research mismatches: " + String.join("; ", problems));
        helper.succeed();
    }

    private static void researchPagesResolve(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        for (ResearchItem research : ResearchCategories.allResearch()) {
            if (!TmResearch.CATEGORY.equals(research.category()) || research instanceof TmLinkedResearchItem) {
                continue;
            }
            for (ResearchPage page : research.pages()) {
                List<String> keys = switch (page) {
                    case ResearchPage.Crafting crafting -> crafting.recipes();
                    case ResearchPage.Arcane arcane -> arcane.recipes();
                    case ResearchPage.Crucible crucible -> crucible.recipes();
                    case ResearchPage.Infusion infusion -> infusion.recipes();
                    default -> List.of();
                };
                for (String key : keys) {
                    TcRecipe recipe = TcRecipes.get(key);
                    if (recipe == null) {
                        problems.add(research.key() + ": unknown recipe " + key);
                        continue;
                    }
                    TcStack result = result(recipe);
                    if (result == null || !result.exists()) {
                        problems.add(research.key() + ": result item missing for " + key);
                    }
                    if (recipe instanceof CraftingRecipeRef reference
                        && helper.getLevel().getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, reference.recipe())).isEmpty()) {
                        problems.add(research.key() + ": crafting recipe missing for " + key);
                    }
                }
            }
        }
        helper.assertTrue(problems.isEmpty(), String.join("; ", problems));
        helper.succeed();
    }

    private static void parentsExist(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        for (ResearchItem research : ResearchCategories.allResearch()) {
            if (!TmResearch.CATEGORY.equals(research.category())) {
                continue;
            }
            List<String> parents = new ArrayList<>(research.parents());
            parents.addAll(research.parentsHidden());
            for (String parent : parents) {
                if (ResearchCategories.getResearch(parent) == null) {
                    problems.add(research.key() + ": unknown parent " + parent);
                }
            }
        }
        helper.assertTrue(problems.isEmpty(), String.join("; ", problems));
        helper.succeed();
    }

    private static void linkedResearchUsesOriginal(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        int count = 0;
        for (ResearchItem research : ResearchCategories.allResearch()) {
            if (!(research instanceof TmLinkedResearchItem linked)) {
                continue;
            }
            count++;
            ResearchItem original = linked.original();
            if (!original.siblings().contains(linked.key())) {
                problems.add(linked.key() + ": not a sibling of " + original.key());
            }
            if (!linked.nameKey().equals(original.nameKey()) || !linked.textKey().equals(original.textKey()) || !linked.pages().equals(original.pages())) {
                problems.add(linked.key() + ": name, text or pages do not come from " + original.key());
            }
        }
        helper.assertTrue(count == 1, "expected one linked research but was " + count);
        helper.assertTrue(problems.isEmpty(), String.join("; ", problems));
        helper.succeed();
    }

    private static void recipesMatchTable(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        for (JsonElement element : load(RECIPE_TABLE)) {
            JsonObject expected = element.getAsJsonObject();
            String key = expected.get("key").getAsString();
            TcRecipe actual = TcRecipes.get(key);
            if (actual == null) {
                problems.add(key + ": recipe is not registered");
                continue;
            }
            compareRecipe(key, expected, actual, problems);
        }
        helper.assertTrue(problems.isEmpty(), problems.size() + " recipe mismatches: " + String.join("; ", problems));
        helper.succeed();
    }

    private static void compareRecipe(String key, JsonObject expected, TcRecipe actual, List<String> problems) {
        String type = expected.get("type").getAsString();
        JsonElement research = expected.get("research");
        if (!research.isJsonNull()) {
            check(problems, key, "research", research.getAsString(), actual.research());
        }
        TcStack result = result(actual);
        if (result == null) {
            problems.add(key + ": recipe has no result");
            return;
        }
        compareStack(key, expected.getAsJsonObject("result"), result, problems);
        switch (type) {
            case "crafting" -> {
                if (!(actual instanceof CraftingRecipeRef)) {
                    problems.add(key + ": expected a crafting reference");
                }
            }
            case "infusion" -> {
                if (actual instanceof InfusionRecipe infusion) {
                    check(problems, key, "instability", expected.get("instability").getAsInt(), infusion.instability());
                    compareAspects(key, expected, infusion.aspects(), problems);
                    check(problems, key, "central", ids(expected.get("central")), ids(infusion.central()));
                    check(problems, key, "components", idList(expected.getAsJsonArray("components")), ingredientIds(infusion.components()));
                } else {
                    problems.add(key + ": expected an infusion recipe");
                }
            }
            case "arcane" -> {
                if (actual instanceof ShapedArcaneRecipe arcane) {
                    compareAspects(key, expected, arcane.aspects(), problems);
                    check(problems, key, "grid", grid(expected), ingredientIds(arcane.grid()));
                    check(problems, key, "width", expected.getAsJsonArray("pattern").asList().stream().mapToInt(row -> row.getAsString().length()).max().orElse(0), arcane.width());
                    check(problems, key, "height", expected.getAsJsonArray("pattern").size(), arcane.height());
                } else {
                    problems.add(key + ": expected a shaped arcane recipe");
                }
            }
            case "arcane_shapeless" -> {
                if (actual instanceof ShapelessArcaneRecipe arcane) {
                    compareAspects(key, expected, arcane.aspects(), problems);
                    check(problems, key, "ingredients", idList(expected.getAsJsonArray("ingredients")), ingredientIds(arcane.ingredients()));
                } else {
                    problems.add(key + ": expected a shapeless arcane recipe");
                }
            }
            case "crucible" -> {
                if (actual instanceof CrucibleRecipe crucible) {
                    compareAspects(key, expected, crucible.aspects(), problems);
                    check(problems, key, "catalyst", ids(expected.get("catalyst")), ids(crucible.catalyst()));
                } else {
                    problems.add(key + ": expected a crucible recipe");
                }
            }
            default -> problems.add(key + ": unknown recipe type " + type);
        }
    }

    private static TcStack result(TcRecipe recipe) {
        return switch (recipe) {
            case ShapedArcaneRecipe arcane -> arcane.result();
            case ShapelessArcaneRecipe arcane -> arcane.result();
            case InfusionRecipe infusion -> infusion.result();
            case CrucibleRecipe crucible -> crucible.result();
            case CraftingRecipeRef reference -> reference.result();
            default -> null;
        };
    }

    private static void compareStack(String key, JsonObject expected, TcStack actual, List<String> problems) {
        check(problems, key, "result", expected.get("item").getAsString(), actual.item().toString());
        check(problems, key, "result count", expected.get("count").getAsInt(), actual.count());
        Map<String, String> data = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : expected.getAsJsonObject("data").entrySet()) {
            data.put(entry.getKey(), entry.getValue().getAsString());
        }
        check(problems, key, "result data", data, actual.data());
        if (!actual.exists()) {
            problems.add(key + ": result item " + actual.item() + " does not exist");
        }
    }

    private static void compareAspects(String key, JsonObject expected, AspectList actual, List<String> problems) {
        Map<String, Integer> aspects = new HashMap<>();
        for (JsonElement entry : expected.getAsJsonArray("aspects")) {
            JsonArray pair = entry.getAsJsonArray();
            aspects.merge(pair.get(0).getAsString(), pair.get(1).getAsInt(), Integer::sum);
        }
        check(problems, key, "aspects", aspects, aspectMap(actual));
    }

    private static List<List<String>> grid(JsonObject expected) {
        JsonArray pattern = expected.getAsJsonArray("pattern");
        JsonObject symbols = expected.getAsJsonObject("symbols");
        int width = 0;
        for (JsonElement row : pattern) {
            width = Math.max(width, row.getAsString().length());
        }
        List<List<String>> cells = new ArrayList<>();
        for (JsonElement element : pattern) {
            String row = element.getAsString();
            for (int column = 0; column < width; column++) {
                char symbol = column < row.length() ? row.charAt(column) : ' ';
                cells.add(symbol == ' ' ? List.of() : ids(symbols.get(String.valueOf(symbol))));
            }
        }
        return cells;
    }

    private static List<List<String>> idList(JsonArray array) {
        List<List<String>> result = new ArrayList<>();
        for (JsonElement element : array) {
            result.add(ids(element));
        }
        return result;
    }

    private static List<String> ids(JsonElement element) {
        List<String> result = new ArrayList<>();
        if (element.isJsonArray()) {
            for (JsonElement item : element.getAsJsonArray()) {
                result.add(item.getAsString());
            }
        } else {
            result.add(element.getAsString());
        }
        return result;
    }

    private static List<List<String>> ingredientIds(List<TcIngredient> ingredients) {
        List<List<String>> result = new ArrayList<>();
        for (TcIngredient ingredient : ingredients) {
            result.add(ids(ingredient));
        }
        return result;
    }

    private static List<String> ids(TcIngredient ingredient) {
        return switch (ingredient) {
            case TcIngredient.Empty empty -> List.of();
            case TcIngredient.Stack stack -> List.of(stack.stack().item().toString());
            case TcIngredient.Tag tag -> List.of("#" + tag.tag().location());
            case TcIngredient.AnyOf anyOf -> {
                List<String> result = new ArrayList<>();
                for (TcIngredient option : anyOf.options()) {
                    result.addAll(ids(option));
                }
                yield result;
            }
        };
    }

    private static Map<String, Integer> aspectMap(AspectList list) {
        Map<String, Integer> result = new HashMap<>();
        for (Aspect aspect : list.getAspects()) {
            result.put(aspect.getTag(), list.getAmount(aspect));
        }
        return result;
    }

    private static void compare(JsonObject expected, ResearchItem actual, List<String> problems) {
        String key = actual.key();
        check(problems, key, "category", expected.get("category").getAsString(), actual.category());
        check(problems, key, "column", expected.get("column").getAsInt(), actual.column());
        check(problems, key, "row", expected.get("row").getAsInt(), actual.row());
        check(problems, key, "complexity", expected.get("complexityClamped").getAsInt(), actual.complexity());
        check(problems, key, "warp", expected.get("warp").getAsInt(), actual.warp());
        check(problems, key, "special", expected.get("special").getAsBoolean(), actual.isSpecial());
        check(problems, key, "round", expected.get("round").getAsBoolean(), actual.isRound());
        check(problems, key, "stub", expected.get("stub").getAsBoolean(), actual.isStub());
        check(problems, key, "virtual", expected.get("virtual").getAsBoolean(), actual.isVirtual());
        check(problems, key, "concealed", expected.get("concealed").getAsBoolean(), actual.isConcealed());
        check(problems, key, "hidden", expected.get("hidden").getAsBoolean(), actual.isHidden());
        check(problems, key, "lost", expected.get("lost").getAsBoolean(), actual.isLost());
        check(problems, key, "autoUnlock", expected.get("autoUnlock").getAsBoolean(), actual.isAutoUnlock());
        check(problems, key, "parents", strings(expected.getAsJsonArray("parents")), actual.parents());
        check(problems, key, "parentsHidden", strings(expected.getAsJsonArray("parentsHidden")), actual.parentsHidden());
        List<String> triggers = new ArrayList<>();
        for (Identifier trigger : actual.itemTriggers()) {
            triggers.add(trigger.toString());
        }
        check(problems, key, "itemTriggers", strings(expected.getAsJsonArray("itemTriggers")), triggers);
        Map<String, Integer> aspects = new HashMap<>();
        for (JsonElement entry : expected.getAsJsonArray("aspects")) {
            JsonArray pair = entry.getAsJsonArray();
            aspects.merge(pair.get(0).getAsString(), pair.get(1).getAsInt(), Integer::sum);
        }
        check(problems, key, "aspects", aspects, aspectMap(actual.tags()));
        JsonElement fake = expected.get("fake");
        if (fake != null && !fake.isJsonNull()) {
            String original = fake.getAsJsonObject().get("original").getAsString();
            if (!(actual instanceof TmLinkedResearchItem linked) || !linked.original().key().equals(original)) {
                problems.add(key + ": expected a link to " + original);
            } else {
                check(problems, key, "secondary", linked.original().isSecondary(), actual.isSecondary());
            }
            return;
        }
        check(problems, key, "secondary", expected.get("secondary").getAsBoolean(), actual.isSecondary());
        check(problems, key, "siblings", strings(expected.getAsJsonArray("siblings")), actual.siblings());
        comparePages(key, expected.getAsJsonArray("pages"), actual.pages(), problems);
    }

    private static void comparePages(String key, JsonArray expected, List<ResearchPage> actual, List<String> problems) {
        if (expected.size() != actual.size()) {
            problems.add(key + ": expected " + expected.size() + " pages but was " + actual.size());
            return;
        }
        for (int index = 0; index < expected.size(); index++) {
            JsonObject page = expected.get(index).getAsJsonObject();
            String type = page.get("type").getAsString();
            ResearchPage found = actual.get(index);
            String label = "page " + index;
            switch (type) {
                case "text" -> {
                    if (found instanceof ResearchPage.Text text) {
                        check(problems, key, label, PAGE_PREFIX + key + "." + page.get("text").getAsString(), text.text());
                    } else {
                        problems.add(key + ": " + label + " expected text but was " + found);
                    }
                }
                case "crafting" -> recipes(problems, key, label, page, found instanceof ResearchPage.Crafting crafting ? crafting.recipes() : null);
                case "arcane" -> recipes(problems, key, label, page, found instanceof ResearchPage.Arcane arcane ? arcane.recipes() : null);
                case "crucible" -> recipes(problems, key, label, page, found instanceof ResearchPage.Crucible crucible ? crucible.recipes() : null);
                case "infusion" -> recipes(problems, key, label, page, found instanceof ResearchPage.Infusion infusion ? infusion.recipes() : null);
                default -> problems.add(key + ": unknown page type " + type);
            }
        }
    }

    private static void recipes(List<String> problems, String key, String label, JsonObject page, List<String> actual) {
        if (actual == null) {
            problems.add(key + ": " + label + " has a wrong type, expected " + page.get("type").getAsString());
            return;
        }
        check(problems, key, label + " recipes", strings(page.getAsJsonArray("recipes")), actual);
    }

    private static void check(List<String> problems, String key, String field, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            problems.add(key + ": " + field + " expected " + expected + " but was " + actual);
        }
    }

    private static List<String> strings(JsonArray array) {
        List<String> values = new ArrayList<>();
        for (JsonElement element : array) {
            values.add(element.getAsString());
        }
        return values;
    }

    private static JsonArray load(String table) {
        Path directory = Path.of("").toAbsolutePath();
        while (directory != null) {
            Path candidate = directory.resolve(table);
            if (Files.exists(candidate)) {
                try {
                    return JsonParser.parseString(Files.readString(candidate)).getAsJsonArray();
                } catch (IOException exception) {
                    throw new IllegalStateException(exception);
                }
            }
            directory = directory.getParent();
        }
        throw new IllegalStateException(table + " not found");
    }
}
