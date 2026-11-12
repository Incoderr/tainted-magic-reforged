package taintedmagic.materials;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import thaumcraft.registry.ModSounds;
import thaumcraft.research.ResearchManager;
import taintedmagic.TaintedMagic;

@EventBusSubscriber(modid = TaintedMagic.MODID)
public final class CreationEvents {
    private CreationEvents() {
    }

    @SubscribeEvent
    static void crafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getCrafting().is(MaterialsFeature.CREATION_SHARD.get())) {
            giveResearch(player);
        }
    }

    public static void giveResearch(ServerPlayer player) {
        if (!ResearchManager.isComplete(player, "CREATION") && ResearchManager.isComplete(player, "CREATIONSHARD")) {
            ResearchManager.complete(player, "CREATION");
            player.sendOverlayMessage(Component.translatable("taintedmagic.text.creation").withStyle(ChatFormatting.DARK_PURPLE));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ELDRITCH_GUARDIAN_IDLE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.HEARTBEAT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
        }
        if (!ResearchManager.isComplete(player, "OUTERREV") && ResearchManager.isComplete(player, "CREATION")) {
            ResearchManager.complete(player, "OUTERREV");
        }
    }
}
