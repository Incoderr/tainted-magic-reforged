package taintedmagic.registry;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import taintedmagic.TaintedMagic;
import taintedmagic.wands.WandsFeature;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TaintedMagic.MODID);

    public static final Supplier<CreativeModeTab> TAINTED_MAGIC = TABS.register(TaintedMagic.MODID, () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + TaintedMagic.MODID))
        .icon(WandsFeature::tabIcon)
        .displayItems((parameters, output) -> {
            WandsFeature.creativeWands().forEach(output::accept);
            for (DeferredHolder<Item, ? extends Item> holder : ModItems.ITEMS.getEntries()) {
                output.accept(holder.get());
            }
        })
        .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
