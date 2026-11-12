package taintedmagic.platform;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import taintedmagic.TaintedMagic;

public final class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TaintedMagic.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ItemStack>> HIP_KATANA = ATTACHMENTS.register("hip_katana", () -> AttachmentType
        .builder(() -> ItemStack.EMPTY)
        .sync(ItemStack.OPTIONAL_STREAM_CODEC)
        .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FLYTE_CHARM = ATTACHMENTS.register("flyte_charm", () -> AttachmentType
        .builder(() -> false)
        .sync(net.minecraft.network.codec.ByteBufCodecs.BOOL)
        .build());

    private ModAttachments() {
    }

    public static void register(IEventBus bus) {
        ATTACHMENTS.register(bus);
    }
}
