package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(DataGenerator pGenerator) { super(pGenerator); }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pConsumer) {
//        AtomizerRecipeProvider.register(pConsumer);
        this.generateGunRecipe(pConsumer);
        this.generateMagazineRecipe(pConsumer);
        this.generateMachineRecipes(pConsumer);
    }


    private void generateGunRecipe(Consumer<FinishedRecipe> pConsumer){
        Item squirtgun = ItemRegistration.SQUIRTGUNITEM.get().asItem();
        ShapedRecipeBuilder
                .shaped(squirtgun)
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('R', Items.REDSTONE)
                .pattern("IRP")
                .pattern("IP ")
                .pattern("I  ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.PISTON}).build()))
                .save(pConsumer);
    }

    private void generateMagazineRecipe(Consumer<FinishedRecipe> pConsumer){
        Item squirtgun = ItemRegistration.SQUIRTMAGAZINEITEM.get().asItem();
        ShapedRecipeBuilder
                .shaped(squirtgun)
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('B', Items.GLASS_BOTTLE)
                .pattern("IBI")
                .pattern("IBI")
                .pattern("IPI")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.PISTON}).build()))
                .save(pConsumer);
    }

    private void generateMachineRecipes(Consumer<FinishedRecipe> pConsumer) {
//        Item atomizer = ((Block) BlockRegistry.ATOMIZER.get()).asItem();
//        ShapedRecipeBuilder
//                .shaped(atomizer)
//                .group("machines")
//                .define('I', Items.IRON_INGOT)
//                .define('P', Items.PISTON)
//                .define('R', Items.REDSTONE)
//                .define('C', Items.CAULDRON)
//                .pattern("IPI")
//                .pattern("CRC")
//                .pattern("IPI")
//                .unlockedBy(
//                        "has_item",
//                        inventoryTrigger(
//                                new ItemPredicate[]{ItemPredicate.Builder.item().of(new ItemLike[]{Items.IRON_INGOT}).build()}))
//                .save(pConsumer);
    }
}
