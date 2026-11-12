def generate(ctx):
    ctx.block_model("nightshade_bush", {"parent": "minecraft:block/cross", "textures": {"cross": ctx.ref("nightshade_bush")}})
    ctx.simple_state("nightshade_bush")
    ctx.generated_item("nightshade_bush", "nightshade_bush", texture_kind="block")
    ctx.write_loot("nightshade_bush", [{
        "rolls": 1,
        "entries": [{
            "type": "minecraft:alternatives",
            "children": [
                {"type": "minecraft:item", "condition": "minecraft:tool/can_shear", "name": f"{ctx.NS}:nightshade_bush"},
                {
                    "type": "minecraft:item",
                    "name": f"{ctx.NS}:nightshade_berries",
                    "modifier": [{"type": "minecraft:set_count", "count": {"type": "minecraft:uniform", "min": 1, "max": 3}}],
                },
            ],
        }],
    }])

    ctx.generated_item("nightshade_berries", "nightshade_berries")
    ctx.generated_item("magic_funguar", "magic_funguar")

    ctx.write(ctx.DATA / ctx.NS / "damage_type" / "nightshade.json", {
        "exhaustion": 0.3,
        "message_id": f"{ctx.NS}.nightshade",
        "scaling": "always",
    })
