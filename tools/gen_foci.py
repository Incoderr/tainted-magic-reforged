FOCI = {
    "focus_taint_swarm": ["thaumcraft:item/focus_whatever_orn", "focus_taint_swarm"],
    "focus_dark_matter": ["thaumcraft:item/focus_warding_orn", "focus_dark_matter"],
    "focus_mage_mace": ["focus_mage_mace_orn", "focus_mage_mace"],
    "focus_shockwave": ["focus_shockwave"],
    "focus_vis_shard": ["focus_vis_shard"],
    "focus_lumos": ["focus_lumos"],
}


def generate(ctx):
    for name, layers in FOCI.items():
        textures = {f"layer{index}": layer if ":" in layer else ctx.ref(layer, "item") for index, layer in enumerate(layers)}
        ctx.item_model(name, {"parent": "minecraft:item/generated", "textures": textures})
        ctx.item_definition(name, ctx.ref(name, "item"))

    ctx.block_model("lumos", {"textures": {"particle": "thaumcraft:block/blank"}})
    ctx.simple_state("lumos")
