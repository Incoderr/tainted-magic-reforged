import json
from pathlib import Path

TABLE = Path(__file__).resolve().parent / "tm_recipes.json"


def generate(ctx):
    for entry in json.loads(TABLE.read_text(encoding="utf-8")):
        if entry["type"] != "crafting":
            continue
        result = entry["result"]
        ctx.shaped(
            result["item"].split(":")[1], result["item"], result["count"],
            entry["pattern"],
            {symbol: value for symbol, value in entry["symbols"].items()},
            "equipment",
        )
