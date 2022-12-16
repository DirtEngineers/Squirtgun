package net.dirtengineers.squirtgun.registry;

import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Recipe;
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
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS;
    public static RegistryObject<RecipeType<ChemicalPhialRecipe>> CHEMICAL_PHIAL_TYPE;
    public static RegistryObject<RecipeType<PotionPhialRecipe>> POTION_PHIAL_TYPE;
    public static final RegistryObject<ChemicalPhialRecipeSerializer<ChemicalPhialRecipe>> CHEMICAL_PHIAL_SERIALIZER;
    public static final RegistryObject<PotionPhialRecipeSerializer<PotionPhialRecipe>> POTION_PHIAL_SERIALIZER;
    private static final Map<RecipeType<? extends Recipe<Inventory>>, List<? extends Recipe<Inventory>>> recipesMap;
    private static final Map<RecipeType<? extends AbstractProcessingRecipe>, LinkedList<? extends AbstractProcessingRecipe>> recipeTypeMap;
    private static final Map<String, LinkedList<? extends AbstractProcessingRecipe>> recipeGroupMap;

    RecipeRegistration() {}

    private static <T extends Recipe<Inventory>> RegistryObject<RecipeType<T>> registerRecipeType(String pType) {
        return RECIPE_TYPES.register(pType, () -> new RecipeType<>() { public String toString() { return pType; } });
    }

    public static void populateRecipeTypeMap(Level pLevel) {
        populateRecipes(CHEMICAL_PHIAL_TYPE.get(), pLevel);
        populateRecipes(POTION_PHIAL_TYPE.get(), pLevel);
    }

    public static <R extends AbstractPhialRecipe> void populateRecipes(RecipeType<R> pRecipeType, Level pLevel) {
        if (recipeTypeMap.get(pRecipeType) == null) {
            LinkedList<R> recipes = (LinkedList)pLevel.getRecipeManager().getRecipes().stream().filter((recipe) -> {
                return recipe.getType().equals(pRecipeType);
            }).map((recipe) -> {
                return (AbstractProcessingRecipe)recipe;
            }).sorted().collect(Collectors.toCollection(LinkedList::new));
            recipeTypeMap.put(pRecipeType, recipes);
        }
    }

    public static LinkedList<AbstractPhialRecipe> getPhialRecipes(Level pLevel) {
        populateRecipeTypeMap(pLevel);
        LinkedList<AbstractPhialRecipe> recipes = (LinkedList<AbstractPhialRecipe>)recipeTypeMap.get(CHEMICAL_PHIAL_TYPE.get());
        for(AbstractPhialRecipe recipe : (LinkedList<AbstractPhialRecipe>)recipeTypeMap.get(POTION_PHIAL_TYPE.get())) {
            if(!recipes.contains(recipe)) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    public static Optional<AbstractPhialRecipe> getPhialRecipe(Predicate<AbstractPhialRecipe> pPredicate, Level pLevel) {
        return getPhialRecipes(pLevel).stream().filter(pPredicate).findFirst();
    }

    public static <R extends AbstractProcessingRecipe> LinkedList<R> getRecipesByGroup(String pGroup, Level pLevel) {
        if (recipeGroupMap.get(pGroup) == null) {
            LinkedList<R> recipes = (LinkedList)pLevel.getRecipeManager().getRecipes().stream().filter((recipe) -> {
                return recipe.getGroup().equals(pGroup);
            }).map((recipe) -> {
                return (AbstractProcessingRecipe)recipe;
            }).sorted().collect(Collectors.toCollection(LinkedList::new));
            recipeGroupMap.put(pGroup, recipes);
        }

        return (LinkedList)recipeGroupMap.get(pGroup);
    }

    public static <R extends AbstractProcessingRecipe> Optional<R> getRecipeByGroupAndId(String pGroup, ResourceLocation pRecipeId, Level pLevel) {
        return (Optional<R>) getRecipesByGroup(pGroup, pLevel).stream().filter((recipe) -> {
            return recipe.getId().equals(pRecipeId);
        }).findFirst();
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }

    static {
        RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Squirtgun.MOD_ID);
        SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Squirtgun.MOD_ID);
        CHEMICAL_PHIAL_TYPE = registerRecipeType("chemical_phial_recipe");
        POTION_PHIAL_TYPE = registerRecipeType("potion_phial_recipe");
        POTION_PHIAL_SERIALIZER = SERIALIZERS.register(
                "potion_phial_serializer",
                () -> {
                    return new PotionPhialRecipeSerializer<>(PotionPhialRecipe::new);
                });
        CHEMICAL_PHIAL_SERIALIZER = SERIALIZERS.register(
                "chemical_phial_serializer",
                () -> new ChemicalPhialRecipeSerializer<>(ChemicalPhialRecipe::new));
        recipesMap = new HashMap<>();
        recipeTypeMap = new LinkedHashMap<>();
        recipeGroupMap = new LinkedHashMap();
    }
}