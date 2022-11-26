package net.dirtengineers.squirtgun.datagen.recipe;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static net.dirtengineers.squirtgun.datagen.recipe.ItemDefinitions.*;

public class MiscRecipes extends net.minecraft.data.recipes.RecipeProvider {
    public MiscRecipes(DataGenerator pGenerator) { super(pGenerator); }

    public static void buildRecipes(Consumer<FinishedRecipe> pConsumer) {
        generateQuartzShard(pConsumer);
        generatePhialCap(pConsumer);
        generateEmptyPhial(pConsumer);
    }

//    private static void generateLuminousPaint(Consumer<FinishedRecipe> pConsumer) {
//        ShapelessRecipeBuilder
//                .shapeless(LUMINOUS_PAINT)
//                .group(Squirtgun.MOD_ID)
//                .requires(RADIUM_DUST)
//                .requires(ZINC_SULPHIDE_DUST)
//                .requires(STARCH_DUST)
//                .requires(AMIDE_DUST)
//                .requires(Items.WATER_BUCKET)
//                .group(Squirtgun.MOD_ID)
//                .unlockedBy(
//                        "has_item",
//                        inventoryTrigger(
//                                ItemPredicate.Builder.item().of(new ItemLike[]{RADIUM_DUST, ZINC_SULPHIDE_DUST, STARCH_DUST, AMIDE_DUST, Items.WATER_BUCKET}).build()))
//                .save(pConsumer);
//    }

    private static void generateQuartzShard(Consumer<FinishedRecipe> pConsumer) {
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(SILICA)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{SILICA}).build()))
                .save(pConsumer, String.format("%s_from_blasting", getConversionRecipeName(QUARTZ_SHARD, SILICA)));

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(Items.QUARTZ)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.QUARTZ}).build()))
                .save(pConsumer, String.format("%s_from_blasting", getConversionRecipeName(QUARTZ_SHARD, Items.QUARTZ)));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(SILICA)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{SILICA}).build()))
                .save(pConsumer, String.format("%s_from_smelting", getConversionRecipeName(QUARTZ_SHARD, SILICA)));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(Items.QUARTZ)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.QUARTZ}).build()))
                .save(pConsumer, String.format("%s_from_smelting", getConversionRecipeName(QUARTZ_SHARD, Items.QUARTZ)));
    }

    private static void generatePhialCap(Consumer<FinishedRecipe> pConsumer) {
        ShapelessRecipeBuilder
                .shapeless(PHIAL_CAP)
                .group(Squirtgun.MOD_ID)
                .requires(BRASS_NUGGET, 2)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET}).build()))
                .save(pConsumer);
    }

    private static void generateEmptyPhial(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(ItemRegistration.PHIAL.get().asItem())
                .group(Squirtgun.MOD_ID)
                .define('C', PHIAL_CAP)
                .define('Q', QUARTZ_SHARD)
                .pattern(" C ")
                .pattern("Q Q")
                .pattern(" Q ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{PHIAL_CAP, QUARTZ_SHARD}).build()))
                .save(pConsumer);
    }
}
