package taintedmagic.research;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.crafting.TcStack;
import taintedmagic.TaintedMagic;

public final class TmResearchTree {
    private TmResearchTree() {
    }

    public static void register() {
        TmResearch.link("TMELDRITCHMAJOR", "ELDRITCHMAJOR", "ELDRITCH", 7, -7, ResourceLocation.parse("thaumcraft:textures/misc/r_eldritchmajor.png"))
            .setRound()
            .setSpecial()
            .setConcealed()
            .registerResearchItem();

        TmResearch.item("TAINTEDMAGIC", new AspectList(), 2, -1, TcStack.of("thaumcraft:jar_label"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.text("2"))
            .setRound()
            .setAutoUnlock()
            .registerResearchItem();

        TmResearch.item("SHADOWMETAL", new AspectList().add(Aspect.METAL, 1).add(Aspect.DARKNESS, 1).add(Aspect.MAGIC, 1), 0, 1, TcStack.of("taintedmagic:shadow_metal_ingot"), 1, 1)
            .setPages(TmResearch.text("1"), TmResearch.crucible("ItemMaterial:0"), TmResearch.recipe("ItemShadowmetalPick"), TmResearch.recipe("ItemShadowmetalSpade"), TmResearch.recipe("ItemShadowmetalAxe"), TmResearch.recipe("ItemShadowmetalHoe"), TmResearch.recipe("ItemShadowmetalSword"))
            .setConcealed()
            .setParents("TAINTEDMAGIC")
            .registerResearchItem();

        TmResearch.item("CAP_shadowmetal", new AspectList().add(Aspect.METAL, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4).add(Aspect.MAGIC, 4), -3, 2, TcStack.of("taintedmagic:shadow_metal_wand_cap"), 2, 4)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemWandCap:0"))
            .setConcealed()
            .setParents("SHADOWMETAL", "CAP_void", "PRIMPEARL")
            .setParentsHidden("INFUSION")
            .registerResearchItem();

        TmResearch.item("SHADOWCLOTH", new AspectList().add(Aspect.CLOTH, 1).add(Aspect.DARKNESS, 1), -2, 4, TcStack.of("taintedmagic:shadow_cloth"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemMaterial:1"))
            .setSecondary()
            .setConcealed()
            .setParents("ENCHFABRIC", "SHADOWMETAL")
            .registerResearchItem();

        TmResearch.item("ROD_warpwood", new AspectList().add(Aspect.TREE, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4), 8, 0, TcStack.of("taintedmagic:warpwood_rod"), 3, 5)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemWandRod:0"))
            .setConcealed()
            .setParents("WARPTREE", "VOIDMETAL", "PRIMPEARL", "ROD_primal_staff")
            .setParentsHidden("SHADOWMETAL", "INFUSION")
            .registerResearchItem();

        TmResearch.item("ROD_warpwood_staff", new AspectList().add(Aspect.TREE, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4), 10, -1, TcStack.of("taintedmagic:warpwood_staff_core"), 2, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemWandRod:1"))
            .setSecondary()
            .setConcealed()
            .setParents("ROD_warpwood")
            .setParentsHidden("CREATIONSHARD")
            .registerResearchItem();

        TmResearch.item("UNBALANCEDSHARDS", new AspectList().add(Aspect.CRYSTAL, 4).add(Aspect.DARKNESS, 4), 4, -1, TcStack.of("taintedmagic:tainted_shard"), 1, 1)
            .setPages(TmResearch.text("1"), TmResearch.crucible("ItemMaterial:4"), TmResearch.crucible("ItemMaterial:3"))
            .setSecondary()
            .setConcealed()
            .setParents("TAINTEDMAGIC")
            .registerResearchItem();

        TmResearch.item("WARPTREE", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.DARKNESS, 4).add(Aspect.TREE, 4), 6, -2, TcStack.of("taintedmagic:warp_fertilizer"), 3, 2)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemWarpFertilizer"))
            .setConcealed()
            .setParents("UNBALANCEDSHARDS")
            .registerResearchItem();

        TmResearch.item("CRIMSONROBES", new AspectList().add(Aspect.CLOTH, 4).add(Aspect.EXCHANGE, 4).add(Aspect.ARMOR, 4), -2, -1, TcStack.of("taintedmagic:crimson_cloth"), 2, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemMaterial:2"), TmResearch.arcane("ItemHelmetCultistRobe"), TmResearch.arcane("ItemChestCultistRobe"), TmResearch.arcane("ItemLegsCultistRobe"), TmResearch.arcane("ItemBootsCultist"))
            .setConcealed()
            .setParents("HOLLOWDAGGER")
            .registerResearchItem();

        TmResearch.item("VOIDFORTRESS", new AspectList().add(Aspect.ARMOR, 5).add(Aspect.ELDRITCH, 3).add(Aspect.DARKNESS, 3).add(Aspect.VOID, 5), 8, -9, TcStack.of("taintedmagic:void_fortress_helmet"), 2, 2)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemVoidFortressHelmet"), TmResearch.infusion("ItemVoidFortressChestplate"), TmResearch.infusion("ItemVoidFortressLeggings"))
            .setConcealed()
            .setParents("TMELDRITCHMAJOR")
            .setParentsHidden("ARMORVOIDFORTRESS", "ARMORFORTRESS", "INFUSION")
            .registerResearchItem();

        TmResearch.item("WARPEDGOGGLES", new AspectList().add(Aspect.ARMOR, 8).add(Aspect.ELDRITCH, 4).add(Aspect.DARKNESS, 4), 2, 4, TcStack.of("taintedmagic:warped_goggles"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemWarpedGoggles"))
            .setSecondary()
            .setConcealed()
            .setParents("SHADOWMETAL")
            .setParentsHidden("TAINTEDMAGIC", "GOGGLES", "INFUSION")
            .registerResearchItem();

        TmResearch.item("KNIGHTROBES", new AspectList().add(Aspect.CLOTH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4), -4, -2, TcStack.of("taintedmagic:crimson_plating"), 2, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemMaterial:7"), TmResearch.arcane("ItemHelmetCultistPlate"), TmResearch.arcane("ItemChestCultistPlate"), TmResearch.arcane("ItemLegsCultistPlate"))
            .setConcealed()
            .setParents("CRIMSONROBES")
            .setParentsHidden("ELDRITCHMINOR", "INFUSION")
            .registerResearchItem();

        TmResearch.item("PRAETORARMOR", new AspectList().add(Aspect.CLOTH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4).add(Aspect.ELDRITCH, 4), -6, 0, TcStack.of("thaumcraft:crimson_praetor_helm"), 2, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemHelmetCultistLeaderPlate"), TmResearch.arcane("ItemChestCultistLeaderPlate"), TmResearch.arcane("ItemLegsCultistLeaderPlate"))
            .setConcealed()
            .setParents("KNIGHTROBES")
            .setParentsHidden("CRIMSONROBES", "TMELDRITCHMAJOR", "VOIDMETAL")
            .registerResearchItem();

        TmResearch.item("VOIDBLOOD", new AspectList().add(Aspect.VOID, 14).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 18).add(Aspect.AURA, 4), -2, -5, TcStack.of("taintedmagic:void_blood"), 1, 3)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemVoidBlood"))
            .setSecondary()
            .setConcealed()
            .setParents("HOLLOWDAGGER")
            .setParentsHidden("ELDRITCHMAJOR", "BREAKPEARL")
            .registerResearchItem();

        TmResearch.item("VOIDWALKERBOOTS", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 8).add(Aspect.ELDRITCH, 8), 4, -8, TcStack.of("taintedmagic:voidwalker_boots"), 3, 4)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemVoidwalkerBoots"))
            .setConcealed()
            .setParents("TMELDRITCHMAJOR")
            .setParentsHidden("PRIMPEARL", "BOOTSTRAVELLER", "SHADOWCLOTH", "ARMORVOIDFORTRESS", "INFUSION")
            .registerResearchItem();

        TmResearch.item("BREAKPEARL", new AspectList().add(Aspect.FIRE, 4).add(Aspect.WATER, 4).add(Aspect.EARTH, 4).add(Aspect.AIR, 4).add(Aspect.ORDER, 4).add(Aspect.ENTROPY, 16), 8, -6, TcStack.of("taintedmagic:primordial_nodule"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemMaterial:9"), TmResearch.arcane("ItemMaterial:10"))
            .setSecondary()
            .setConcealed()
            .setParents("PRIMPEARL")
            .registerResearchItem();

        TmResearch.item("CREATIONSHARD", TmResearch.primals(16), 9, -7, TcStack.of("taintedmagic:creation_shard"), 3, 9)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemMaterial:5"), TmResearch.arcane("ItemMaterial:11"))
            .setSpecial()
            .setConcealed()
            .setParents("TMELDRITCHMAJOR")
            .setParentsHidden("PRIMPEARL", "INFUSION")
            .registerResearchItem();

        TmResearch.item("CREATION", new AspectList(), 11, -5, TaintedMagic.id("textures/misc/r_creation.png"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.text("2"), TmResearch.text("3"), TmResearch.text("4"), TmResearch.text("5"), TmResearch.text("6"))
            .setRound()
            .setSpecial()
            .setConcealed()
            .setHidden()
            .setParents("CREATIONSHARD")
            .registerResearchItem();

        TmResearch.item("SKYSALT", TmResearch.primals(4).add(Aspect.WEATHER, 4), 12, -7, TcStack.of("taintedmagic:salis_tempestas"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemSalis:0"))
            .setSecondary()
            .setConcealed()
            .setParents("CREATION")
            .setParentsHidden("INFUSION")
            .registerResearchItem();

        TmResearch.item("TIMESALT", TmResearch.primals(4).add(Aspect.EXCHANGE, 4), 13, -6, TcStack.of("taintedmagic:salis_aevum"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemSalis:1"))
            .setSecondary()
            .setConcealed()
            .setParents("CREATION")
            .setParentsHidden("INFUSION")
            .registerResearchItem();

        TmResearch.item("THAUMICDISASSEMBLER", new AspectList().add(Aspect.METAL, 4).add(Aspect.WEAPON, 8).add(Aspect.TOOL, 4), 6, -9, TcStack.of("taintedmagic:thaumic_disassembler"), 3, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemThaumicDisassembler"), TmResearch.arcane("ItemMaterial:6"))
            .setConcealed()
            .setParents("TMELDRITCHMAJOR")
            .setParentsHidden("THAUMIUM", "VOIDMETAL", "PRIMPEARL", "INFUSION")
            .registerResearchItem();

        TmResearch.item("VOIDSASH", new AspectList().add(Aspect.VOID, 4).add(Aspect.METAL, 8).add(Aspect.ARMOR, 4), 3, -9, TcStack.of("taintedmagic:voidwalker_sash"), 1, 3)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemVoidwalkerSash"))
            .setSecondary()
            .setConcealed()
            .setParents("VOIDWALKERBOOTS")
            .setParentsHidden("PRIMPEARL", "INFUSION")
            .registerResearchItem();

        TmResearch.item("MAGICFUNGUAR", new AspectList().add(Aspect.MAGIC, 2).add(Aspect.HUNGER, 4).add(Aspect.PLANT, 2), 2, -4, TcStack.of("taintedmagic:magic_funguar"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemMagicFunguar"))
            .setSecondary()
            .setConcealed()
            .setParents("VISHROOMCRAFT")
            .registerResearchItem();

        TmResearch.item("VISHROOMCRAFT", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.CRAFT, 2).add(Aspect.PLANT, 3), 3, -3, TcStack.of("thaumcraft:vishroom"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.crucible("Vishroom_red"), TmResearch.crucible("Vishroom_brown"))
            .setSecondary()
            .setConcealed()
            .setHidden()
            .setItemTriggers("thaumcraft:vishroom")
            .registerResearchItem();

        TmResearch.item("HOLLOWDAGGER", new AspectList().add(Aspect.WEAPON, 4).add(Aspect.FIRE, 4).add(Aspect.HEAL, 4), 0, -3, TcStack.of("taintedmagic:hollow_dagger"), 2, 2)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemHollowDagger"))
            .setParents("TAINTEDMAGIC")
            .setParentsHidden("ENCHFABRIC", "ESSENTIACRYSTAL", "ELDRITCHMINOR")
            .registerResearchItem();

        TmResearch.item("CRIMSONBLADE", new AspectList().add(Aspect.ENTROPY, 8).add(Aspect.ELDRITCH, 2).add(Aspect.WEAPON, 16).add(Aspect.VOID, 6), -6, 2, TcStack.of("thaumcraft:crimson_blade"), 1, 5)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemSwordCrimson"))
            .setSecondary()
            .setConcealed()
            .setParents("PRAETORARMOR")
            .setParentsHidden("INFUSION")
            .registerResearchItem();

        TmResearch.item("SHADOWFORTRESS", new AspectList().add(Aspect.METAL, 4).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 8).add(Aspect.VOID, 2), 0, 3, TcStack.of("taintedmagic:shadow_fortress_helmet"), 3, 3)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemShadowFortressHelmet"), TmResearch.infusion("ItemShadowFortressChestplate"), TmResearch.infusion("ItemShadowFortressLeggings"))
            .setConcealed()
            .setParents("SHADOWMETAL")
            .setParentsHidden("VOIDFORTRESS", "TMELDRITCHMAJOR", "UNBALANCEDSHARDS", "INFUSION")
            .registerResearchItem();

        TmResearch.item("CAP_cloth", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.CLOTH, 4), 3, 1, TcStack.of("taintedmagic:cloth_wand_cap"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemWandCap:1"))
            .setSecondary()
            .setConcealed()
            .setParentsHidden("CAP_gold", "ENCHFABRIC")
            .registerResearchItem();

        TmResearch.item("CAP_crimsoncloth", new AspectList().add(Aspect.MAGIC, 8).add(Aspect.CLOTH, 4).add(Aspect.HEAL, 8), -1, -1, TcStack.of("taintedmagic:crimson_cloth_wand_cap"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemWandCap:2"))
            .setSecondary()
            .setConcealed()
            .setParents("CRIMSONROBES")
            .setParentsHidden("CAP_cloth")
            .registerResearchItem();

        TmResearch.item("CAP_shadowcloth", new AspectList().add(Aspect.MAGIC, 16).add(Aspect.CLOTH, 8).add(Aspect.VOID, 16).add(Aspect.DARKNESS, 8), -3, 5, TcStack.of("taintedmagic:shadow_cloth_wand_cap"), 1, 1)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemWandCap:3"))
            .setSecondary()
            .setConcealed()
            .setParents("SHADOWCLOTH")
            .setParentsHidden("CAP_cloth")
            .registerResearchItem();

        TmResearch.item("PRIMALBLADE", new AspectList().add(Aspect.MAGIC, 1).add(Aspect.ELDRITCH, 1).add(Aspect.WEAPON, 1).add(Aspect.VOID, 1).add(Aspect.AURA, 1), 11, -3, TcStack.of("taintedmagic:primal_blade"), 3, 5)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemPrimalBlade"))
            .setConcealed()
            .setParents("CREATION")
            .setParentsHidden("VOIDMETAL", "PRIMALCRUSHER", "UNBALANCEDSHARDS", "HOLLOWDAGGER", "INFUSION")
            .registerResearchItem();

        TmResearch.item("THAUMIUMKATANA", new AspectList().add(Aspect.METAL, 8).add(Aspect.MAGIC, 4).add(Aspect.WEAPON, 6), 12, -1, TcStack.of("taintedmagic:thaumium_katana"), 3, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemKatana:0"))
            .setConcealed()
            .setParentsHidden("ARMORFORTRESS", "INFUSION")
            .registerResearchItem();

        TmResearch.item("VOIDMETALKATANA", new AspectList().add(Aspect.METAL, 16).add(Aspect.MAGIC, 8).add(Aspect.WEAPON, 12).add(Aspect.VOID, 12), 7, -10, TcStack.of("taintedmagic:void_metal_katana"), 3, 3)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemKatana:1"))
            .setSecondary()
            .setConcealed()
            .setParents("VOIDFORTRESS")
            .setParentsHidden("THAUMIUMKATANA", "INFUSION")
            .registerResearchItem();

        TmResearch.item("SHADOWMETALKATANA", new AspectList().add(Aspect.METAL, 16).add(Aspect.MAGIC, 8).add(Aspect.WEAPON, 12).add(Aspect.VOID, 12).add(Aspect.DARKNESS, 14), 0, 5, TcStack.of("taintedmagic:shadow_metal_katana"), 3, 5)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemKatana:2"))
            .setSecondary()
            .setConcealed()
            .setParents("SHADOWFORTRESS")
            .setParentsHidden("VOIDMETALKATANA", "INFUSION")
            .registerResearchItem();

        TmResearch.item("INSCRIPTIONFIRE", new AspectList().add(Aspect.FIRE, 8).add(Aspect.ENTROPY, 4).add(Aspect.METAL, 6), 14, 0, TaintedMagic.id("textures/misc/r_inscription0.png"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemKatanaThaumium:inscription0"))
            .setSecondary()
            .setConcealed()
            .setParents("THAUMIUMKATANA")
            .setParentsHidden("FOCUSFIRE", "INFUSION")
            .registerResearchItem();

        TmResearch.item("INSCRIPTIONTHUNDER", new AspectList().add(Aspect.ENTROPY, 8).add(Aspect.MOTION, 4).add(Aspect.METAL, 6), 14, 1, TaintedMagic.id("textures/misc/r_inscription1.png"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemKatanaThaumium:inscription1"))
            .setSecondary()
            .setConcealed()
            .setParents("THAUMIUMKATANA")
            .setParentsHidden("FOCUSSHOCKWAVE", "INFUSION")
            .registerResearchItem();

        TmResearch.item("INSCRIPTIONHEAL", new AspectList().add(Aspect.HEAL, 8).add(Aspect.UNDEAD, 4).add(Aspect.METAL, 6), 14, 2, TaintedMagic.id("textures/misc/r_inscription2.png"), 1, 2)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemKatanaThaumium:inscription2"))
            .setSecondary()
            .setConcealed()
            .setParents("THAUMIUMKATANA")
            .setParentsHidden("BATHSALTS", "INFUSION")
            .registerResearchItem();

        TmResearch.item("VOIDGOGGLES", new AspectList().add(Aspect.VOID, 8).add(Aspect.DARKNESS, 4).add(Aspect.MAGIC, 6).add(Aspect.SENSES, 12), 3, 3, TcStack.of("taintedmagic:void_metal_goggles"), 1, 2)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemVoidmetalGoggles"))
            .setSecondary()
            .setConcealed()
            .setParents("WARPEDGOGGLES")
            .setParentsHidden("VOIDMETAL", "INFUSION")
            .registerResearchItem();

        TmResearch.item("ELDRITCHFOCUS", new AspectList().add(Aspect.ELDRITCH, 22).add(Aspect.ENTROPY, 14).add(Aspect.AIR, 4).add(Aspect.DARKNESS, 6), 5, -5, TcStack.of("taintedmagic:focus_dark_matter"), 3, 7)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemFocusDarkMatter"))
            .setSpecial()
            .setConcealed()
            .setParents("TMELDRITCHMAJOR")
            .setParentsHidden("OUTERREV", "INFUSION")
            .registerResearchItem();

        TmResearch.item("DIFFUSIONUPGRADE", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.DARKNESS, 8).add(Aspect.WEAPON, 8).add(Aspect.ELDRITCH, 10), 6, -4, TaintedMagic.id("textures/foci/icon_diffusion.png"), 1, 2)
            .setPages(TmResearch.text("1"))
            .setSecondary()
            .setConcealed()
            .setParents("ELDRITCHFOCUS")
            .setParentsHidden("FOCALMANIPULATION")
            .registerResearchItem();

        TmResearch.item("MACEFOCUS", new AspectList().add(Aspect.ENTROPY, 10).add(Aspect.EARTH, 4).add(Aspect.WEAPON, 6).add(Aspect.MAGIC, 8), 7, 3, TcStack.of("taintedmagic:focus_mage_mace"), 2, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemFocusMageMace"))
            .setConcealed()
            .setParentsHidden("THAUMIUM", "FOCUSFIRE", "INFUSION")
            .registerResearchItem();

        TmResearch.item("FOCUSSHOCKWAVE", new AspectList().add(Aspect.MAGIC, 20).add(Aspect.ENTROPY, 12).add(Aspect.AIR, 6).add(Aspect.MOTION, 12), 7, 2, TcStack.of("taintedmagic:focus_shockwave"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemFocusShockwave"))
            .setSecondary()
            .setConcealed()
            .setParentsHidden("FOCUSSHOCK", "INFUSION")
            .registerResearchItem();

        TmResearch.item("FOCUSSHARD", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.MAGIC, 4).add(Aspect.MOTION, 6), 6, 1, TcStack.of("taintedmagic:focus_vis_shard"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemFocusVisShard"))
            .setSecondary()
            .setConcealed()
            .setParents("UNBALANCEDSHARDS")
            .setParentsHidden("FOCUSFIRE")
            .registerResearchItem();

        TmResearch.item("TAINTFOCUS", new AspectList().add(Aspect.TAINT, 4).add(Aspect.LIFE, 4).add(Aspect.MOTION, 4), 5, 2, TcStack.of("taintedmagic:focus_taint_swarm"), 3, 3)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemFocusTaintSwarm"))
            .setConcealed()
            .setParents("FOCUSSHARD")
            .setParentsHidden("INFUSION", "BOTTLETAINT")
            .registerResearchItem();

        TmResearch.item("FOCUSLUMOS", new AspectList().add(Aspect.FIRE, 16).add(Aspect.LIGHT, 24).add(Aspect.ENERGY, 8), 5, 3, TcStack.of("taintedmagic:focus_lumos"), 1, 0)
            .setPages(TmResearch.text("1"), TmResearch.arcane("ItemFocusLumos"))
            .setSecondary()
            .setConcealed()
            .setParentsHidden("FOCUSFIRE")
            .registerResearchItem();

        TmResearch.item("LUMOSRING", new AspectList().add(Aspect.ARMOR, 16).add(Aspect.AURA, 12).add(Aspect.LIGHT, 24).add(Aspect.ENERGY, 8), 6, 4, TcStack.of("taintedmagic:lumos_ring"), 2, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemLumosRing"))
            .setConcealed()
            .setParents("FOCUSLUMOS")
            .setParentsHidden("RUNICARMOR", "INFUSION")
            .registerResearchItem();

        TmResearch.item("FLYTECHARM", new AspectList().add(Aspect.FLIGHT, 15).add(Aspect.AIR, 20).add(Aspect.SENSES, 8).add(Aspect.MAGIC, 12), 13, -4, TcStack.of("taintedmagic:flyte_charm"), 3, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemFlyteCharm"))
            .setConcealed()
            .setParents("CREATION")
            .setParentsHidden("VOIDSASH", "PRIMALARROW", "INFUSION")
            .registerResearchItem();

        TmResearch.item("GATEKEY", new AspectList().add(Aspect.TRAVEL, 30).add(Aspect.EXCHANGE, 15).add(Aspect.AURA, 20).add(Aspect.FLIGHT, 10), 9, -4, TcStack.of("taintedmagic:gate_key"), 3, 0)
            .setPages(TmResearch.text("1"), TmResearch.infusion("ItemGateKey"))
            .setConcealed()
            .setParents("CREATION")
            .setParentsHidden("INFUSION")
            .registerResearchItem();
    }
}
