package taintedmagic;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue MAGE_MACE_DMG_INC_BASE = BUILDER
        .comment("Defines the base amount of damage to increase wand / staff attack damage by when the Mage's Mace focus is equipped.")
        .defineInRange("wands_and_foci.MAGE_MACE_DMG_INC_BASE", 8.0, 1.0, 100.0);

    public static final ModConfigSpec.DoubleValue WARP_WAND_REFRESH_BASE = BUILDER
        .comment("Defines the base refresh period in ticks for the Warpwood Wand. The delay period between vis refreshes is calculated by dividing this number by the amount of permanent warp the player has accumulated.")
        .defineInRange("wands_and_foci.WARP_WAND_REFRESH_BASE", 10000.0, 100.0, 100000.0);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {
    }

    public static double mageMaceDamageBase() {
        return SPEC.isLoaded() ? MAGE_MACE_DMG_INC_BASE.getAsDouble() : MAGE_MACE_DMG_INC_BASE.getDefault();
    }

    public static double warpWandRefreshBase() {
        return SPEC.isLoaded() ? WARP_WAND_REFRESH_BASE.getAsDouble() : WARP_WAND_REFRESH_BASE.getDefault();
    }
}
