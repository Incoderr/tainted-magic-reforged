ITEMS = [
    "warpwood_rod",
    "warpwood_staff_core",
    "shadow_metal_wand_cap",
    "cloth_wand_cap",
    "crimson_cloth_wand_cap",
    "shadow_cloth_wand_cap",
]


def generate(ctx):
    for name in ITEMS:
        ctx.generated_item(name, name)
