package net.dirtengineers.squirtgun.datagen.recipe.fluid_encapsulator.creation;

import net.dirtengineers.squirtgun.Config;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PhialTypeCreationRecipeBuilder implements RecipeBuilder {
    private String group;
    private final ResourceLocation recipeId;
    private final ItemStack phial;
    private final FluidStack fluid;
    private final ItemStack result;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    public PhialTypeCreationRecipeBuilder(ItemStack pPhial, FluidStack pfluidStack, ItemStack pOutput, ResourceLocation pRecipeId) {
        this.phial = pPhial;
        this.fluid = pfluidStack;
        this.result = pOutput;
        this.recipeId = pRecipeId;
    }

    public static PhialTypeCreationRecipeBuilder createRecipe(ItemStack pInput1, FluidStack pInput2, ItemStack pOutput, ResourceLocation pRecipeId) {
        return new PhialTypeCreationRecipeBuilder(pInput1, pInput2, pOutput, pRecipeId);
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancementBuilder
                .addCriterion(pCriterionName, pCriterionTrigger)
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(
                        new ResourceLocation(Squirtgun.MOD_ID, String.format("%s_phial", this.recipeId.getPath()))))
                .requirements(RequirementsStrategy.OR);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    private String getFluidName(FluidType pFluidType){
        return ItemRegistration.CHEMICAL_FLUIDS
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getFluidType() == pFluidType)
                .findFirst()
                .map(entry -> entry.getKey().getChemicalName())
                .orElse("");
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        ResourceLocation recipeLocation = new ResourceLocation(
                Squirtgun.MOD_ID
                ,String.format("%screate_%s"
                ,Config.Common.fluidEncapsulatorPhialCreationRecipeLocationPrefix
                ,pRecipeId.getPath()));
        ResourceLocation advancementLocation = new ResourceLocation(
                Squirtgun.MOD_ID
                ,String.format("%screate_%s"
                ,Config.Common.fluidEncapsulatorPhialCreationAdvancementLocationPrefix
                ,pRecipeId.getPath()));
        pFinishedRecipeConsumer.accept(
                new PhialTypeCreationRecipeResult(
                        this.group,
                        this.advancementBuilder,
                        recipeLocation,
                        advancementLocation,
                        this.phial,
                        this.fluid,
                        this.result));
    }
}
