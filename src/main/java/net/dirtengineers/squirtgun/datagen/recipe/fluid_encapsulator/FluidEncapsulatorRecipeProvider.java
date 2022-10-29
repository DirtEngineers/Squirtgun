package net.dirtengineers.squirtgun.datagen.recipe.fluid_encapsulator;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.datagen.DatagenUtility;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Consumer;

public class FluidEncapsulatorRecipeProvider {
    Consumer<FinishedRecipe> consumer;

    public FluidEncapsulatorRecipeProvider(Consumer<FinishedRecipe> pConsumer) {
        this.consumer = pConsumer;
    }

    public static void register(Consumer<FinishedRecipe> pConsumer) {
        (new FluidEncapsulatorRecipeProvider(pConsumer)).register();
    }

    private void register() {
        ItemRegistration.buildChemical_Fluids();
        for(Map.Entry<BasePhial, Chemical> phialEntry : ItemRegistration.PHIALS.entrySet()) {
            this.encapsulator(
                    new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL_ITEM.getId()), 1),
                    new FluidStack(ItemRegistration.CHEMICAL_FLUIDS.get(phialEntry.getValue()), 1000),
                    new ItemStack(phialEntry.getKey(), 1));
        }
    }

    private void encapsulator(ItemStack pPhial, FluidStack pFluidStack, ItemStack pOutput) {
        FluidEncapsulatorRecipeBuilder
                .createRecipe(pPhial, pFluidStack, pOutput, ForgeRegistries.ITEMS.getKey(pOutput.getItem()))
                .group(String.format("%s:fluid_encapsulator", Squirtgun.MOD_ID))
                .unlockedBy("has_the_recipe",
                        RecipeUnlockedTrigger
                                .unlocked(DatagenUtility.getLocation(pOutput, "fluid_encapsulator")))
                .save(this.consumer);
    }
}
