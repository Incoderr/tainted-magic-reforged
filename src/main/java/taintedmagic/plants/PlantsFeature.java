package taintedmagic.plants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import thaumcraft.platform.Registration;
import taintedmagic.TaintedMagic;
import taintedmagic.registry.ModBlocks;
import taintedmagic.registry.ModItems;

public final class PlantsFeature {
    public static final ResourceKey<DamageType> NIGHTSHADE = ResourceKey.create(Registries.DAMAGE_TYPE, TaintedMagic.id("nightshade"));

    public static final DeferredBlock<NightshadeBushBlock> NIGHTSHADE_BUSH = ModBlocks.withItem(ModBlocks.BLOCKS.registerBlock(
        "nightshade_bush",
        NightshadeBushBlock::new,
        properties -> properties
            .mapColor(MapColor.PLANT)
            .noCollision()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.POPPED)
    ));
    public static final DeferredItem<NightshadeBerriesItem> NIGHTSHADE_BERRIES = ModItems.ITEMS.registerItem(
        "nightshade_berries",
        NightshadeBerriesItem::new,
        properties -> Registration.food(properties, 0, 0.0F, true, 1.6F).rarity(Rarity.UNCOMMON)
    );
    public static final DeferredItem<MagicFunguarItem> MAGIC_FUNGUAR = ModItems.ITEMS.registerItem(
        "magic_funguar",
        MagicFunguarItem::new,
        properties -> Registration.food(properties, 3, 0.2F, true, 1.6F).rarity(Rarity.UNCOMMON)
    );

    private PlantsFeature() {
    }

    public static void load() {
    }
}
