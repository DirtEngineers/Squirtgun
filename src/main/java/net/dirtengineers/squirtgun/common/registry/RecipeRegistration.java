package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.recipe.fluid_encapsulator.FluidEncapsulatorRecipe;
import net.dirtengineers.squirtgun.common.recipe.fluid_encapsulator.FluidEncapsulatorRecipeSerializer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeRegistration {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS;
    public static RegistryObject<RecipeType<FluidEncapsulatorRecipe>> FLUID_ENCAPSULATOR_TYPE;
    public static final RegistryObject<FluidEncapsulatorRecipeSerializer<FluidEncapsulatorRecipe>> FLUID_ENCAPSULATOR_SERIALIZER;
    private static final Map<RecipeType<? extends Recipe<Inventory>>, List<? extends Recipe<Inventory>>> recipesMap;

    RecipeRegistration() {
    }

    private static <T extends Recipe<Inventory>> RegistryObject<RecipeType<T>> registerRecipeType(String pType) {
        return RECIPE_TYPES.register(pType, () -> new RecipeType<>() {
            public String toString() {
                return pType;
            }
        });
    }

    public static <T extends Recipe<Inventory>> List getRecipesByType(RecipeType<T> pRecipeType, Level pLevel) {
        if (recipesMap.get(pRecipeType) == null) {
            List recipes = pLevel.getRecipeManager().getRecipes().stream().filter((recipe) -> recipe.getType().equals(pRecipeType)).collect(Collectors.toList());
            recipesMap.put(pRecipeType, recipes);
        }

        return recipesMap.get(pRecipeType);
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }

    static {
        RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Squirtgun.MOD_ID);
        SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Squirtgun.MOD_ID);
        FLUID_ENCAPSULATOR_TYPE = registerRecipeType("fluid_encapsulator");
        FLUID_ENCAPSULATOR_SERIALIZER = SERIALIZERS.register("fluid_encapsulator", () -> {
            return new FluidEncapsulatorRecipeSerializer(FluidEncapsulatorRecipe::new);
        });
        recipesMap = new HashMap<>();
    }
}