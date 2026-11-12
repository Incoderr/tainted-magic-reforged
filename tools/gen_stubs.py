ITEMS = [
    "thaumic_disassembler",
    "hollow_dagger",
    "shadow_metal_hoe",
    "shadow_metal_pickaxe",
    "shadow_metal_axe",
    "shadow_metal_shovel",
    "shadow_metal_sword",
    "primal_blade",
]


def generate(ctx):
    for name in ITEMS:
        ctx.generated_item(name, name)
