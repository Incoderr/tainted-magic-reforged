package taintedmagic.tools;

import net.minecraft.world.InteractionResultHolder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.wand.WandManager;

public class ThaumicDisassemblerItem extends Item {
    public static final int MAX_CHARGE = 50000;
    public static final int ENTROPY_USAGE_BASE = 5;
    public static final int ENTROPY_USAGE_HOE = ENTROPY_USAGE_BASE * 5;
    public static final int ENTROPY_USAGE_HIT = ENTROPY_USAGE_BASE * 10;
    private static final String[] MODES = {"normal", "slow", "fast", "off"};

    public ThaumicDisassemblerItem(Properties properties) {
        super(properties);
    }

    public static int efficiency(int mode) {
        return switch (mode) {
            case 0 -> 20;
            case 1 -> 8;
            case 2 -> 128;
            default -> 0;
        };
    }

    public static int mode(ItemStack stack) {
        return ToolsFeature.DISASSEMBLER_MODE.getOrDefault(stack, 0);
    }

    public static int charge(ItemStack stack) {
        return ToolsFeature.ENTROPY_CHARGE.getOrDefault(stack, 0);
    }

    public static void setCharge(ItemStack stack, int charge) {
        ToolsFeature.ENTROPY_CHARGE.set(stack, Math.max(0, Math.min(MAX_CHARGE, charge)));
    }

    public static void consume(ItemStack stack, int amount) {
        setCharge(stack, charge(stack) - amount);
    }

    public static float attackDamage(ItemStack stack) {
        return charge(stack) > 0 ? 20.0F : 4.0F;
    }

    private static Component modeName(int mode) {
        return Component.translatable("taintedmagic.text.disassembler." + MODES[mode]).withStyle(mode == 3 ? ChatFormatting.RED : ChatFormatting.GREEN);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * charge(stack) / MAX_CHARGE);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x6E6E6E;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return charge(stack) > 0 ? efficiency(mode(stack)) : 1.0F;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return !state.is(Blocks.BEDROCK);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity owner) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            consume(stack, (int) (ENTROPY_USAGE_BASE * (double) efficiency(mode(stack)) / 8.0));
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity owner, int slotIndex, boolean selected) {
        if (!(world instanceof ServerLevel level)) {
            return;
        }
        if (owner instanceof Player player && charge(stack) < MAX_CHARGE && owner.tickCount % 20 == 0) {
            int amount = Math.min(100, MAX_CHARGE - charge(stack));
            if (WandManager.consumeVisFromInventory(player, new AspectList().add(Aspect.ENTROPY, amount))) {
                setCharge(stack, charge(stack) + amount);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            int mode = (mode(stack) + 1) % 4;
            ToolsFeature.DISASSEMBLER_MODE.set(stack, mode);
            player.displayClientMessage(Component.translatable("taintedmagic.text.disassembler.mode").withStyle(ChatFormatting.GRAY)
                .append(": ").append(modeName(mode))
                .append(Component.literal(" (" + efficiency(mode) + ")").withStyle(mode == 3 ? ChatFormatting.RED : ChatFormatting.GREEN)), true);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (player == null || player.isShiftKeyDown() || mode(stack) == 3) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        if (!till(stack, player, level, pos, side) && !level.getBlockState(pos).is(Blocks.FARMLAND)) {
            return InteractionResult.PASS;
        }
        if (mode(stack) != 1) {
            int radius = mode(stack) == 0 ? 1 : 2;
            for (BlockPos other : BlockPos.betweenClosed(pos.offset(-radius, 0, -radius), pos.offset(radius, 0, radius))) {
                till(stack, player, level, other.immutable(), side);
            }
        }
        return InteractionResult.SUCCESS;
    }

    public static boolean till(ItemStack stack, Player player, Level level, BlockPos pos, Direction side) {
        if (!player.mayUseItemAt(pos, side, stack) || !player.isCreative() && charge(stack) < ENTROPY_USAGE_HOE) {
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if (side == Direction.DOWN || !level.isEmptyBlock(pos.above()) || !state.is(Blocks.GRASS_BLOCK) && !state.is(Blocks.DIRT)) {
            return false;
        }
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
            level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 0.8F);
            if (!player.isCreative()) {
                consume(stack, ENTROPY_USAGE_HOE);
            }
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> builder, TooltipFlag flag) {
        builder.add(Component.translatable("taintedmagic.text.disassembler.charge").append(": ")
            .append(Component.literal(charge(stack) / 100 + "/" + MAX_CHARGE / 100).withStyle(ChatFormatting.DARK_GRAY)));
        builder.add(Component.translatable("taintedmagic.text.disassembler.mode").append(": ").append(modeName(mode(stack))));
        builder.add(Component.translatable("taintedmagic.text.disassembler.efficiency").append(": ")
            .append(Component.literal(String.valueOf(efficiency(mode(stack)))).withStyle(mode(stack) == 3 ? ChatFormatting.RED : ChatFormatting.GREEN)));
        builder.add(Component.literal(" "));
        builder.add(Component.literal("+20 ").append(Component.translatable("taintedmagic.text.attackdamage")).withStyle(ChatFormatting.BLUE));
    }
}
