ITEMS = [
    "shadow_metal_ingot",
    "shadow_cloth",
    "crimson_cloth",
    "warped_shard",
    "tainted_shard",
    "creation_shard",
    "thaumic_alloy_plating",
    "crimson_plating",
    "shadow_metal_nugget",
    "primordial_nodule",
    "primordial_mote",
    "creation_fragment",
    "void_blood",
    "crimson_blood",
    "salis_tempestas",
    "salis_aevum",
]


def generate(ctx):
    for name in ITEMS:
        ctx.generated_item(name, name)
    ctx.item_tags["c:ingots/shadow_metal"].add(f"{ctx.NS}:shadow_metal_ingot")
    ctx.item_tags["c:ingots"].add(f"{ctx.NS}:shadow_metal_ingot")
    ctx.shaped(
        "shadow_metal_ingot_from_nuggets", f"{ctx.NS}:shadow_metal_ingot", 1,
        ["AAA", "AAA", "AAA"],
        {"A": f"{ctx.NS}:shadow_metal_nugget"},
    )
    ctx.shaped(
        "shadow_metal_nugget", f"{ctx.NS}:shadow_metal_nugget", 9,
        ["A"],
        {"A": f"{ctx.NS}:shadow_metal_ingot"},
    )
