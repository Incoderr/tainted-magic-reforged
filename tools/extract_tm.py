import json
import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))

import tmjava

ROOT = Path(__file__).resolve().parent.parent
SOURCE = ROOT / "reference" / "tm-src" / "src" / "main" / "java" / "taintedmagic" / "common"
RESEARCH_OUTPUT = ROOT / "tools" / "tm_research.json"
RECIPES_OUTPUT = ROOT / "tools" / "tm_recipes.json"
ID_MAP = ROOT / "tools" / "id_map.json"
REFS = ROOT / "tools" / "tc4_refs.json"
BASE = next(parent / "thaumcraft-4-reforged" for parent in ROOT.parents if (parent / "thaumcraft-4-reforged").is_dir())
ASPECT_SOURCE = BASE / "src" / "main" / "java" / "thaumcraft" / "aspect" / "Aspect.java"
NS = "taintedmagic"
CATEGORY = "TAINTEDMAGIC"
WILDCARD = "*"
PRIMALS = ["FIRE", "WATER", "EARTH", "AIR", "ORDER", "ENTROPY"]
CONSTANTS = {"ItemKatana.TAG_INSCRIPTION": "inscription", "OreDictionary.WILDCARD_VALUE": WILDCARD}
ITEM_API = {"itemHelmetCultistLeaderPlate": "thaumcraft:crimson_praetor_helm"}
PAGE_HELPERS = {"recipePage": "crafting", "arcanePage": "arcane", "infusionPage": "infusion", "cruciblePage": "crucible"}
FLAG_METHODS = {
    "setSecondary": "secondary",
    "setRound": "round",
    "setStub": "stub",
    "setSpecial": "special",
    "setConcealed": "concealed",
    "setAutoUnlock": "autoUnlock",
    "setVirtual": "virtual",
    "setHidden": "hidden",
    "setLost": "lost",
}
LIST_METHODS = {"setParents": "parents", "setParentsHidden": "parentsHidden", "setSiblings": "siblings"}
RECIPE_METHODS = {
    "addInfusionCraftingRecipe": "infusion",
    "addArcaneCraftingRecipe": "arcane",
    "addShapelessArcaneCraftingRecipe": "arcane_shapeless",
    "addCrucibleRecipe": "crucible",
    "addShapedRecipe": "crafting",
}
ASPECT_TAGS = dict(re.findall(r"public static final Aspect (\w+) = \w+\(\"(\w+)\"", ASPECT_SOURCE.read_text(encoding="utf-8")))


class Items:
    def __init__(self):
        id_map = json.loads(ID_MAP.read_text(encoding="utf-8"))
        self.own = {}
        for entry in id_map["blocks"] + id_map["items"]:
            if entry.get("skip") or entry.get("variant"):
                continue
            self.own.setdefault(entry["old"], {})[entry["meta"]] = f"{NS}:{entry['id']}"
        refs = json.loads(REFS.read_text(encoding="utf-8"))
        self.thaumcraft = refs["thaumcraft"]
        self.vanilla = refs["vanilla"]

    def own_ids(self, name, meta):
        metas = self.own[name]
        if meta == WILDCARD:
            ordered = [metas[key] for key in sorted(key for key in metas if key is not None)]
            return list(dict.fromkeys(([metas[None]] if None in metas else []) + ordered))
        if meta in metas:
            return [metas[meta]]
        if None in metas:
            return [metas[None]]
        raise ValueError(f"no id for {name}:{meta}")

    def thaumcraft_ids(self, name, meta):
        metas = self.thaumcraft[name]
        if meta == WILDCARD:
            values = [metas[WILDCARD]] if WILDCARD in metas else [value for value in metas.values() if not value.startswith("skipped")]
        else:
            values = [metas[str(meta)] if str(meta) in metas else metas[WILDCARD]]
        result = []
        for value in values:
            if value.startswith("skipped"):
                replacement = re.search(r"replaced by vanilla (minecraft:\w+)", value)
                if not replacement:
                    raise ValueError(f"{name}:{meta} is {value}")
                value = replacement.group(1)
            result.append(value)
        return result

    def vanilla_ids(self, name, meta):
        metas = self.vanilla[name]
        key = str(meta) if meta != WILDCARD and str(meta) in metas else WILDCARD
        return list(metas[key])

    def resolve(self, owner, name, meta):
        if owner in ("ItemRegistry", "BlockRegistry"):
            return self.own_ids(name, meta)
        if owner in ("ConfigItems", "ConfigBlocks"):
            return self.thaumcraft_ids(f"{owner}.{name}", meta)
        if owner in ("Items", "Blocks"):
            return self.vanilla_ids(f"{owner}.{name}", meta)
        raise ValueError(f"unknown item owner {owner}")


ITEMS = Items()


def chain_of(node):
    calls = []
    current = node
    while current["t"] == "call" and current["target"] is not None:
        calls.append(current)
        current = current["target"]
    calls.reverse()
    return current, calls


