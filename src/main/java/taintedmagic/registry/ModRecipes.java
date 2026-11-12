package taintedmagic.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import taintedmagic.TaintedMagic;
import taintedmagic.armor.VoidBlood;

public final class ModRecipes {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TaintedMagic.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<VoidBlood>> VOID_BLOOD = SERIALIZERS.register("void_blood", () -> VoidBlood.SERIALIZER);

    private ModRecipes() {
    }

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}
