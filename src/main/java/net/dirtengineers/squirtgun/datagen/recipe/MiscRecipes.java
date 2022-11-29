package net.dirtengineers.squirtgun.datagen.recipe;

import com.smashingmods.chemlib.registry.ItemRegistry;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static net.dirtengineers.squirtgun.registry.ItemRegistration.FUSED_QUARTZ_SHARD;

public class MiscRecipes extends net.minecraft.data.recipes.RecipeProvider {
    public static final Item SILICA = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_COMPOUNDS, "silicon_dioxide").get();

    public MiscRecipes(DataGenerator pGenerator) { super(pGenerator); }

    public static void buildRecipes(Consumer<FinishedRecipe> pConsumer) {
        generateQuartzShardSmelting(pConsumer);
        generateQuartzShardBlasting(pConsumer);
    }

    private static void generateQuartzShardSmelting(Consumer<FinishedRecipe> pConsumer) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(SILICA)
                        , FUSED_QUARTZ_SHARD.get()
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{SILICA}).build()))
                .save(pConsumer, String.format("%s_from_smelting", getConversionRecipeName(FUSED_QUARTZ_SHARD.get(), SILICA)));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(Items.QUARTZ)
                        , FUSED_QUARTZ_SHARD.get()
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.QUARTZ}).build()))
                .save(pConsumer, String.format("%s_from_smelting", getConversionRecipeName(FUSED_QUARTZ_SHARD.get(), Items.QUARTZ)));
    }

    private static void generateQuartzShardBlasting(Consumer<FinishedRecipe> pConsumer) {
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(SILICA)
                        , FUSED_QUARTZ_SHARD.get()
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{SILICA}).build()))
                .save(pConsumer, String.format("%s_from_blasting", getConversionRecipeName(FUSED_QUARTZ_SHARD.get(), SILICA)));

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(Items.QUARTZ)
                        , FUSED_QUARTZ_SHARD.get()
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.QUARTZ}).build()))
                .save(pConsumer, String.format("%s_from_blasting", getConversionRecipeName(FUSED_QUARTZ_SHARD.get(), Items.QUARTZ)));
    }
}
