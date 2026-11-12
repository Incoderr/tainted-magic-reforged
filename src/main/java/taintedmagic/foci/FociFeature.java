package taintedmagic.foci;

import java.util.function.Function;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.wand.focus.FocusItem;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.TaintedMagic;
import taintedmagic.foci.entity.DarkMatter;
import taintedmagic.foci.entity.Diffusion;
import taintedmagic.foci.entity.HomingShard;
import taintedmagic.registry.ModBlocks;
import taintedmagic.registry.ModEntities;
import taintedmagic.registry.ModItems;

public final class FociFeature {
    public static final FocusUpgradeType SANITY = upgrade(64, "sanity", "icon_sanity", new AspectList().add(Aspect.MIND, 1).add(Aspect.HEAL, 1));
    public static final FocusUpgradeType ANTIBODY = upgrade(65, "antibody", "icon_antibody", new AspectList().add(Aspect.TAINT, 1).add(Aspect.HEAL, 1));
    public static final FocusUpgradeType CORROSIVE = upgrade(66, "corrosive", "icon_corrosive", new AspectList().add(Aspect.TAINT, 1).add(Aspect.POISON, 1));
    public static final FocusUpgradeType PERSISTENT = upgrade(67, "persistent", "icon_persistent", new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MOTION, 1).add(Aspect.ENERGY, 1));
    public static final FocusUpgradeType DIFFUSION = upgrade(68, "diffusion", "icon_diffusion", new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.ELDRITCH, 2).add(Aspect.AURA, 4));

    public static final DeferredItem<TaintSwarmFocus> FOCUS_TAINT_SWARM = focus("focus_taint_swarm", TaintSwarmFocus::new);
    public static final DeferredItem<DarkMatterFocus> FOCUS_DARK_MATTER = focus("focus_dark_matter", DarkMatterFocus::new);
    public static final DeferredItem<MageMaceFocus> FOCUS_MAGE_MACE = focus("focus_mage_mace", MageMaceFocus::new);
    public static final DeferredItem<ShockwaveFocus> FOCUS_SHOCKWAVE = focus("focus_shockwave", ShockwaveFocus::new);
    public static final DeferredItem<VisShardFocus> FOCUS_VIS_SHARD = focus("focus_vis_shard", VisShardFocus::new);
    public static final DeferredItem<LumosFocus> FOCUS_LUMOS = focus("focus_lumos", LumosFocus::new);

    public static final DeferredBlock<LumosBlock> LUMOS = ModBlocks.BLOCKS.registerBlock(
        "lumos",
        LumosBlock::new,
        properties -> properties
            .mapColor(MapColor.NONE)
            .noCollission()
            .instabreak()
            .replaceable()
            .noLootTable()
            .lightLevel(state -> 15)
            .sound(SoundType.STONE)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<DarkMatter>> DARK_MATTER = ModEntities.projectile("dark_matter", DarkMatter::new, 0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<HomingShard>> HOMING_SHARD = ModEntities.projectile("homing_shard", HomingShard::new, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<Diffusion>> DIFFUSION_ENTITY = ModEntities.projectile("diffusion", Diffusion::new, 0.25F);

    private FociFeature() {
    }

    private static FocusUpgradeType upgrade(int id, String name, String icon, AspectList aspects) {
        if (FocusUpgradeType.byId(id) != null) {
            throw new IllegalStateException("Tainted Magic focus upgrade id " + id + " is already used by " + FocusUpgradeType.byId(id).name());
        }
        return new FocusUpgradeType(id, name, TaintedMagic.id("textures/foci/" + icon + ".png"), aspects);
    }

    private static <T extends Item> DeferredItem<T> focus(String name, Function<Item.Properties, T> factory) {
        return ModItems.ITEMS.registerItem(name, factory, FocusItem::properties);
    }

    public static void load() {
    }
}
