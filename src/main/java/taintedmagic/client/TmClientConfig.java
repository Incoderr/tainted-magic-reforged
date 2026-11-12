package taintedmagic.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class TmClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue RESEARCH_TAGS = BUILDER
        .comment("Setting this to false will disable the '[TM]' tag on Tainted Magic research items.")
        .define("research.RESEARCH_TAGS", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private TmClientConfig() {
    }

    public static boolean researchTags() {
        return SPEC.isLoaded() ? RESEARCH_TAGS.getAsBoolean() : RESEARCH_TAGS.getDefault();
    }
}
