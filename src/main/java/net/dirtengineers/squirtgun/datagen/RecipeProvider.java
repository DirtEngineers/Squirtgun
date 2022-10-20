package net.dirtengineers.squirtgun.datagen;

import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.common.item.BaseSquirtMagazine;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(DataGenerator pGenerator) { super(pGenerator); }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pConsumer) {
        this.generateGunRecipe(pConsumer);
        this.generateMagazineRecipes(pConsumer);
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

    private void generateMagazineRecipes(Consumer<FinishedRecipe> pConsumer){
        this.generateEmptyMagazineRecipe(pConsumer);
        for(BaseSquirtMagazine magazine : ItemRegistration.MAGAZINES.keySet()){
            BucketItem bucketItem = this.getBucket(magazine);
            if(bucketItem != null) {
                ShapedRecipeBuilder
                        .shaped(magazine)
                        .define('I', Items.IRON_INGOT)
                        .define('P', Items.PISTON)
                        .define('B', bucketItem)
                        .pattern("IBI")
                        .pattern("IBI")
                        .pattern("IPI")
                        .unlockedBy(
                                "has_item",
                                inventoryTrigger(
                                        ItemPredicate.Builder.item().of(new ItemLike[]{Items.PISTON, bucketItem}).build()))
                        .save(pConsumer);
            }
        }
    }

    private BucketItem getBucket(BaseSquirtMagazine magazine){
        BucketItem bucketItem = null;
        if(FluidRegistry.getFluidTypeByName(ItemRegistration.MAGAZINES.get(magazine).getChemicalName()).isPresent()) {
            FluidType fluidType = FluidRegistry.getFluidTypeByName(ItemRegistration.MAGAZINES.get(magazine).getChemicalName()).get();

            for (BucketItem bucketItem1 : FluidRegistry.getBuckets().toList()) {
                if (bucketItem1.getFluid().getFluidType() == fluidType)
                    bucketItem = bucketItem1;
            }
        }
        return bucketItem;
    }

    private void generateEmptyMagazineRecipe(Consumer<FinishedRecipe> pConsumer){
        Item squirtgun_magazine = ItemRegistration.SQUIRTMAGAZINEITEM.get().asItem();
        ShapedRecipeBuilder
                .shaped(squirtgun_magazine)
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
