package taintedmagic.registry;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.Wands;
import taintedmagic.TaintedMagic;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TaintedMagic.MODID);

    public static final Supplier<CreativeModeTab> TAINTED_MAGIC = TABS.register(TaintedMagic.MODID, () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + TaintedMagic.MODID))
        .icon(ModCreativeTabs::icon)
        .displayItems((parameters, output) -> {
            for (DeferredHolder<Item, ? extends Item> holder : ModItems.ITEMS.getEntries()) {
                output.accept(holder.get());
            }
        })
        .build());

    private ModCreativeTabs() {
    }

    static ItemStack icon() {
        return Wands.create(WandRegistry.CAP_VOID, WandRegistry.ROD_SILVERWOOD, false);
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
