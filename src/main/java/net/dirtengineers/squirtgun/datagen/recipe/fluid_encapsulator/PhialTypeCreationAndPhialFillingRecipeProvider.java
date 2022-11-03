package net.dirtengineers.squirtgun.datagen.recipe.fluid_encapsulator;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Config;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BasePhialItem;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.datagen.recipe.fluid_encapsulator.creation.PhialTypeCreationRecipeBuilder;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PhialTypeCreationAndPhialFillingRecipeProvider {
    Consumer<FinishedRecipe> consumer;

    public PhialTypeCreationAndPhialFillingRecipeProvider(Consumer<FinishedRecipe> pConsumer) {
        this.consumer = pConsumer;
    }

    public static void register(Consumer<FinishedRecipe> pConsumer) {
        (new PhialTypeCreationAndPhialFillingRecipeProvider(pConsumer)).register();
    }

    private void register() {
        ItemRegistration.buildChemical_Fluids();
        for(Map.Entry<BasePhialItem, Chemical> phialEntry : ItemRegistration.PHIALS.entrySet()) {
            if (phialEntry.getValue().getFluidTypeReference().isPresent()) {
                this.buildPhialCreationRecipe(
                        new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL_ITEM.getId()), 1),
                        new FluidStack(ItemRegistration.CHEMICAL_FLUIDS.get(phialEntry.getValue()), 1000),
                        new ItemStack(phialEntry.getKey(), 1));

                this.buildPhialFillingRecipe(
                        new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL_ITEM.getId()), 1),
                        new FluidStack(ItemRegistration.CHEMICAL_FLUIDS.get(phialEntry.getValue()), 1000),
                        new ItemStack(phialEntry.getKey(), 1));
            }
        }
    }

    private void buildPhialCreationRecipe(ItemStack pPhial, FluidStack pFluid, ItemStack pOutput) {
        PhialTypeCreationRecipeBuilder
                .createRecipe(pPhial
                        ,pFluid
                        ,pOutput
                        ,new ResourceLocation(
                                Squirtgun.MOD_ID
                                ,String.format("create_%s", (Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pOutput.getItem()))).getPath())))
                .group(String.format("%s:fluid_encapsulator", Squirtgun.MOD_ID))
                .unlockedBy("has_the_recipe",
                        RecipeUnlockedTrigger
                                .unlocked(new ResourceLocation(Squirtgun.MOD_ID,
                                        String.format("%s%s%s"
                                                ,Config.Common.fluidEncapsulatorPhialCreationRecipeLocationPrefix
                                                ,"create_"
                                                ,Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pOutput.getItem())).getPath()))
                                ))
                .save(this.consumer);
    }
//create_pentane_phial
    private void buildPhialFillingRecipe(ItemStack pPhial, FluidStack pFluid, ItemStack pOutput) {
//        PhialTypeCreationRecipeBuilder
//                .createRecipe(pPhial
//                        ,pFluid
//                        ,pOutput
//                        ,new ResourceLocation(
//                                Squirtgun.MOD_ID
//                                ,String.format("create_%s", (Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pOutput.getItem()))).getPath())))
//                .group(String.format("%s:fluid_encapsulator", Squirtgun.MOD_ID))
//                .unlockedBy("has_the_recipe",
//                        RecipeUnlockedTrigger
//                                .unlocked(DatagenUtility.getLocation(pOutput, "fluid_encapsulator/creation")))
//                .save(this.consumer);
    }
}
