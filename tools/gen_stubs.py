ITEMS = [
    "focus_taint_swarm",
    "focus_dark_matter",
    "focus_mage_mace",
    "focus_shockwave",
    "focus_vis_shard",
    "focus_lumos",
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
    "gate_key",
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
