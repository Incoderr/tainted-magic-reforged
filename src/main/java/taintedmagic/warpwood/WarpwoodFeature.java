package taintedmagic.warpwood;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import taintedmagic.registry.ModBlocks;
import taintedmagic.registry.ModItems;

public final class WarpwoodFeature {
    public static final DeferredBlock<RotatedPillarBlock> WARPWOOD_LOG = ModBlocks.withItem(ModBlocks.BLOCKS.registerBlock(
        "warpwood_log", RotatedPillarBlock::new, properties -> wood(properties, 2.5F)
    ));
    public static final DeferredBlock<WarpwoodKnotBlock> WARPWOOD_KNOT = ModBlocks.BLOCKS.registerBlock(
        "warpwood_knot", WarpwoodKnotBlock::new, properties -> wood(properties, 25.0F).explosionResistance(2.5F)
    );
    public static final DeferredBlock<Block> WARPWOOD_PLANKS = ModBlocks.withItem(ModBlocks.BLOCKS.registerBlock(
        "warpwood_planks", Block::new, properties -> wood(properties, 0.8F)
    ));
    public static final DeferredBlock<WarpwoodLeavesBlock> WARPWOOD_LEAVES = ModBlocks.withItem(ModBlocks.BLOCKS.registerBlock(
        "warpwood_leaves",
        WarpwoodLeavesBlock::new,
        properties -> properties
            .mapColor(MapColor.PLANT)
            .strength(0.1F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isSuffocating((state, level, pos) -> false)
            .isViewBlocking((state, level, pos, box) -> false)
            .pushReaction(PushReaction.POPPED)
            .isRedstoneConductor((state, level, pos) -> false)
    ));
    public static final DeferredBlock<WarpwoodSaplingBlock> WARPWOOD_SAPLING = ModBlocks.withItem(ModBlocks.BLOCKS.registerBlock(
        "warpwood_sapling",
        WarpwoodSaplingBlock::new,
        properties -> properties
            .mapColor(MapColor.PLANT)
            .noCollision()
            .instabreak()
            .randomTicks()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.POPPED)
    ));
    public static final DeferredItem<WarpFertilizerItem> WARP_FERTILIZER = ModItems.ITEMS.registerItem(
        "warp_fertilizer", WarpFertilizerItem::new, properties -> properties.rarity(Rarity.UNCOMMON)
    );

    private WarpwoodFeature() {
    }

    private static BlockBehaviour.Properties wood(BlockBehaviour.Properties properties, float hardness) {
        return properties.mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(hardness).sound(SoundType.WOOD);
    }

    public static void load() {
    }
}
