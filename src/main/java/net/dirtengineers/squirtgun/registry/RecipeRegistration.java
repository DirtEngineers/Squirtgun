package net.dirtengineers.squirtgun.registry;

import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RecipeRegistration {

    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Squirtgun.MOD_ID);
    public static RecipeType<ChemicalPhialRecipe> CHEMICAL_PHIAL_TYPE;
    public static RecipeType<PotionPhialRecipe> POTION_PHIAL_TYPE;

    public static final RegistryObject<ChemicalPhialRecipeSerializer<ChemicalPhialRecipe>> CHEMICAL_PHIAL_SERIALIZER = SERIALIZERS.register(
            "chemical_phial_serializer",
            () -> new ChemicalPhialRecipeSerializer<>(ChemicalPhialRecipe::new));
    public static final RegistryObject<PotionPhialRecipeSerializer<PotionPhialRecipe>> POTION_PHIAL_SERIALIZER = SERIALIZERS.register(
            "potion_phial_serializer",
            () -> new PotionPhialRecipeSerializer<>(PotionPhialRecipe::new));

    private static final Map<RecipeType<? extends AbstractProcessingRecipe>, LinkedList<? extends AbstractProcessingRecipe>> recipeTypeMap = new LinkedHashMap<>();
    private static final Map<String, LinkedList<? extends AbstractProcessingRecipe>> recipeGroupMap = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static <R extends AbstractPhialRecipe> LinkedList<R> getRecipesByType(RecipeType<R> pRecipeType, Level pLevel) {
        if (recipeTypeMap.get(pRecipeType) == null) {
            LinkedList<R> recipes = pLevel.getRecipeManager().getRecipes().stream()
                    .filter((recipe) -> Objects.equals(recipe.getType(), pRecipeType))
                    .map(recipe -> (R) recipe)
                    .sorted()
                    .collect(Collectors.toCollection(LinkedList::new));
            recipeTypeMap.put(pRecipeType, recipes);
        }
        return (LinkedList<R>) recipeTypeMap.get(pRecipeType);
    }

    @SuppressWarnings("unchecked")
    public static <R extends AbstractPhialRecipe> LinkedList<R> getRecipesByGroup(String pGroup, Level pLevel) {
        if (recipeGroupMap.get(pGroup) == null) {
            LinkedList<R> recipes = pLevel.getRecipeManager().getRecipes().stream()
                    .filter(recipe -> Objects.equals(recipe.getGroup(), pGroup))
                    .map(recipe -> (R) recipe)
                    .sorted()
                    .collect(Collectors.toCollection(LinkedList::new));
            recipeGroupMap.put(pGroup, recipes);
        }
        return (LinkedList<R>) recipeGroupMap.get(pGroup);
    }

    @SuppressWarnings("unchecked")
    public static <R extends AbstractProcessingRecipe> Optional<R> getRecipeByGroupAndId(String pGroup, ResourceLocation pRecipeId, Level pLevel) {
        return getRecipesByGroup(pGroup, pLevel).stream().filter(recipe -> recipe.getId().equals(pRecipeId)).findFirst().map(recipe -> (R) recipe);
    }

    public static LinkedList<ChemicalPhialRecipe> getChemicalPhialRecipes(Level pLevel) {
        return getRecipesByType(CHEMICAL_PHIAL_TYPE, pLevel);
    }

    public static LinkedList<PotionPhialRecipe> getPotionPhialRecipes(Level pLevel) {
        return getRecipesByType(POTION_PHIAL_TYPE, pLevel);
    }

    public static LinkedList<AbstractPhialRecipe> getAllPhialRecipes(Level pLevel) {

        LinkedList<AbstractPhialRecipe> recipes = new LinkedList<>();
        recipes.addAll(getChemicalPhialRecipes(pLevel));
        recipes.addAll(getPotionPhialRecipes(pLevel));
        return recipes;
    }

    public static Optional<AbstractPhialRecipe> getPhialRecipe(Predicate<AbstractPhialRecipe> pPredicate, Level pLevel) {
        return getAllPhialRecipes(pLevel).stream().filter(pPredicate).findFirst();
    }

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}