package taintedmagic;

import net.neoforged.bus.api.IEventBus;
import thaumcraft.aspect.AspectRegistry;
import taintedmagic.armor.ArmorFeature;
import taintedmagic.foci.FociFeature;
import taintedmagic.materials.MaterialsFeature;
import taintedmagic.tools.ToolsFeature;
import taintedmagic.wands.WandsFeature;
import taintedmagic.plants.PlantsFeature;
import taintedmagic.test.MaterialsGameTests;
import taintedmagic.test.PlantsGameTests;
import taintedmagic.test.WarpwoodGameTests;
import taintedmagic.warpwood.WarpwoodFeature;

public final class Features {
    private Features() {
    }

    public static void load() {
        MaterialsFeature.load();
        WarpwoodFeature.load();
        PlantsFeature.load();
        WandsFeature.load();
        FociFeature.load();
        ArmorFeature.load();
        ToolsFeature.load();
    }

    public static void research() {
    }

    public static void recipes() {
    }

    public static void aspects(AspectRegistry registry) {
    }

    public static void tests(IEventBus modEventBus) {
        MaterialsGameTests.register(modEventBus);
        WarpwoodGameTests.register(modEventBus);
        PlantsGameTests.register(modEventBus);
    }
}