def integer(node):
    if node["t"] == "num":
        return int(node["v"])
    if node["t"] == "neg":
        return -integer(node["v"])
    raise ValueError("cannot evaluate int " + node["raw"])


def string(node):
    if node["t"] == "str":
        return node["v"]
    if node["t"] == "field" and node["target"]["t"] == "name":
        return CONSTANTS[f"{node['target']['v']}.{node['name']}"]
    raise ValueError("cannot evaluate string " + node["raw"])


def field_name(node):
    if node["t"] == "field" and node["target"]["t"] == "name":
        return node["target"]["v"], node["name"]
    raise ValueError("not a field " + node["raw"])


def meta_value(node):
    if node["t"] == "field":
        owner, name = field_name(node)
        return CONSTANTS[f"{owner}.{name}"]
    return integer(node)


def stack(node):
    if node["t"] == "new" and node["cls"] == "ItemStack":
        args = node["args"]
        owner, name = field_name(args[0])
        count = integer(args[1]) if len(args) > 1 else 1
        meta = meta_value(args[2]) if len(args) > 2 else 0
        return ITEMS.resolve(owner, name, meta), count
    if node["t"] == "field":
        owner, name = field_name(node)
        return ITEMS.resolve(owner, name, 0), 1
    if node["t"] == "call" and node["name"] == "getItem" and node["target"]["t"] == "name" and node["target"]["v"] == "ItemApi":
        return [ITEM_API[string(node["args"][0])]], 1
    raise ValueError("cannot evaluate item " + node["raw"])


def ingredient(node):
    ids, _ = stack(node)
    return ids[0] if len(ids) == 1 else ids


def result(node):
    ids, count = stack(node)
    if len(ids) != 1:
        raise ValueError("ambiguous result " + node["raw"])
    return {"item": ids[0], "count": count, "data": {}}


def aspects(node):
    root, calls = chain_of(node)
    entries = []
    if root["t"] == "new" and root["cls"] == "AspectList" and not root["args"]:
        pass
    elif root["t"] == "name" and root["v"] == "TaintedMagicHelper" and calls and calls[0]["name"] == "getPrimals":
        amount = integer(calls[0]["args"][0])
        entries = [[ASPECT_TAGS[name], amount] for name in PRIMALS]
        calls = calls[1:]
    else:
        raise ValueError("cannot evaluate aspects " + node["raw"])
    for call in calls:
        if call["name"] not in ("add", "merge"):
            raise ValueError("unexpected aspect call " + call["raw"])
        if len(call["args"]) == 1:
            entries.extend(aspects(call["args"][0]))
            continue
        owner, name = field_name(call["args"][0])
        if owner != "Aspect":
            raise ValueError("unexpected aspect " + call["raw"])
        entries.append([ASPECT_TAGS[name], integer(call["args"][1])])
    merged = {}
    for tag, amount in entries:
        merged[tag] = merged.get(tag, 0) + amount
    return [[tag, amount] for tag, amount in merged.items()]


def nbt_value(node):
    if node["t"] == "new" and node["cls"] in ("NBTTagInt", "NBTTagByte"):
        return str(integer(node["args"][0]))
    raise ValueError("cannot evaluate nbt " + node["raw"])


def recipe(key, node):
    method = node["name"]
    kind = RECIPE_METHODS[method]
    args = node["args"]
    entry = {"key": key, "type": kind}
    if kind == "crafting":
        entry["research"] = None
        entry["result"] = result(args[0])
        pattern, symbols = shaped(args[1:])
        entry["pattern"] = pattern
        entry["symbols"] = symbols
        return entry
    entry["research"] = string(args[0])
    if kind == "infusion":
        central = ingredient(args[4])
        if args[1]["t"] == "array":
            items = args[1]["items"]
            first = central if isinstance(central, str) else central[0]
            entry["result"] = {"item": first, "count": 1, "data": {string(items[0]): nbt_value(items[1])}}
        else:
            entry["result"] = result(args[1])
        entry["instability"] = integer(args[2])
        entry["aspects"] = aspects(args[3])
        entry["central"] = central
        entry["components"] = [ingredient(item) for item in args[5]["items"]]
    elif kind == "arcane":
        entry["result"] = result(args[1])
        entry["aspects"] = aspects(args[2])
        pattern, symbols = shaped(args[3:])
        entry["pattern"] = pattern
        entry["symbols"] = symbols
    elif kind == "arcane_shapeless":
        entry["result"] = result(args[1])
        entry["aspects"] = aspects(args[2])
        entry["ingredients"] = [ingredient(item) for item in args[3:]]
    elif kind == "crucible":
        entry["result"] = result(args[1])
        entry["catalyst"] = ingredient(args[2])
        entry["aspects"] = aspects(args[3])
    return entry


