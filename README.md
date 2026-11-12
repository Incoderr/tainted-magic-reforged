# Tainted Magic: Reforged

Unofficial port of **Tainted Magic 8.2.0** by John Yorke from Minecraft 1.7.10 to **Minecraft 26.3** on **NeoForge 26.3**. It is an addon to [Thaumcraft 4: Reforged](https://github.com/notlikethiss/thaumcraft-4-reforged) and needs it to run.

The goal is to recreate the original gameplay as closely as possible: shadow metal and warpwood, the warpwood wand and staff, new caps and foci with their upgrades, void and shadow fortress armor, voidwalker gear, the katana and the rest of the darker side of Thaumaturgy. Integrations with other mods are not ported, except recipe viewers (JEI and REI).

> **Personal use only.** Tainted Magic and all of its assets belong to John Yorke and are licensed under CC BY-NC-ND 4.0, which does not allow sharing adaptations. This port is not intended for redistribution.

## Requirements to build

- Java 25
- Python 3
- A checkout of Thaumcraft 4: Reforged next to this repository (`../thaumcraft-4-reforged`)

## Building and running

The base mod is included as a Gradle composite build, so Gradle builds it automatically and both mods are loaded in every run.

```sh
python3 tools/fetch_tm.py
python3 tools/convert_assets.py
python3 tools/gen_resources.py
./gradlew build
./gradlew runClient
./gradlew runGameTestServer
```
