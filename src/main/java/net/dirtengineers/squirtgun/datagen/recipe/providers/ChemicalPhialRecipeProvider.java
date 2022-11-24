package net.dirtengineers.squirtgun.datagen.recipe.providers;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
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
        for(Map.Entry<ChemicalPhial, Chemical> phialEntry : ItemRegistration.PHIALS.entrySet()) {
            if (phialEntry.getValue().getFluidTypeReference().isPresent()) {
                this.buildPhialCreationRecipe(
                        new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId()), 1),
                        new FluidStack(ItemRegistration.CHEMICAL_FLUIDS.get(phialEntry.getValue()), 1000),
                        new ItemStack(phialEntry.getKey(), 1));
            }
        }
    }

    private void buildPhialCreationRecipe(ItemStack pPhial, FluidStack pFluid, ItemStack pOutput) {
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
                                                , Constants.encapsulatorPhialCreationRecipeLocationPrefix
                                                , Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pOutput.getItem())).getPath())
                                        )
                                )
                )
                .save(this.consumer);
    }
}
