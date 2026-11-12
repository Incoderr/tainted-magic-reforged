def log_models(ctx, name, side):
    ctx.cube_column(name, "warpwood_log_top", side)
    ctx.block_model(f"{name}_horizontal", {
        "parent": "minecraft:block/cube_column_horizontal",
        "textures": {"end": ctx.ref("warpwood_log_top"), "side": ctx.ref(side)},
    })
    ctx.blockstate(name, {"variants": {
        "axis=x": {"model": ctx.ref(f"{name}_horizontal"), "x": 90, "y": 90},
        "axis=y": {"model": ctx.ref(name)},
        "axis=z": {"model": ctx.ref(f"{name}_horizontal"), "x": 90},
    }})


def generate(ctx):
    log_models(ctx, "warpwood_log", "warpwood_log_side")
    ctx.block_item("warpwood_log")
    ctx.self_drop("warpwood_log")

    log_models(ctx, "warpwood_knot", "warpwood_log_knot")
    ctx.write_loot("warpwood_knot", [{
        "rolls": 1,
        "entries": [{
            "type": "minecraft:item",
            "name": "thaumcraft:void_seed",
            "modifier": [{"type": "minecraft:set_count", "count": {"type": "minecraft:uniform", "min": 1, "max": 5}}],
        }],
    }])

    ctx.cube_all("warpwood_planks", "warpwood_planks")
    ctx.simple_state("warpwood_planks")
    ctx.block_item("warpwood_planks")
    ctx.self_drop("warpwood_planks")

    ctx.block_model("warpwood_leaves", {"parent": "minecraft:block/leaves", "textures": {"all": ctx.ref("warpwood_leaves_transparent")}})
    ctx.simple_state("warpwood_leaves")
    ctx.block_item("warpwood_leaves")
    ctx.shear_only("warpwood_leaves")

    ctx.block_model("warpwood_sapling", {"parent": "minecraft:block/cross", "textures": {"cross": ctx.ref("warpwood_sapling")}})
    ctx.simple_state("warpwood_sapling")
    ctx.generated_item("warpwood_sapling", "warpwood_sapling", texture_kind="block")
    ctx.self_drop("warpwood_sapling")

    ctx.generated_item("warp_fertilizer", "warp_fertilizer")

    ctx.mineable("axe", "warpwood_log", "warpwood_knot", "warpwood_planks")
    ctx.mineable("hoe", "warpwood_leaves")
    ctx.tag_both("minecraft:logs", "warpwood_log")
    ctx.tag_both("minecraft:planks", "warpwood_planks")
    ctx.tag_both("minecraft:leaves", "warpwood_leaves")
    ctx.tag_both("minecraft:saplings", "warpwood_sapling")

    ctx.shaped(
        "warpwood_planks", f"{ctx.NS}:warpwood_planks", 4,
        ["A"],
        {"A": f"{ctx.NS}:warpwood_log"},
        "building",
    )
