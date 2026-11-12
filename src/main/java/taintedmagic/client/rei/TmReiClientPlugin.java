package taintedmagic.client.rei;

import java.util.Map;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.infusion.InfusionKeys;
import taintedmagic.tools.ToolsFeature;

@REIPluginClient
public final class TmReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerEntries(EntryRegistry registry) {
        Item[] katanas = {ToolsFeature.THAUMIUM_KATANA.get(), ToolsFeature.VOID_METAL_KATANA.get(), ToolsFeature.SHADOW_METAL_KATANA.get()};
        for (Item katana : katanas) {
            for (int inscription = 0; inscription < 3; inscription++) {
                ItemStack stack = new ItemStack(katana);
                InfusionKeys.TAGS.set(stack, Map.of("inscription", String.valueOf(inscription)));
                EntryStack<ItemStack> entry = EntryStacks.of(stack);
                if (!registry.alreadyContain(entry)) {
                    registry.addEntry(entry);
                }
            }
        }
    }
}
