package taintedmagic.crafting;

import java.util.Map;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.crafting.TcStack;
import taintedmagic.research.TmResearch;

public final class TmRecipeBook {
    private TmRecipeBook() {
    }

    public static void register() {
        TmRecipes.vanilla("ItemShadowmetalPick", "SHADOWMETAL", "shadow_metal_pickaxe", TcStack.of("taintedmagic:shadow_metal_pickaxe"));

        TmRecipes.vanilla("ItemShadowmetalAxe", "SHADOWMETAL", "shadow_metal_axe", TcStack.of("taintedmagic:shadow_metal_axe"));

        TmRecipes.vanilla("ItemShadowmetalSpade", "SHADOWMETAL", "shadow_metal_shovel", TcStack.of("taintedmagic:shadow_metal_shovel"));

        TmRecipes.vanilla("ItemShadowmetalHoe", "SHADOWMETAL", "shadow_metal_hoe", TcStack.of("taintedmagic:shadow_metal_hoe"));

        TmRecipes.vanilla("ItemShadowmetalSword", "SHADOWMETAL", "shadow_metal_sword", TcStack.of("taintedmagic:shadow_metal_sword"));

        TmRecipes.infusion(
            "ItemFocusShockwave",
            "FOCUSSHOCKWAVE",
            TcStack.of("taintedmagic:focus_shockwave"),
            6,
            new AspectList().add(Aspect.AIR, 35).add(Aspect.MOTION, 42).add(Aspect.ENERGY, 42).add(Aspect.MAGIC, 16),
            TmRecipes.item("thaumcraft:focus_shock"),
            TmRecipes.item("minecraft:tnt"),
            TmRecipes.item("minecraft:gunpowder"),
            TmRecipes.item("thaumcraft:air_shard"),
            TmRecipes.item("minecraft:tnt"),
            TmRecipes.item("minecraft:gunpowder"),
            TmRecipes.item("thaumcraft:air_shard")
        );

        TmRecipes.infusion(
            "ItemPrimalBlade",
            "PRIMALBLADE",
            TcStack.of("taintedmagic:primal_blade"),
            8,
            new AspectList().add(Aspect.WEAPON, 65).add(Aspect.ELDRITCH, 46).add(Aspect.DARKNESS, 16).add(Aspect.METAL, 60).add(Aspect.VOID, 56),
            TmRecipes.item("thaumcraft:void_sword"),
            TmRecipes.item("taintedmagic:creation_shard"),
            TmRecipes.item("thaumcraft:air_shard"),
            TmRecipes.item("thaumcraft:fire_shard"),
            TmRecipes.item("thaumcraft:water_shard"),
            TmRecipes.item("thaumcraft:balanced_shard"),
            TmRecipes.item("thaumcraft:earth_shard"),
            TmRecipes.item("thaumcraft:order_shard"),
            TmRecipes.item("thaumcraft:entropy_shard")
        );

        TmRecipes.infusion(
            "ItemFocusMageMace",
            "MACEFOCUS",
            TcStack.of("taintedmagic:focus_mage_mace"),
            3,
            new AspectList().add(Aspect.WEAPON, 32).add(Aspect.METAL, 8).add(Aspect.ENTROPY, 18).add(Aspect.MAGIC, 26),
            TmRecipes.item("minecraft:iron_block"),
            TmRecipes.item("minecraft:iron_sword"),
            TmRecipes.item("minecraft:quartz"),
            TmRecipes.item("thaumcraft:entropy_shard"),
            TmRecipes.item("minecraft:quartz"),
            TmRecipes.item("minecraft:iron_sword"),
            TmRecipes.item("minecraft:quartz"),
            TmRecipes.item("thaumcraft:entropy_shard"),
            TmRecipes.item("minecraft:quartz")
        );

        TmRecipes.infusion(
            "ItemSwordCrimson",
            "CRIMSONBLADE",
            TcStack.of("thaumcraft:crimson_blade"),
            7,
            new AspectList().add(Aspect.WEAPON, 32).add(Aspect.METAL, 16).add(Aspect.ENTROPY, 8).add(Aspect.ELDRITCH, 18).add(Aspect.VOID, 32),
            TmRecipes.item("thaumcraft:void_sword"),
            TmRecipes.item("taintedmagic:crimson_blood"),
            TmRecipes.item("taintedmagic:crimson_blood"),
            TmRecipes.item("taintedmagic:crimson_blood")
        );

        TmRecipes.infusion(
            "ItemVoidwalkerSash",
            "VOIDSASH",
            TcStack.of("taintedmagic:voidwalker_sash"),
            7,
            new AspectList().add(Aspect.VOID, 56).add(Aspect.MAGIC, 40).add(Aspect.ARMOR, 76).add(Aspect.TRAVEL, 16).add(Aspect.FLIGHT, 10),
            TmRecipes.item("thaumcraft:runic_shielding_girdle"),
            TmRecipes.item("taintedmagic:primordial_mote"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_cloth"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_cloth"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot")
        );

        TmRecipes.infusion(
            "ItemMaterial:5",
            "CREATIONSHARD",
            TcStack.of("taintedmagic:creation_shard"),
            8,
            TmResearch.primals(55),
            TmRecipes.item("thaumcraft:balanced_shard"),
            TmRecipes.item("thaumcraft:primordial_pearl"),
            TmRecipes.item("thaumcraft:air_shard"),
            TmRecipes.item("thaumcraft:fire_shard"),
            TmRecipes.item("thaumcraft:water_shard"),
            TmRecipes.item("minecraft:nether_star"),
            TmRecipes.item("thaumcraft:earth_shard"),
            TmRecipes.item("thaumcraft:order_shard"),
            TmRecipes.item("thaumcraft:entropy_shard")
        );

        TmRecipes.infusion(
            "ItemWandRod:0",
            "ROD_warpwood",
            TcStack.of("taintedmagic:warpwood_rod"),
            8,
            new AspectList().add(Aspect.ELDRITCH, 65).add(Aspect.DARKNESS, 30).add(Aspect.TREE, 12).add(Aspect.MAGIC, 45).add(Aspect.AURA, 35),
            TmRecipes.item("taintedmagic:warpwood_log"),
            TmRecipes.item("taintedmagic:primordial_nodule"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:warped_shard"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:zombie_brain"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:warped_shard"),
            TmRecipes.item("thaumcraft:void_metal_ingot")
        );

        TmRecipes.infusion(
            "ItemVoidFortressHelmet:goggles",
            "HELMGOGGLES",
            TcStack.withData("taintedmagic:void_fortress_helmet", 1, Map.of("goggles", "1")),
            5,
            new AspectList().add(Aspect.SENSES, 32).add(Aspect.AURA, 16).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:void_fortress_helmet"),
            TmRecipes.item("minecraft:slime_ball"),
            TmRecipes.item("thaumcraft:goggles_of_revealing")
        );

        TmRecipes.infusion(
            "ItemVoidFortressHelmet:mask0",
            "MASKGRINNINGDEVIL",
            TcStack.withData("taintedmagic:void_fortress_helmet", 1, Map.of("mask", "0")),
            8,
            new AspectList().add(Aspect.MIND, 64).add(Aspect.HEAL, 64).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:void_fortress_helmet"),
            TmRecipes.item("minecraft:ink_sac"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather"),
            TmRecipes.item("thaumcraft:shimmerleaf"),
            TmRecipes.item("thaumcraft:zombie_brain"),
            TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.infusion(
            "ItemVoidFortressHelmet:mask1",
            "MASKANGRYGHOST",
            TcStack.withData("taintedmagic:void_fortress_helmet", 1, Map.of("mask", "1")),
            8,
            new AspectList().add(Aspect.ENTROPY, 64).add(Aspect.DEATH, 64).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:void_fortress_helmet"),
            TmRecipes.item("minecraft:bone_meal"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather"),
            TmRecipes.item("minecraft:poisonous_potato"),
            TmRecipes.item("minecraft:wither_skeleton_skull"),
            TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.infusion(
            "ItemVoidFortressHelmet:mask2",
            "MASKSIPPINGFIEND",
            TcStack.withData("taintedmagic:void_fortress_helmet", 1, Map.of("mask", "2")),
            8,
            new AspectList().add(Aspect.UNDEAD, 64).add(Aspect.LIFE, 64).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:void_fortress_helmet"),
            TmRecipes.item("minecraft:red_dye"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather"),
            TmRecipes.item("minecraft:ghast_tear"),
            TmRecipes.item("minecraft:milk_bucket"),
            TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.infusion(
            "ItemVoidFortressHelmet",
            "VOIDFORTRESS",
            TcStack.of("taintedmagic:void_fortress_helmet"),
            6,
            new AspectList().add(Aspect.METAL, 24).add(Aspect.ARMOR, 16).add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 16).add(Aspect.VOID, 16),
            TmRecipes.item("thaumcraft:void_helmet"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:emerald")
        );

        TmRecipes.infusion(
            "ItemVoidFortressChestplate",
            "VOIDFORTRESS",
            TcStack.of("taintedmagic:void_fortress_chestplate"),
            6,
            new AspectList().add(Aspect.METAL, 24).add(Aspect.ARMOR, 24).add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 16).add(Aspect.VOID, 24),
            TmRecipes.item("thaumcraft:void_chestplate"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:leather")
        );

        TmRecipes.infusion(
            "ItemVoidFortressLeggings",
            "VOIDFORTRESS",
            TcStack.of("taintedmagic:void_fortress_leggings"),
            6,
            new AspectList().add(Aspect.METAL, 24).add(Aspect.ARMOR, 20).add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 16).add(Aspect.VOID, 20),
            TmRecipes.item("thaumcraft:void_leggings"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:leather")
        );

        TmRecipes.infusion(
            "ItemWarpedGoggles",
            "WARPEDGOGGLES",
            TcStack.of("taintedmagic:warped_goggles"),
            3,
            new AspectList().add(Aspect.ELDRITCH, 35).add(Aspect.DARKNESS, 12).add(Aspect.MAGIC, 16).add(Aspect.ARMOR, 8),
            TmRecipes.item("thaumcraft:goggles_of_revealing"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("thaumcraft:quicksilver_drop")
        );

        TmRecipes.infusion(
            "ItemWandCap:0",
            "CAP_shadowmetal",
            TcStack.of("taintedmagic:shadow_metal_wand_cap"),
            8,
            new AspectList().add(Aspect.ELDRITCH, 55).add(Aspect.DARKNESS, 47).add(Aspect.MAGIC, 52).add(Aspect.METAL, 45).add(Aspect.VOID, 55),
            TmRecipes.item("thaumcraft:void_wand_cap"),
            TmRecipes.item("taintedmagic:warped_shard"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:primordial_nodule"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot")
        );

        TmRecipes.infusion(
            "ItemFocusTaintSwarm",
            "TAINTFOCUS",
            TcStack.of("taintedmagic:focus_taint_swarm"),
            4,
            new AspectList().add(Aspect.TAINT, 45).add(Aspect.LIFE, 24).add(Aspect.MOTION, 24).add(Aspect.MAGIC, 16).add(Aspect.DEATH, 37),
            TmRecipes.item("taintedmagic:focus_vis_shard"),
            TmRecipes.item("thaumcraft:tainted_goo"),
            TmRecipes.item("taintedmagic:tainted_shard"),
            TmRecipes.item("thaumcraft:bottled_taint"),
            TmRecipes.item("taintedmagic:tainted_shard"),
            TmRecipes.item("thaumcraft:tainted_goo"),
            TmRecipes.item("taintedmagic:tainted_shard"),
            TmRecipes.item("thaumcraft:bottled_taint"),
            TmRecipes.item("taintedmagic:tainted_shard")
        );

        TmRecipes.infusion(
            "ItemFocusDarkMatter",
            "ELDRITCHFOCUS",
            TcStack.of("taintedmagic:focus_dark_matter"),
            6,
            new AspectList().add(Aspect.ELDRITCH, 64).add(Aspect.DARKNESS, 32).add(Aspect.MAGIC, 32).add(Aspect.DEATH, 32).add(Aspect.VOID, 32),
            TmRecipes.item("thaumcraft:focus_portable_hole"),
            TmRecipes.item("thaumcraft:primordial_pearl"),
            TmRecipes.item("taintedmagic:warped_shard"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:warped_shard"),
            TmRecipes.item("thaumcraft:liquid_death_bucket"),
            TmRecipes.item("taintedmagic:warped_shard"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:warped_shard")
        );

        TmRecipes.infusion(
            "ItemMaterial:7",
            "KNIGHTROBES",
            TcStack.of("taintedmagic:crimson_plating", 9),
            2,
            new AspectList().add(Aspect.HUNGER, 4).add(Aspect.METAL, 8).add(Aspect.MAGIC, 6),
            TmRecipes.item("minecraft:iron_block"),
            TmRecipes.item("taintedmagic:crimson_blood"),
            TmRecipes.item("minecraft:gold_nugget"),
            TmRecipes.item("taintedmagic:crimson_blood"),
            TmRecipes.item("minecraft:gold_nugget"),
            TmRecipes.item("taintedmagic:crimson_blood"),
            TmRecipes.item("minecraft:gold_nugget"),
            TmRecipes.item("taintedmagic:crimson_blood"),
            TmRecipes.item("minecraft:gold_nugget")
        );

        TmRecipes.infusion(
            "ItemVoidwalkerBoots",
            "VOIDWALKERBOOTS",
            TcStack.of("taintedmagic:voidwalker_boots"),
            8,
            new AspectList().add(Aspect.DARKNESS, 42).add(Aspect.VOID, 56).add(Aspect.ELDRITCH, 38).add(Aspect.ARMOR, 60).add(Aspect.TRAVEL, 45),
            TmRecipes.item("thaumcraft:boots_traveller"),
            TmRecipes.item("taintedmagic:primordial_mote"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_cloth"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_cloth"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:eldritch_eye"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_cloth"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_cloth"),
            TmRecipes.item("thaumcraft:void_metal_ingot")
        );

        TmRecipes.infusion(
            "ItemSalis:0",
            "SKYSALT",
            TcStack.of("taintedmagic:salis_tempestas", 3),
            7,
            new AspectList().add(Aspect.AURA, 20).add(Aspect.WEATHER, 30).add(Aspect.WATER, 30),
            TmRecipes.item("thaumcraft:salis_mundus"),
            TmRecipes.item("taintedmagic:creation_fragment"),
            TmRecipes.item("thaumcraft:quicksilver_drop")
        );

        TmRecipes.infusion(
            "ItemSalis:1",
            "TIMESALT",
            TcStack.of("taintedmagic:salis_aevum", 3),
            7,
            new AspectList().add(Aspect.AURA, 20).add(Aspect.LIGHT, 30).add(Aspect.DARKNESS, 30),
            TmRecipes.item("thaumcraft:salis_mundus"),
            TmRecipes.item("taintedmagic:creation_fragment"),
            TmRecipes.item("minecraft:gold_nugget")
        );

        TmRecipes.infusion(
            "ItemThaumicDisassembler",
            "THAUMICDISASSEMBLER",
            TcStack.of("taintedmagic:thaumic_disassembler"),
            3,
            new AspectList().add(Aspect.METAL, 22).add(Aspect.TOOL, 24).add(Aspect.WEAPON, 14).add(Aspect.MECHANISM, 12),
            TmRecipes.item("taintedmagic:thaumic_alloy_plating"),
            TmRecipes.item("taintedmagic:primordial_mote"),
            TmRecipes.item("thaumcraft:void_pickaxe"),
            TmRecipes.item("thaumcraft:thaumium_ingot"),
            TmRecipes.item("thaumcraft:void_shovel"),
            TmRecipes.item("minecraft:diamond"),
            TmRecipes.item("thaumcraft:void_sword"),
            TmRecipes.item("thaumcraft:thaumium_ingot"),
            TmRecipes.item("thaumcraft:void_axe")
        );

        TmRecipes.infusion(
            "ItemShadowFortressHelmet:goggles",
            "HELMGOGGLES",
            TcStack.withData("taintedmagic:shadow_fortress_helmet", 1, Map.of("goggles", "1")),
            5,
            new AspectList().add(Aspect.SENSES, 32).add(Aspect.AURA, 16).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:shadow_fortress_helmet"),
            TmRecipes.item("minecraft:slime_ball"),
            TmRecipes.item("thaumcraft:goggles_of_revealing")
        );

        TmRecipes.infusion(
            "ItemShadowFortressHelmet:mask0",
            "MASKGRINNINGDEVIL",
            TcStack.withData("taintedmagic:shadow_fortress_helmet", 1, Map.of("mask", "0")),
            8,
            new AspectList().add(Aspect.MIND, 64).add(Aspect.HEAL, 64).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:shadow_fortress_helmet"),
            TmRecipes.item("minecraft:ink_sac"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather"),
            TmRecipes.item("thaumcraft:shimmerleaf"),
            TmRecipes.item("thaumcraft:zombie_brain"),
            TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.infusion(
            "ItemShadowFortressHelmet:mask1",
            "MASKANGRYGHOST",
            TcStack.withData("taintedmagic:shadow_fortress_helmet", 1, Map.of("mask", "1")),
            8,
            new AspectList().add(Aspect.ENTROPY, 64).add(Aspect.DEATH, 64).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:shadow_fortress_helmet"),
            TmRecipes.item("minecraft:bone_meal"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather"),
            TmRecipes.item("minecraft:poisonous_potato"),
            TmRecipes.item("minecraft:wither_skeleton_skull"),
            TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.infusion(
            "ItemShadowFortressHelmet:mask2",
            "MASKSIPPINGFIEND",
            TcStack.withData("taintedmagic:shadow_fortress_helmet", 1, Map.of("mask", "2")),
            8,
            new AspectList().add(Aspect.UNDEAD, 64).add(Aspect.LIFE, 64).add(Aspect.ARMOR, 16),
            TmRecipes.item("taintedmagic:shadow_fortress_helmet"),
            TmRecipes.item("minecraft:red_dye"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather"),
            TmRecipes.item("minecraft:ghast_tear"),
            TmRecipes.item("minecraft:milk_bucket"),
            TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.infusion(
            "ItemShadowFortressHelmet",
            "SHADOWFORTRESS",
            TcStack.of("taintedmagic:shadow_fortress_helmet"),
            7,
            new AspectList().add(Aspect.METAL, 28).add(Aspect.ARMOR, 20).add(Aspect.MAGIC, 12).add(Aspect.DARKNESS, 30).add(Aspect.VOID, 22),
            TmRecipes.item("thaumcraft:void_helmet"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:emerald")
        );

        TmRecipes.infusion(
            "ItemShadowFortressChestplate",
            "SHADOWFORTRESS",
            TcStack.of("taintedmagic:shadow_fortress_chestplate"),
            7,
            new AspectList().add(Aspect.METAL, 38).add(Aspect.ARMOR, 26).add(Aspect.MAGIC, 18).add(Aspect.DARKNESS, 34).add(Aspect.VOID, 26),
            TmRecipes.item("thaumcraft:void_chestplate"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather")
        );

        TmRecipes.infusion(
            "ItemShadowFortressLeggings",
            "SHADOWFORTRESS",
            TcStack.of("taintedmagic:shadow_fortress_leggings"),
            7,
            new AspectList().add(Aspect.METAL, 32).add(Aspect.ARMOR, 24).add(Aspect.MAGIC, 16).add(Aspect.DARKNESS, 32).add(Aspect.VOID, 24),
            TmRecipes.item("thaumcraft:void_leggings"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:leather")
        );

        TmRecipes.infusion(
            "ItemKatana:0",
            "THAUMIUMKATANA",
            TcStack.of("taintedmagic:thaumium_katana"),
            3,
            new AspectList().add(Aspect.METAL, 42).add(Aspect.WEAPON, 45).add(Aspect.MAGIC, 22),
            TmRecipes.item("thaumcraft:thaumium_sword"),
            TmRecipes.item("minecraft:obsidian"),
            TmRecipes.item("thaumcraft:thaumium_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:emerald"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("thaumcraft:thaumium_ingot"),
            TmRecipes.item("minecraft:obsidian"),
            TmRecipes.item("thaumcraft:thaumium_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:emerald"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("thaumcraft:thaumium_ingot")
        );

        TmRecipes.infusion(
            "ItemKatana:1",
            "VOIDMETALKATANA",
            TcStack.of("taintedmagic:void_metal_katana"),
            6,
            new AspectList().add(Aspect.METAL, 52).add(Aspect.WEAPON, 55).add(Aspect.MAGIC, 32).add(Aspect.VOID, 46).add(Aspect.ELDRITCH, 12),
            TmRecipes.item("thaumcraft:void_sword"),
            TmRecipes.item("minecraft:obsidian"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:emerald"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("minecraft:obsidian"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("minecraft:emerald"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("thaumcraft:void_metal_ingot")
        );

        TmRecipes.infusion(
            "ItemKatana:2",
            "SHADOWMETALKATANA",
            TcStack.of("taintedmagic:shadow_metal_katana"),
            7,
            new AspectList().add(Aspect.METAL, 62).add(Aspect.WEAPON, 65).add(Aspect.MAGIC, 42).add(Aspect.VOID, 16).add(Aspect.DARKNESS, 46),
            TmRecipes.item("taintedmagic:shadow_metal_sword"),
            TmRecipes.item("minecraft:obsidian"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:emerald"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:obsidian"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:emerald"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("taintedmagic:shadow_metal_ingot")
        );

        TmRecipes.infusion(
            "ItemFlyteCharm",
            "FLYTECHARM",
            TcStack.of("taintedmagic:flyte_charm"),
            7,
            new AspectList().add(Aspect.FLIGHT, 86).add(Aspect.AURA, 56).add(Aspect.SENSES, 25).add(Aspect.MAGIC, 48).add(Aspect.ENERGY, 65),
            TmRecipes.item("thaumcraft:air_arrow"),
            TmRecipes.item("taintedmagic:creation_shard"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("thaumcraft:air_shard"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("minecraft:feather"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("thaumcraft:air_shard"),
            TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.infusion(
            "ItemGateKey",
            "GATEKEY",
            TcStack.of("taintedmagic:gate_key"),
            7,
            new AspectList().add(Aspect.MOTION, 40).add(Aspect.AURA, 20).add(Aspect.TRAVEL, 25).add(Aspect.MAGIC, 30),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("taintedmagic:creation_fragment"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("thaumcraft:balanced_shard"),
            TmRecipes.item("minecraft:ender_pearl"),
            TmRecipes.item("minecraft:feather"),
            TmRecipes.item("minecraft:iron_ingot"),
            TmRecipes.item("thaumcraft:balanced_shard"),
            TmRecipes.item("minecraft:ender_pearl")
        );

        TmRecipes.infusion(
            "ItemVoidmetalGoggles",
            "VOIDGOGGLES",
            TcStack.of("taintedmagic:void_metal_goggles"),
            5,
            new AspectList().add(Aspect.VOID, 40).add(Aspect.SENSES, 35).add(Aspect.ARMOR, 20),
            TmRecipes.item("taintedmagic:warped_goggles"),
            TmRecipes.item("thaumcraft:void_metal_ingot"),
            TmRecipes.item("thaumcraft:quicksilver_drop")
        );

        TmRecipes.infusion(
            "ItemLumosRing",
            "LUMOSRING",
            TcStack.of("taintedmagic:lumos_ring"),
            1,
            new AspectList().add(Aspect.LIGHT, 35).add(Aspect.SENSES, 25).add(Aspect.AURA, 10),
            TmRecipes.item("thaumcraft:mundane_ring"),
            TmRecipes.item("taintedmagic:focus_lumos"),
            TmRecipes.item("thaumcraft:amber"),
            TmRecipes.item("minecraft:gold_ingot"),
            TmRecipes.item("thaumcraft:salis_mundus"),
            TmRecipes.item("thaumcraft:amber"),
            TmRecipes.item("minecraft:gold_ingot")
        );

        TmRecipes.infusion(
            "ItemKatanaThaumium:inscription0",
            "INSCRIPTIONFIRE",
            TcStack.withData("taintedmagic:thaumium_katana", 1, Map.of("inscription", "0")),
            8,
            new AspectList().add(Aspect.FIRE, 64).add(Aspect.ENERGY, 64).add(Aspect.WEAPON, 16),
            TmRecipes.item("taintedmagic:thaumium_katana"),
            TmRecipes.item("thaumcraft:focus_fire"),
            TmRecipes.item("minecraft:coal"),
            TmRecipes.item("minecraft:netherrack"),
            TmRecipes.item("minecraft:fire_charge"),
            TmRecipes.item("minecraft:blaze_powder"),
            TmRecipes.item("thaumcraft:nitor")
        );

        TmRecipes.infusion(
            "ItemKatanaThaumium:inscription1",
            "INSCRIPTIONTHUNDER",
            TcStack.withData("taintedmagic:thaumium_katana", 1, Map.of("inscription", "1")),
            8,
            new AspectList().add(Aspect.AIR, 64).add(Aspect.WEATHER, 64).add(Aspect.WEAPON, 16),
            TmRecipes.item("taintedmagic:thaumium_katana"),
            TmRecipes.item("taintedmagic:focus_shockwave"),
            TmRecipes.item("thaumcraft:air_shard"),
            TmRecipes.item("minecraft:feather"),
            TmRecipes.item("minecraft:tnt"),
            TmRecipes.item("thaumcraft:entropy_shard"),
            TmRecipes.item("minecraft:gunpowder")
        );

        TmRecipes.infusion(
            "ItemKatanaThaumium:inscription2",
            "INSCRIPTIONHEAL",
            TcStack.withData("taintedmagic:thaumium_katana", 1, Map.of("inscription", "2")),
            8,
            new AspectList().add(Aspect.HEAL, 64).add(Aspect.UNDEAD, 64).add(Aspect.WEAPON, 16),
            TmRecipes.item("taintedmagic:thaumium_katana"),
            TmRecipes.item("thaumcraft:focus_pech"),
            TmRecipes.item("minecraft:bone"),
            TmRecipes.item("minecraft:rotten_flesh"),
            TmRecipes.item("minecraft:fermented_spider_eye"),
            TmRecipes.item("thaumcraft:zombie_brain"),
            TmRecipes.item("thaumcraft:bath_salts")
        );

        TmRecipes.infusion(
            "ItemKatana:inscription0",
            "INSCRIPTIONFIRE",
            TcStack.withData("taintedmagic:thaumium_katana", 1, Map.of("inscription", "0")),
            8,
            new AspectList().add(Aspect.FIRE, 64).add(Aspect.ENERGY, 64).add(Aspect.WEAPON, 16),
            TmRecipes.any("taintedmagic:thaumium_katana", "taintedmagic:void_metal_katana", "taintedmagic:shadow_metal_katana"),
            TmRecipes.item("thaumcraft:focus_fire"),
            TmRecipes.item("minecraft:coal"),
            TmRecipes.item("minecraft:netherrack"),
            TmRecipes.item("minecraft:fire_charge"),
            TmRecipes.item("minecraft:blaze_powder"),
            TmRecipes.item("thaumcraft:nitor")
        );

        TmRecipes.infusion(
            "ItemKatana:inscription1",
            "INSCRIPTIONTHUNDER",
            TcStack.withData("taintedmagic:thaumium_katana", 1, Map.of("inscription", "1")),
            8,
            new AspectList().add(Aspect.AIR, 64).add(Aspect.WEATHER, 64).add(Aspect.WEAPON, 16),
            TmRecipes.any("taintedmagic:thaumium_katana", "taintedmagic:void_metal_katana", "taintedmagic:shadow_metal_katana"),
            TmRecipes.item("taintedmagic:focus_shockwave"),
            TmRecipes.item("thaumcraft:air_shard"),
            TmRecipes.item("minecraft:feather"),
            TmRecipes.item("minecraft:tnt"),
            TmRecipes.item("thaumcraft:entropy_shard"),
            TmRecipes.item("minecraft:gunpowder")
        );

        TmRecipes.infusion(
            "ItemKatana:inscription2",
            "INSCRIPTIONHEAL",
            TcStack.withData("taintedmagic:thaumium_katana", 1, Map.of("inscription", "2")),
            8,
            new AspectList().add(Aspect.HEAL, 64).add(Aspect.UNDEAD, 64).add(Aspect.WEAPON, 16),
            TmRecipes.any("taintedmagic:thaumium_katana", "taintedmagic:void_metal_katana", "taintedmagic:shadow_metal_katana"),
            TmRecipes.item("thaumcraft:focus_pech"),
            TmRecipes.item("minecraft:bone"),
            TmRecipes.item("minecraft:rotten_flesh"),
            TmRecipes.item("minecraft:fermented_spider_eye"),
            TmRecipes.item("thaumcraft:zombie_brain"),
            TmRecipes.item("thaumcraft:bath_salts")
        );

        TmRecipes.arcaneShapeless(
            "ItemMaterial:2",
            "CRIMSONROBES",
            TcStack.of("taintedmagic:crimson_cloth"),
            new AspectList().add(Aspect.FIRE, 5).add(Aspect.ENTROPY, 5),
            TmRecipes.item("taintedmagic:crimson_blood"),
            TmRecipes.item("thaumcraft:enchanted_fabric"),
            TmRecipes.item("minecraft:gold_nugget")
        );

        TmRecipes.arcaneShapeless(
            "ItemWarpFertilizer",
            "WARPTREE",
            TcStack.of("taintedmagic:warp_fertilizer"),
            new AspectList().add(Aspect.ENTROPY, 150),
            TmRecipes.item("thaumcraft:zombie_brain"),
            TmRecipes.item("taintedmagic:warped_shard"),
            TmRecipes.item("thaumcraft:wisp_essence"),
            TmRecipes.item("minecraft:bone_meal")
        );

        TmRecipes.arcane(
            "ItemMaterial:6",
            "THAUMICDISASSEMBLER",
            TcStack.of("taintedmagic:thaumic_alloy_plating"),
            new AspectList().add(Aspect.ORDER, 50).add(Aspect.ENTROPY, 50),
            new String[] {"BAB", "A A", "BAB"},
            'A', TmRecipes.item("thaumcraft:thaumium_ingot"),
            'B', TmRecipes.item("thaumcraft:void_metal_ingot")
        );

        TmRecipes.arcane(
            "ItemHelmetCultistRobe",
            "CRIMSONROBES",
            TcStack.of("thaumcraft:crimson_cult_hood"),
            new AspectList().add(Aspect.FIRE, 5).add(Aspect.ENTROPY, 5),
            new String[] {"AAA", "A A"},
            'A', TmRecipes.item("taintedmagic:crimson_cloth")
        );

        TmRecipes.arcane(
            "ItemChestCultistRobe",
            "CRIMSONROBES",
            TcStack.of("thaumcraft:crimson_cult_robe"),
            new AspectList().add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10),
            new String[] {"A A", "CAC", "ABA"},
            'A', TmRecipes.item("taintedmagic:crimson_cloth"),
            'B', TmRecipes.item("minecraft:gold_ingot"),
            'C', TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.arcane(
            "ItemLegsCultistRobe",
            "CRIMSONROBES",
            TcStack.of("thaumcraft:crimson_cult_leggings"),
            new AspectList().add(Aspect.FIRE, 8).add(Aspect.ENTROPY, 8),
            new String[] {"ABA", "ACA", "A A"},
            'A', TmRecipes.item("taintedmagic:crimson_cloth"),
            'B', TmRecipes.item("minecraft:gold_ingot"),
            'C', TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.arcane(
            "ItemBootsCultist",
            "CRIMSONROBES",
            TcStack.of("thaumcraft:crimson_cult_boots"),
            new AspectList().add(Aspect.FIRE, 5).add(Aspect.ENTROPY, 5),
            new String[] {"A A", "A A"},
            'A', TmRecipes.item("taintedmagic:crimson_cloth")
        );

        TmRecipes.arcane(
            "ItemWandRod:1",
            "ROD_warpwood_staff",
            TcStack.of("taintedmagic:warpwood_staff_core"),
            TmResearch.primals(120),
            new String[] {"  A", " B ", "B  "},
            'A', TmRecipes.item("thaumcraft:primordial_pearl"),
            'B', TmRecipes.item("taintedmagic:warpwood_rod")
        );

        TmRecipes.arcane(
            "ItemHelmetCultistPlate",
            "KNIGHTROBES",
            TcStack.of("thaumcraft:crimson_cult_helm"),
            new AspectList().add(Aspect.EARTH, 5).add(Aspect.FIRE, 5).add(Aspect.ENTROPY, 5),
            new String[] {"ABA", "C C"},
            'A', TmRecipes.item("taintedmagic:crimson_plating"),
            'B', TmRecipes.item("minecraft:gold_ingot"),
            'C', TmRecipes.item("minecraft:iron_ingot")
        );

        TmRecipes.arcane(
            "ItemChestCultistPlate",
            "KNIGHTROBES",
            TcStack.of("thaumcraft:crimson_cult_chestplate"),
            new AspectList().add(Aspect.EARTH, 10).add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10),
            new String[] {"A A", "ABA", "BBB"},
            'A', TmRecipes.item("minecraft:iron_ingot"),
            'B', TmRecipes.item("taintedmagic:crimson_plating")
        );

        TmRecipes.arcane(
            "ItemLegsCultistPlate",
            "KNIGHTROBES",
            TcStack.of("thaumcraft:crimson_cult_greaves"),
            new AspectList().add(Aspect.EARTH, 8).add(Aspect.FIRE, 8).add(Aspect.ENTROPY, 8),
            new String[] {"ABA", "ACA", " C "},
            'A', TmRecipes.item("minecraft:iron_ingot"),
            'B', TmRecipes.item("minecraft:gold_ingot"),
            'C', TmRecipes.item("taintedmagic:crimson_plating")
        );

        TmRecipes.arcane(
            "ItemHelmetCultistLeaderPlate",
            "PRAETORARMOR",
            TcStack.of("thaumcraft:crimson_praetor_helm"),
            new AspectList().add(Aspect.ORDER, 15).add(Aspect.ENTROPY, 15).add(Aspect.FIRE, 15),
            new String[] {"BCB", "DAD", "BCB"},
            'A', TmRecipes.item("thaumcraft:crimson_cult_helm"),
            'B', TmRecipes.item("minecraft:iron_ingot"),
            'C', TmRecipes.item("taintedmagic:crimson_plating"),
            'D', TmRecipes.item("minecraft:gold_ingot")
        );

        TmRecipes.arcane(
            "ItemChestCultistLeaderPlate",
            "PRAETORARMOR",
            TcStack.of("thaumcraft:crimson_praetor_chestplate"),
            new AspectList().add(Aspect.ORDER, 25).add(Aspect.ENTROPY, 25).add(Aspect.FIRE, 25),
            new String[] {"BBB", "CAC", "CDC"},
            'A', TmRecipes.item("thaumcraft:crimson_cult_chestplate"),
            'B', TmRecipes.item("minecraft:iron_ingot"),
            'C', TmRecipes.item("taintedmagic:crimson_plating"),
            'D', TmRecipes.item("minecraft:gold_ingot")
        );

        TmRecipes.arcane(
            "ItemLegsCultistLeaderPlate",
            "PRAETORARMOR",
            TcStack.of("thaumcraft:crimson_praetor_greaves"),
            new AspectList().add(Aspect.ORDER, 20).add(Aspect.ENTROPY, 20).add(Aspect.FIRE, 20),
            new String[] {"CDC", "BAB", "BCB"},
            'A', TmRecipes.item("thaumcraft:crimson_cult_greaves"),
            'B', TmRecipes.item("taintedmagic:crimson_plating"),
            'C', TmRecipes.item("minecraft:iron_ingot"),
            'D', TmRecipes.item("minecraft:gold_ingot")
        );

        TmRecipes.arcane(
            "ItemMaterial:1",
            "SHADOWMETAL",
            TcStack.of("taintedmagic:shadow_cloth"),
            new AspectList().add(Aspect.ORDER, 10).add(Aspect.ENTROPY, 10),
            new String[] {" A ", "ABA", " A "},
            'A', TmRecipes.item("taintedmagic:shadow_metal_nugget"),
            'B', TmRecipes.item("thaumcraft:enchanted_fabric")
        );

        TmRecipes.arcaneShapeless(
            "ItemMagicFunguar",
            "MAGICFUNGUAR",
            TcStack.of("taintedmagic:magic_funguar"),
            TmResearch.primals(1),
            TmRecipes.item("thaumcraft:vishroom"),
            TmRecipes.any("thaumcraft:air_shard", "thaumcraft:fire_shard", "thaumcraft:water_shard", "thaumcraft:earth_shard", "thaumcraft:order_shard", "thaumcraft:entropy_shard", "thaumcraft:balanced_shard"),
            TmRecipes.item("minecraft:glowstone_dust"),
            TmRecipes.item("minecraft:redstone")
        );

        TmRecipes.arcane(
            "ItemHollowDagger",
            "HOLLOWDAGGER",
            TcStack.of("taintedmagic:hollow_dagger"),
            new AspectList().add(Aspect.ENTROPY, 85),
            new String[] {"  A", " BD", "C  "},
            'A', TmRecipes.item("thaumcraft:bone_rod"),
            'B', TmRecipes.item("thaumcraft:greatwood_log"),
            'C', TmRecipes.item("minecraft:stick"),
            'D', TmRecipes.item("minecraft:iron_nugget")
        );

        TmRecipes.arcane(
            "ItemWandCap:1",
            "CAP_cloth",
            TcStack.of("taintedmagic:cloth_wand_cap"),
            new AspectList().add(Aspect.EARTH, 10).add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10).add(Aspect.ORDER, 10),
            new String[] {"AAA", "A A"},
            'A', TmRecipes.item("thaumcraft:enchanted_fabric")
        );

        TmRecipes.arcane(
            "ItemWandCap:2",
            "CAP_crimsoncloth",
            TcStack.of("taintedmagic:crimson_cloth_wand_cap"),
            new AspectList().add(Aspect.FIRE, 25).add(Aspect.ENTROPY, 25).add(Aspect.ORDER, 25),
            new String[] {"AAA", "ABA"},
            'A', TmRecipes.item("taintedmagic:crimson_cloth"),
            'B', TmRecipes.item("thaumcraft:salis_mundus")
        );

        TmRecipes.arcane(
            "ItemWandCap:3",
            "CAP_shadowcloth",
            TcStack.of("taintedmagic:shadow_cloth_wand_cap"),
            new AspectList().add(Aspect.EARTH, 55).add(Aspect.FIRE, 55).add(Aspect.ENTROPY, 55).add(Aspect.ORDER, 55),
            new String[] {"AAA", "ABA"},
            'A', TmRecipes.item("taintedmagic:shadow_cloth"),
            'B', TmRecipes.item("thaumcraft:salis_mundus")
        );

        TmRecipes.arcane(
            "ItemFocusVisShard",
            "FOCUSSHARD",
            TcStack.of("taintedmagic:focus_vis_shard"),
            new AspectList().add(Aspect.AIR, 46).add(Aspect.ENTROPY, 38).add(Aspect.ORDER, 22),
            new String[] {"BDC", "DAD", "CDB"},
            'A', TmRecipes.item("thaumcraft:quicksilver"),
            'B', TmRecipes.item("thaumcraft:balanced_shard"),
            'C', TmRecipes.item("taintedmagic:tainted_shard"),
            'D', TmRecipes.item("thaumcraft:wisp_essence")
        );

        TmRecipes.arcane(
            "ItemFocusLumos",
            "FOCUSLUMOS",
            TcStack.of("taintedmagic:focus_lumos"),
            new AspectList().add(Aspect.AIR, 52).add(Aspect.FIRE, 56).add(Aspect.ORDER, 28),
            new String[] {"BDC", "DAD", "CDB"},
            'A', TmRecipes.item("thaumcraft:quicksilver"),
            'B', TmRecipes.item("thaumcraft:nitor"),
            'C', TmRecipes.item("thaumcraft:balanced_shard"),
            'D', TmRecipes.item("minecraft:glowstone_dust")
        );

        TmRecipes.arcaneShapeless(
            "ItemMaterial:9",
            "BREAKPEARL",
            TcStack.of("taintedmagic:primordial_nodule", 3),
            new AspectList().add(Aspect.ENTROPY, 25),
            TmRecipes.item("thaumcraft:primordial_pearl")
        );

        TmRecipes.arcaneShapeless(
            "ItemMaterial:10",
            "BREAKPEARL",
            TcStack.of("taintedmagic:primordial_mote", 3),
            new AspectList().add(Aspect.ENTROPY, 15),
            TmRecipes.item("taintedmagic:primordial_nodule")
        );

        TmRecipes.arcaneShapeless(
            "ItemMaterial:11",
            "CREATIONSHARD",
            TcStack.of("taintedmagic:creation_fragment", 9),
            new AspectList().add(Aspect.ENTROPY, 99),
            TmRecipes.item("taintedmagic:creation_shard")
        );

        TmRecipes.arcane(
            "ItemVoidBlood",
            "VOIDBLOOD",
            TcStack.of("taintedmagic:void_blood"),
            new AspectList().add(Aspect.ORDER, 35).add(Aspect.ENTROPY, 25),
            new String[] {" B ", "CAC", " D "},
            'A', TmRecipes.item("taintedmagic:crimson_blood"),
            'B', TmRecipes.item("taintedmagic:primordial_mote"),
            'C', TmRecipes.item("thaumcraft:void_seed"),
            'D', TmRecipes.item("thaumcraft:wisp_essence")
        );

        TmRecipes.crucible("ItemMaterial:3", "UNBALANCEDSHARDS", TcStack.of("taintedmagic:warped_shard"), TmRecipes.item("thaumcraft:balanced_shard"), new AspectList().add(Aspect.ELDRITCH, 4));

        TmRecipes.crucible("ItemMaterial:4", "UNBALANCEDSHARDS", TcStack.of("taintedmagic:tainted_shard"), TmRecipes.item("thaumcraft:balanced_shard"), new AspectList().add(Aspect.TAINT, 4));

        TmRecipes.crucible("ItemMaterial:0", "SHADOWMETAL", TcStack.of("taintedmagic:shadow_metal_ingot"), TmRecipes.item("minecraft:iron_ingot"), new AspectList().add(Aspect.DARKNESS, 3).add(Aspect.METAL, 7).add(Aspect.MAGIC, 2));

        TmRecipes.crucible("Vishroom_brown", "VISHROOMCRAFT", TcStack.of("thaumcraft:vishroom"), TmRecipes.item("minecraft:brown_mushroom"), new AspectList().add(Aspect.MAGIC, 3).add(Aspect.POISON, 1));

        TmRecipes.crucible("Vishroom_red", "VISHROOMCRAFT", TcStack.of("thaumcraft:vishroom"), TmRecipes.item("minecraft:red_mushroom"), new AspectList().add(Aspect.MAGIC, 3).add(Aspect.POISON, 1));
    }
}
