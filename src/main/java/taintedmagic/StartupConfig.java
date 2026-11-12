package taintedmagic;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class StartupConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue CUSTOM_RESEARCH_TAB_BACK = BUILDER
        .comment("Setting this to true will bring back the old Tainted Magic research tab background.")
        .define("research.CUSTOM_RESEARCH_TAB_BACK", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private StartupConfig() {
    }

    public static boolean customResearchTabBack() {
        return SPEC.isLoaded() ? CUSTOM_RESEARCH_TAB_BACK.getAsBoolean() : CUSTOM_RESEARCH_TAB_BACK.getDefault();
    }
}
