package taintedmagic.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public final class TmRarity {
    public static final EnumProxy<Rarity> CREATION_PROXY = new EnumProxy<>(Rarity.class, -1, "taintedmagic:creation", ChatFormatting.GOLD);

    private TmRarity() {
    }

    public static Rarity creation() {
        return CREATION_PROXY.getValue();
    }
}
