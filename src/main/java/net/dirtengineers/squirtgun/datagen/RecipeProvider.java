package net.dirtengineers.squirtgun.datagen;

import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.datagen.recipe.fluid_encapsulator.FluidEncapsulatorRecipeProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(DataGenerator pGenerator) { super(pGenerator); }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pConsumer) {
        this.generateGunRecipe(pConsumer);
        this.generateEmptyPhialRecipe(pConsumer);
        FluidEncapsulatorRecipeProvider.register(pConsumer);
//        this.generatePhialRecipes(pConsumer);
        this.generateMachineRecipes(pConsumer);
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

    private BucketItem getBucket(BasePhial phial) {
        BucketItem bucketItem = (BucketItem) requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:water_bucket")));
        String fluidTypename = ItemRegistration.PHIALS.get(phial).getChemicalName();
        if (FluidRegistry.getFluidTypeByName(fluidTypename).isPresent()) {
            FluidType fluidType = FluidRegistry.getFluidTypeByName(fluidTypename).get();

            for (BucketItem bucketItem1 : FluidRegistry.getBuckets().toList()) {
                if (bucketItem1.getFluid().getFluidType() == fluidType)
                    bucketItem = bucketItem1;
            }
        }
        return requireNonNull(bucketItem);
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
