package taintedmagic.armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import taintedmagic.materials.MaterialsFeature;
import taintedmagic.platform.TmStackKey;

public class VoidBlood extends CustomRecipe {
    public static final TmStackKey<Boolean> VOIDTOUCHED = ArmorFeature.VOIDTOUCHED;
    public static final VoidBlood INSTANCE = new VoidBlood();
    public static final MapCodec<VoidBlood> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, VoidBlood> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<VoidBlood> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public static boolean isVoidtouched(ItemStack stack) {
        return VOIDTOUCHED.getOrDefault(stack, false);
    }

    public static boolean isArmor(ItemStack stack) {
        var equippable = stack.get(DataComponents.EQUIPPABLE);
        return equippable != null && equippable.slot().getType() == EquipmentSlot.Type.HUMANOID_ARMOR && stack.isDamageableItem();
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

    @Override
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
    public RecipeSerializer<VoidBlood> getSerializer() {
        return SERIALIZER;
    }
}