def shaped(args):
    pattern = []
    index = 0
    while args[index]["t"] == "str":
        pattern.append(args[index]["v"])
        index += 1
    symbols = {}
    while index < len(args):
        symbols[args[index]["v"]] = ingredient(args[index + 1])
        index += 2
    return pattern, symbols


def collect_recipes(text):
    tokens = tmjava.tokenize(text)
    parser = tmjava.Parser(text, tokens)
    recipes = []
    for index, token in enumerate(tokens):
        if token[1] != "put" or tokens[index - 1][1] != "." or tokens[index - 2][1] != "recipes":
            continue
        parser.index = index + 1
        args = parser.parse_args()
        call = args[1]
        recipes.append(recipe(string(args[0]), call))
    return recipes


def icon(node):
    if node["t"] == "new" and node["cls"] == "ResourceLocation":
        return {"texture": string(node["args"][0])}
    ids, _ = stack(node)
    return {"item": ids[0]}


def page(node, recipes):
    if node["t"] == "new" and node["cls"] == "ResearchPage":
        return {"type": "text", "text": string(node["args"][0])}
    if node["t"] == "call" and node["target"] is None and node["name"] in PAGE_HELPERS:
        key = string(node["args"][0])
        if key not in recipes:
            raise ValueError("unknown recipe " + key)
        return {"type": PAGE_HELPERS[node["name"]], "recipes": [key]}
    raise ValueError("unknown page " + node["raw"])


def new_entry(key, tags, column, row, complexity, picture):
    entry = {
        "key": key,
        "category": CATEGORY,
        "aspects": tags,
        "column": column,
        "row": row,
        "complexity": complexity,
        "icon": picture,
        "parents": [],
        "parentsHidden": [],
        "siblings": [],
        "warp": 0,
        "pages": [],
        "itemTriggers": [],
        "aspectTriggers": [],
        "fake": None,
    }
    for flag in FLAG_METHODS.values():
        entry[flag] = False
    return entry


def research(node, recipes):
    root, calls = chain_of(node)
    args = root["args"]
    if root["cls"] == "ThaumcraftResearchItem":
        entry = new_entry(string(args[0]), [], integer(args[3]), integer(args[4]), 1, icon(args[5]))
        entry["fake"] = {"original": string(args[1]), "category": string(args[2])}
        entry["stub"] = True
        entry["hidden"] = True
    else:
        entry = new_entry(string(args[0]), aspects(args[1]), integer(args[2]), integer(args[3]), integer(args[5]), icon(args[4]))
        entry["warp"] = integer(args[6])
    registered = False
    for call in calls:
        name = call["name"]
        if name in FLAG_METHODS:
            entry[FLAG_METHODS[name]] = True
        elif name in LIST_METHODS:
            entry[LIST_METHODS[name]] = [string(item) for item in call["args"]]
        elif name == "setPages":
            entry["pages"] = [page(item, recipes) for item in call["args"]]
        elif name == "setItemTriggers":
            entry["itemTriggers"] = [ingredient(item) for item in call["args"]]
        elif name == "registerResearchItem":
            registered = True
        else:
            raise ValueError("unknown method " + name + " in " + entry["key"])
    if not registered:
        raise ValueError("research " + entry["key"] + " is not registered")
    if any(item["type"] == "infusion" for item in entry["pages"]) and "INFUSION" not in entry["parentsHidden"]:
        entry["parentsHidden"].append("INFUSION")
    entry["complexityClamped"] = max(1, min(3, entry["complexity"]))
    return entry


def collect_research(text, recipes):
    return [research(node, recipes) for node in tmjava.find_new(text, {"TMResearchItem", "ThaumcraftResearchItem"})]


def main():
    recipe_text = tmjava.strip_comments((SOURCE / "registry" / "RecipeRegistry.java").read_text(encoding="utf-8"))
    research_text = tmjava.strip_comments((SOURCE / "registry" / "ResearchRegistry.java").read_text(encoding="utf-8"))
    recipes = collect_recipes(recipe_text)
    keys = [entry["key"] for entry in recipes]
    duplicates = sorted({key for key in keys if keys.count(key) > 1})
    if duplicates:
        raise SystemExit("duplicate recipe keys: " + ", ".join(duplicates))
    by_key = {entry["key"]: entry for entry in recipes}
    entries = collect_research(research_text, by_key)
    research_keys = [entry["key"] for entry in entries]
    duplicates = sorted({key for key in research_keys if research_keys.count(key) > 1})
    if duplicates:
        raise SystemExit("duplicate research keys: " + ", ".join(duplicates))
    RECIPES_OUTPUT.write_text(json.dumps(recipes, indent=2, ensure_ascii=False) + "\n", encoding="utf-8", newline="\n")
    RESEARCH_OUTPUT.write_text(json.dumps(entries, indent=2, sort_keys=True, ensure_ascii=False) + "\n", encoding="utf-8", newline="\n")
    print("recipes:", len(recipes), "research:", len(entries), "fake:", sum(1 for entry in entries if entry["fake"]))


if __name__ == "__main__":
    main()
