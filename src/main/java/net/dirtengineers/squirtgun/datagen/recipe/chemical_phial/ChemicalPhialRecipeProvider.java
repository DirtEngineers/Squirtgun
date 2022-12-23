package net.dirtengineers.squirtgun.datagen.recipe.chemical_phial;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

public class ChemicalPhialRecipeProvider {
    Consumer<FinishedRecipe> consumer;

    public ChemicalPhialRecipeProvider(Consumer<FinishedRecipe> pConsumer) {
        this.consumer = pConsumer;
    }

    public static void register(Consumer<FinishedRecipe> pConsumer) {
        (new ChemicalPhialRecipeProvider(pConsumer)).register();
    }

    private void register() {
        ItemRegistration.buildChemical_Fluids();
        for(ChemicalPhial phial : ItemRegistration.CHEMICAL_PHIALS) {
            if (phial.getChemical().getFluidReference().isPresent()) {
                this.buildPhialRecipe(
                        new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId()), 1),
                        new FluidStack(ItemRegistration.CHEMICAL_FLUIDS.get(phial.getChemical()), 1000),
                        new ItemStack(phial, 1));
            }
        }
    }

    private void buildPhialRecipe(ItemStack pPhial, FluidStack pFluid, ItemStack pOutput) {
        ChemicalPhialRecipeBuilder
                .createRecipe(pPhial
                        , pFluid
                        , pOutput
                        , new ResourceLocation(Squirtgun.MOD_ID, (Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pOutput.getItem()))).getPath()
                        )
                )
                .group(Constants.encapsulatorRecipeGroupName)
                .unlockedBy("has_the_recipe",
                        RecipeUnlockedTrigger
                                .unlocked(new ResourceLocation(Squirtgun.MOD_ID,
                                        String.format("%s%s"
                                                , Constants.chemicalPhialCreationRecipeLocationPrefix
                                                , Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pOutput.getItem())).getPath())
                                        )
                                )
                )
                .save(this.consumer);
    }
}
