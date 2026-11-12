import shutil

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
KATANAS = {
    "thaumium_katana": "model_katana_thaumium",
    "void_metal_katana": "model_katana_voidmetal",
    "shadow_metal_katana": "model_katana_shadowmetal",
}
BOXES = [
    ((22, 0), (-1.0, -52.0, -1.5), (2, 12, 3), 0.0),
    ((0, 0), (-0.5, -40.0, -2.0), (1, 48, 4), -0.75),
    ((0, 52), (-2.5, -40.0, -3.5), (5, 1, 7), 0.0),
]
SAYA = [((10, 0), (-1.0, -39.5, -2.0), (2, 48, 4), 0.0)]
SCALE = 0.4
OFFSET_Y = 26.4


def uv(u, v, width, height):
    return [u * 0.5, v * 0.25, (u + width) * 0.5, (v + height) * 0.25]


def element(texture_uv, origin, size, inflate):
    u, v = texture_uv
    dx, dy, dz = size
    low = [origin[axis] - inflate for axis in range(3)]
    high = [origin[axis] + size[axis] + inflate for axis in range(3)]
    low, high = [min(a, b) for a, b in zip(low, high)], [max(a, b) for a, b in zip(low, high)]
    start = [8 + low[0] * SCALE, OFFSET_Y + low[1] * SCALE, 8 + low[2] * SCALE]
    end = [8 + high[0] * SCALE, OFFSET_Y + high[1] * SCALE, 8 + high[2] * SCALE]
    faces = {
        "down": uv(u + dz, v, dx, dz),
        "up": uv(u + dz + dx, v, dx, dz),
        "west": uv(u, v + dz, dz, dy),
        "north": uv(u + dz, v + dz, dx, dy),
        "east": uv(u + dz + dx, v + dz, dz, dy),
        "south": uv(u + 2 * dz + dx, v + dz, dx, dy),
    }
    return {
        "from": [round(value, 4) for value in start],
        "to": [round(value, 4) for value in end],
        "faces": {face: {"uv": coords, "texture": "#katana"} for face, coords in faces.items()},
    }


DISPLAY = {
    "gui": {"rotation": [0, 0, -45], "translation": [0, 0, 0], "scale": [0.55, 0.55, 0.55]},
    "ground": {"rotation": [0, 0, 0], "translation": [0, 2, 0], "scale": [0.5, 0.5, 0.5]},
    "fixed": {"rotation": [0, 0, -45], "translation": [0, 0, 0], "scale": [0.6, 0.6, 0.6]},
    "thirdperson_righthand": {"rotation": [0, 0, 0], "translation": [0, 3, 1], "scale": [1.0, 1.0, 1.0]},
    "thirdperson_lefthand": {"rotation": [0, 0, 0], "translation": [0, 3, 1], "scale": [1.0, 1.0, 1.0]},
    "firstperson_righthand": {"rotation": [-35, 0, 0], "translation": [1.5, -1.5, -2.0], "scale": [0.7, 0.7, 0.7]},
    "firstperson_lefthand": {"rotation": [-35, 0, 0], "translation": [-1.5, -1.5, -2.0], "scale": [0.7, 0.7, 0.7]},
}
CHARGING = dict(DISPLAY)
CHARGING["firstperson_righthand"] = {"rotation": [-75, 0, 0], "translation": [1.5, -1.0, -2.0], "scale": [0.7, 0.7, 0.7]}
CHARGING["firstperson_lefthand"] = {"rotation": [-75, 0, 0], "translation": [-1.5, -1.0, -2.0], "scale": [0.7, 0.7, 0.7]}


def generate(ctx):
    for name in ITEMS:
        ctx.generated_item(name, name)
    for name, source in KATANAS.items():
        destination = ctx.ASSETS / "textures" / "item" / f"{name}.png"
        destination.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(ctx.SOURCE_TEXTURES / "models" / f"{source}.png", destination)
        texture = f"{ctx.NS}:item/{name}"
        models = {
            name: (BOXES, DISPLAY),
            f"{name}_charging": (BOXES, CHARGING),
            f"{name}_sheathed": (BOXES + SAYA, DISPLAY),
        }
        for model, (boxes, display) in models.items():
            ctx.item_model(model, {
                "textures": {"katana": texture, "particle": texture},
                "elements": [element(*box) for box in boxes],
                "display": display,
            })
        ref = lambda model: {"type": "minecraft:model", "model": f"{ctx.NS}:item/{model}"}
        ctx.write(ctx.ASSETS / "items" / f"{name}.json", {"model": {
            "type": "minecraft:select",
            "property": "minecraft:display_context",
            "cases": [{"when": ["gui", "ground", "fixed"], "model": ref(f"{name}_sheathed")}],
            "fallback": {
                "type": "minecraft:condition",
                "property": "minecraft:using_item",
                "on_true": ref(f"{name}_charging"),
                "on_false": ref(name),
            },
        }})
