package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.datagen.recipe.BrassItemRecipes;
import net.dirtengineers.squirtgun.datagen.recipe.ComponentRecipes;
import net.dirtengineers.squirtgun.datagen.recipe.MiscRecipes;
import net.dirtengineers.squirtgun.datagen.recipe.providers.ChemicalPhialRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class RecipeGenerator extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeGenerator(DataGenerator pGenerator) {
        super(pGenerator);
    }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pConsumer) {
        BrassItemRecipes.buildRecipes(pConsumer);
        ComponentRecipes.buildRecipes(pConsumer);
        MiscRecipes.buildRecipes(pConsumer);
        ChemicalPhialRecipeProvider.register(pConsumer);
    }
}
