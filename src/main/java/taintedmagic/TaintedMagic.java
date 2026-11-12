package taintedmagic;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import thaumcraft.aspect.AspectSync;
import taintedmagic.crafting.TmRecipeBook;
import taintedmagic.registry.ModBlocks;
import taintedmagic.research.TmResearchTree;
import taintedmagic.registry.ModCreativeTabs;
import taintedmagic.registry.ModEntities;
import taintedmagic.platform.TmStackData;
import taintedmagic.registry.ModRecipes;
import taintedmagic.registry.ModSounds;
import taintedmagic.registry.ModItems;
import taintedmagic.research.TmResearch;
import taintedmagic.test.CoreGameTests;
import taintedmagic.test.ResearchTableGameTests;
import taintedmagic.test.SmokeGameTests;

@Mod(TaintedMagic.MODID)
public class TaintedMagic {
    public static final String MODID = "taintedmagic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TaintedMagic(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.STARTUP, StartupConfig.SPEC);
        Features.load();
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        taintedmagic.platform.ModAttachments.register(modEventBus);
        ModRecipes.register(modEventBus);
        TmStackData.register(modEventBus);
        TmResearch.init();
        TmRecipeBook.register();
        Features.recipes();
        TmResearchTree.register();
        Features.research();
        AspectSync.addRegistrar(Features::aspects);
        SmokeGameTests.register(modEventBus);
        CoreGameTests.register(modEventBus);
        ResearchTableGameTests.register(modEventBus);
        Features.tests(modEventBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
