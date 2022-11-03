package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.datagen.recipe.fluid_encapsulator.PhialTypeCreationAndPhialFillingRecipeProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(DataGenerator pGenerator) { super(pGenerator); }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pConsumer) {
        this.generateGunRecipe(pConsumer);
        this.generateEmptyPhialRecipe(pConsumer);
        PhialTypeCreationAndPhialFillingRecipeProvider.register(pConsumer);
    }

    private void generateGunRecipe(Consumer<FinishedRecipe> pConsumer){
        Item squirtgun = ItemRegistration.SQUIRTGUNITEM.get().asItem();
        ShapedRecipeBuilder
                .shaped(squirtgun)
                .group("Squirtgun")
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('R', Items.REDSTONE)
                .pattern("IRP")
                .pattern("IP ")
                .pattern("I  ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.PISTON, Items.REDSTONE, Items.IRON_INGOT}).build()))
                .save(pConsumer);
    }

    private void generateEmptyPhialRecipe(Consumer<FinishedRecipe> pConsumer) {
        BucketItem bucketItem = (BucketItem) Items.BUCKET;
        ShapedRecipeBuilder
                .shaped(ItemRegistration.PHIAL_ITEM.get().asItem())
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('B', bucketItem)
                .pattern("IBI")
                .pattern("IBI")
                .pattern("IPI")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.PISTON, bucketItem, Items.IRON_INGOT}).build()))
                .save(pConsumer);
    }
}
