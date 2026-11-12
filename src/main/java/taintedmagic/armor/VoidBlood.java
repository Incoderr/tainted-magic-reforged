package taintedmagic.armor;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import taintedmagic.materials.MaterialsFeature;
import taintedmagic.platform.TmStackKey;

public class VoidBlood extends CustomRecipe {
    public static final TmStackKey<Boolean> VOIDTOUCHED = ArmorFeature.VOIDTOUCHED;
    public static final VoidBlood INSTANCE = new VoidBlood(CraftingBookCategory.MISC);
    public static final RecipeSerializer<VoidBlood> SERIALIZER = new SimpleCraftingRecipeSerializer<>(VoidBlood::new);

    public VoidBlood(CraftingBookCategory category) {
        super(category);
    }

    public static boolean isVoidtouched(ItemStack stack) {
        return VOIDTOUCHED.getOrDefault(stack, false);
    }

    public static boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armor && armor.getEquipmentSlot().getType() == EquipmentSlot.Type.HUMANOID_ARMOR && stack.isDamageableItem();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean blood = false;
        boolean armor = false;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(MaterialsFeature.VOID_BLOOD.get()) && !blood) {
                blood = true;
            } else if (isArmor(stack) && !armor) {
                armor = true;
            } else {
                return false;
            }
        }
        return blood && armor;
    }

    public ItemStack assemble(CraftingInput input) {
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (isArmor(stack)) {
                if (isVoidtouched(stack)) {
                    return ItemStack.EMPTY;
                }
                ItemStack result = stack.copyWithCount(1);
                VOIDTOUCHED.set(result, true);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return assemble(input);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<VoidBlood> getSerializer() {
        return SERIALIZER;
    }
}