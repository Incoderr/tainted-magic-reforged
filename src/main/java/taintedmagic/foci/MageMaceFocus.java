package taintedmagic.foci;

import java.util.List;
import java.text.DecimalFormat;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import javax.annotation.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectList;
import thaumcraft.wand.focus.FocusUpgradeType;
import taintedmagic.Config;
import taintedmagic.TaintedMagic;

public class MageMaceFocus extends TmFocus {
    private static final AspectList COST = new AspectList().add(Aspect.ENTROPY, 10);

    public MageMaceFocus(Properties properties) {
        super(properties);
    }

    @Override
    public String getSortingHelper(ItemStack focus) {
        return "MACE" + super.getSortingHelper(focus);
    }

    @Override
    public int getFocusColor(ItemStack focus) {
        return 0xACA9B5;
    }

    @Override
    public @Nullable ResourceLocation getFocusTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_mage_mace");
    }

    @Override
    public @Nullable ResourceLocation getOrnamentTexture(ItemStack focus) {
        return TaintedMagic.id("item/focus_mage_mace_orn");
    }

    @Override
    public AspectList getVisCost(ItemStack focus) {
        return COST;
    }

    @Override
    public int getActivationCooldown(ItemStack focus) {
        return -1;
    }

    @Override
    public void appendCostText(ItemStack stack, List<Component> builder) {
        List<Component> lines = new java.util.ArrayList<>();
        super.appendCostText(stack, lines);
        for (Component line : lines) {
            builder.add(isCostHeader(line) ? Component.translatable("item.Focus.cost3") : line);
        }
    }

    private static boolean isCostHeader(Component line) {
        return line.getContents() instanceof net.minecraft.network.chat.contents.TranslatableContents contents && contents.getKey().equals("item.Focus.cost1");
    }

    public static double damage(ItemStack focus, int potency) {
        return Config.mageMaceDamageBase() + potency;
    }

    @Override
    public void addFocusInformation(ItemStack stack, List<Component> builder) {
        super.addFocusInformation(stack, builder);
        builder.add(Component.literal(" "));
        String amount = new DecimalFormat("0.#").format(damage(stack, getUpgradeLevel(stack, FocusUpgradeType.POTENCY)));
        builder.add(Component.literal("+" + amount + " ").append(Component.translatable("taintedmagic.text.attackdamageequipped")).withStyle(ChatFormatting.BLUE));
    }

    @Override
    public InteractionResult onFocusRightClick(ItemStack wand, Level level, Player player, @Nullable HitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    @Nullable public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack focus, int rank) {
        return rank >= 1 && rank <= 5 ? upgrades(FocusUpgradeType.FRUGAL, FocusUpgradeType.POTENCY) : null;
    }
}
