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
    ((22, 0), (-1.0, -12.0, -1.5), (2, 12, 3), 0.0),
    ((0, 0), (-0.5, -40.0, -2.0), (1, 48, 4), -0.75),
    ((0, 52), (-2.5, -40.0, -3.5), (5, 1, 7), 0.0),
]
SCALE = 0.5
OFFSET_Y = 4.0


def uv(u, v, width, height):
    return [u * 0.5, v * 0.25, (u + width) * 0.5, (v + height) * 0.25]


def element(texture_uv, origin, size, inflate):
    u, v = texture_uv
    dx, dy, dz = size
    low = [origin[axis] - inflate for axis in range(3)]
    high = [origin[axis] + size[axis] + inflate for axis in range(3)]
    low, high = [min(a, b) for a, b in zip(low, high)], [max(a, b) for a, b in zip(low, high)]
    start = [8 + low[0] * SCALE, OFFSET_Y - high[1] * SCALE, 8 + low[2] * SCALE]
    end = [8 + high[0] * SCALE, OFFSET_Y - low[1] * SCALE, 8 + high[2] * SCALE]
    faces = {
        "up": uv(u + dz, v, dx, dz),
        "down": uv(u + dz + dx, v, dx, dz),
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
    "ground": {"rotation": [0, 0, 0], "translation": [0, 2, 0], "scale": [0.4, 0.4, 0.4]},
    "fixed": {"rotation": [0, 0, -45], "translation": [0, 0, 0], "scale": [0.6, 0.6, 0.6]},
    "thirdperson_righthand": {"rotation": [0, 90, 0], "translation": [0, 5, 1], "scale": [0.85, 0.85, 0.85]},
    "thirdperson_lefthand": {"rotation": [0, -90, 0], "translation": [0, 5, 1], "scale": [0.85, 0.85, 0.85]},
    "firstperson_righthand": {"rotation": [0, -90, 25], "translation": [1.13, 4.2, 1.13], "scale": [0.68, 0.68, 0.68]},
    "firstperson_lefthand": {"rotation": [0, 90, -25], "translation": [1.13, 4.2, 1.13], "scale": [0.68, 0.68, 0.68]},
}


def generate(ctx):
    for name in ITEMS:
        ctx.generated_item(name, name)
    for name, source in KATANAS.items():
        destination = ctx.ASSETS / "textures" / "item" / f"{name}.png"
        destination.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(ctx.SOURCE_TEXTURES / "models" / f"{source}.png", destination)
        texture = f"{ctx.NS}:item/{name}"
        ctx.item_model(name, {
            "textures": {"katana": texture, "particle": texture},
            "elements": [element(*box) for box in BOXES],
            "display": DISPLAY,
        })
        ctx.item_definition(name, f"{ctx.NS}:item/{name}")
