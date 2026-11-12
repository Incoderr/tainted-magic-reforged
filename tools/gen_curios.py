NS = "taintedmagic"
BAUBLES = {
    "belt": ["voidwalker_sash"],
    "ring": ["lumos_ring"],
}


def generate(ctx):
    if ctx.profile.modern:
        return
    ctx.write(ctx.DATA / NS / "curios" / "entities" / "player.json", {
        "entities": ["minecraft:player"],
        "slots": [*BAUBLES],
    })
    for slot, items in BAUBLES.items():
        for item in items:
            ctx.item_tags[f"curios:{slot}"].add(f"{NS}:{item}")
