import shutil

ITEMS = [
    "warped_goggles",
    "void_metal_goggles",
    "void_fortress_helmet",
    "void_fortress_chestplate",
    "void_fortress_leggings",
    "shadow_fortress_helmet",
    "shadow_fortress_chestplate",
    "shadow_fortress_leggings",
    "voidwalker_boots",
    "voidwalker_sash",
    "lumos_ring",
    "flyte_charm",
]
EQUIPMENT = {
    "warped_goggles": {"humanoid": "model_warped_goggles"},
    "void_metal_goggles": {"humanoid": "model_voidmetal_goggles"},
    "voidwalker_boots": {"humanoid": "model_voidwalker_boots"},
    "void_fortress": {"humanoid": "model_void_fortress_armor", "humanoid_leggings": "model_void_fortress_armor"},
    "shadow_fortress": {"humanoid": "model_shadow_fortress_armor", "humanoid_leggings": "model_shadow_fortress_armor"},
}
HEAD = ["warped_goggles", "void_metal_goggles", "void_fortress_helmet", "shadow_fortress_helmet"]
CHEST = ["void_fortress_chestplate", "shadow_fortress_chestplate"]
LEGS = ["void_fortress_leggings", "shadow_fortress_leggings"]
FEET = ["voidwalker_boots"]


def equipment(ctx):
    for asset, textures in EQUIPMENT.items():
        layers = {}
        for layer, source in textures.items():
            destination = ctx.ASSETS / "textures" / "entity" / "equipment" / layer / f"{asset}.png"
            destination.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(ctx.SOURCE_TEXTURES / "models" / f"{source}.png", destination)
            layers[layer] = [{"texture": f"{ctx.NS}:{asset}"}]
        ctx.write(ctx.ASSETS / "equipment" / f"{asset}.json", {"layers": layers})


def generate(ctx):
    for name in ITEMS:
        ctx.generated_item(name, name)

    ctx.item_model("gate_key", {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": ctx.ref("gate_key", "item"), "layer1": ctx.ref("gate_key_overlay", "item")},
    })
    ctx.item_definition("gate_key", ctx.ref("gate_key", "item"), [ctx.tint(0xFFFFFF), {"type": f"{ctx.NS}:gate_key"}])

    equipment(ctx)

    ctx.item_tags[f"{ctx.NS}:repairs_shadow_metal"].add(f"{ctx.NS}:shadow_metal_ingot")
    for tag, names in (
        ("minecraft:head_armor", HEAD),
        ("minecraft:chest_armor", CHEST),
        ("minecraft:leg_armor", LEGS),
        ("minecraft:foot_armor", FEET),
        ("minecraft:enchantable/armor", HEAD + CHEST + LEGS + FEET),
        ("minecraft:enchantable/head_armor", HEAD),
        ("minecraft:enchantable/chest_armor", CHEST),
        ("minecraft:enchantable/leg_armor", LEGS),
        ("minecraft:enchantable/foot_armor", FEET),
        ("minecraft:enchantable/durability", HEAD + CHEST + LEGS + FEET),
        ("c:armors", HEAD + CHEST + LEGS + FEET),
    ):
        for name in names:
            ctx.item_tags[tag].add(f"{ctx.NS}:{name}")

    ctx.recipe("void_blood_armor", {"type": f"{ctx.NS}:void_blood"}, [])
