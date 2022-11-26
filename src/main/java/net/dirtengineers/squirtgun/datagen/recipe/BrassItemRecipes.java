package net.dirtengineers.squirtgun.datagen.recipe;

import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static net.dirtengineers.squirtgun.datagen.recipe.ItemDefinitions.*;

public class BrassItemRecipes extends net.minecraft.data.recipes.RecipeProvider {

    public BrassItemRecipes(DataGenerator pGenerator) { super(pGenerator); }

   public static void buildRecipes(Consumer<FinishedRecipe> pConsumer) {
       generateBrassBlend(pConsumer);
       generateBrassNugget(pConsumer);
       generateBrassIngot(pConsumer);
       generateBrassBlock(pConsumer);
   }

    public static void generateBrassBlend(Consumer<FinishedRecipe> pConsumer) {
        ShapelessRecipeBuilder
                .shapeless(BRASS_BLEND, 3)
                .group(Squirtgun.MOD_ID)
                .requires(COPPER_DUST, 2)
                .requires(ZINC_DUST)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{COPPER_DUST, ZINC_DUST}).build()))
                .save(pConsumer);
    }

    private static void generateBrassNugget(Consumer<FinishedRecipe> pConsumer) {
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(BRASS_BLEND)
                        , BRASS_NUGGET
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLEND}).build()))
                .save(pConsumer, getBlastingRecipeName(BRASS_NUGGET));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(BRASS_BLEND)
                        , BRASS_NUGGET
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLEND}).build()))
                .save(pConsumer, getSmeltingRecipeName(BRASS_NUGGET));

        ShapelessRecipeBuilder
                .shapeless(BRASS_NUGGET, 9)
                .group(Squirtgun.MOD_ID)
                .requires(BRASS_INGOT)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_NUGGET, BRASS_INGOT));

    }

    private static void generateBrassIngot(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(BRASS_INGOT, 9)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_BLOCK)
                .pattern("   ")
                .pattern(" X ")
                .pattern("   ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLOCK}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_INGOT, BRASS_BLOCK));

        ShapedRecipeBuilder
                .shaped(BRASS_INGOT)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_NUGGET)
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_INGOT, BRASS_NUGGET));
    }

    private static void generateBrassBlock(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(BRASS_BLOCK)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_INGOT)
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_INGOT}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_BLOCK, BRASS_INGOT));
    }
}
