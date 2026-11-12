package taintedmagic.armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import thaumcraft.item.gear.WarpingGear;
import thaumcraft.registry.ModSounds;
import taintedmagic.fx.TmFx;

public class GateKeyItem extends Item implements WarpingGear {
    public static final int USE_TICKS = 40;

    public record Binding(int x, int y, int z, String dimension, int colour) {
        public static final Codec<Binding> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Binding::x),
            Codec.INT.fieldOf("y").forGetter(Binding::y),
            Codec.INT.fieldOf("z").forGetter(Binding::z),
            Codec.STRING.fieldOf("dimension").forGetter(Binding::dimension),
            Codec.INT.fieldOf("colour").forGetter(Binding::colour)
        ).apply(instance, Binding::new));
        public static final StreamCodec<ByteBuf, Binding> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, Binding::x,
            ByteBufCodecs.VAR_INT, Binding::y,
            ByteBufCodecs.VAR_INT, Binding::z,
            ByteBufCodecs.STRING_UTF8, Binding::dimension,
            ByteBufCodecs.VAR_INT, Binding::colour,
            Binding::new
        );
    }

    public GateKeyItem(Properties properties) {
        super(properties);
    }

    public static Binding binding(ItemStack stack) {
        return ArmorFeature.GATE_BINDING.get(stack);
    }

    @Override
    public int getWarp(ItemStack stack, Player player) {
        return 3;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return USE_TICKS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (binding(stack) == null) {
            BlockPos pos = context.getClickedPos();
            if (!level.isClientSide()) {
                ArmorFeature.GATE_BINDING.set(stack, new Binding(pos.getX(), pos.getY() + 1, pos.getZ(), level.dimension().identifier().toString(), level.getRandom().nextInt(360) + 1));
            } else {
                for (int index = 0; index < 9; index++) {
                    TmFx.get().gateSparkle(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                }
            }
            if (player != null) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.WAND.get(), SoundSource.PLAYERS, 0.5F + (float) Math.random() * 0.5F, 1.0F);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (binding(player.getItemInHand(hand)) != null) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int ticksRemaining) {
        if (level.isClientSide()) {
            TmFx.get().gateSparkle(level, entity.getX(), entity.getBoundingBox().minY, entity.getZ());
        }
        float pitch = 1.0F + (float) Math.random() * 0.25F;
        if (entity.tickCount % 5 == 0 && !level.isClientSide()) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.WIND.get(), SoundSource.PLAYERS, pitch * 0.1F, pitch);
        }
    }

    public static boolean canTeleport(Level level, Binding binding) {
        BlockPos target = new BlockPos(binding.x(), binding.y(), binding.z());
        return binding.y() > level.getMinY() - 1 && binding.dimension().equals(level.dimension().identifier().toString())
            && level.getBlockState(target).isAir() && level.getBlockState(target.above()).isAir();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Binding binding = binding(stack);
        if (binding != null && entity instanceof Player player) {
            if (canTeleport(level, binding)) {
                if (level instanceof ServerLevel server && player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.teleportTo(binding.x() + 0.5, binding.y(), binding.z() + 0.5);
                    server.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 5.0F, 1.0F);
                }
                player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 160, 0));
            } else if (!level.isClientSide()) {
                String key = binding.dimension().equals(level.dimension().identifier().toString()) ? "taintedmagic.key.error" : "taintedmagic.key.invaliddim";
                player.sendOverlayMessage(Component.translatable(key).withStyle(ChatFormatting.RED));
            }
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        Binding binding = binding(stack);
        if (binding != null) {
            builder.accept(Component.translatable("taintedmagic.key.bound").withStyle(ChatFormatting.GREEN));
            builder.accept(Component.literal(binding.dimension()));
            builder.accept(Component.literal(binding.x() + ", " + binding.y() + ", " + binding.z()));
        } else {
            builder.accept(Component.translatable("taintedmagic.key.unbound").withStyle(ChatFormatting.RED));
        }
    }
}
