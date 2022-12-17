package net.dirtengineers.squirtgun.datagen.recipe.chemical_phial;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ChemicalPhialRecipeBuilder implements RecipeBuilder {
    private String group;
    private final ResourceLocation recipeId;
    private final ItemStack phial;
    private final FluidStack fluid;
    private final ItemStack result;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    public ChemicalPhialRecipeBuilder(ItemStack pPhial, FluidStack pfluidStack, ItemStack pOutput, ResourceLocation pRecipeId) {
        this.phial = pPhial;
        this.fluid = pfluidStack;
        this.result = pOutput;
        this.recipeId = pRecipeId;
    }

    public static ChemicalPhialRecipeBuilder createRecipe(ItemStack pInput1, FluidStack pInput2, ItemStack pOutput, ResourceLocation pRecipeId) {
        return new ChemicalPhialRecipeBuilder(pInput1, pInput2, pOutput, pRecipeId);
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancementBuilder
                .addCriterion(pCriterionName, pCriterionTrigger)
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(
                        new ResourceLocation(Squirtgun.MOD_ID, this.recipeId.getPath())))
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

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        ResourceLocation recipeLocation = new ResourceLocation(
                Squirtgun.MOD_ID, String.format("%s%s", Constants.chemicalPhialCreationRecipeLocationPrefix, pRecipeId.getPath()));
        ResourceLocation advancementLocation = new ResourceLocation(
                Squirtgun.MOD_ID, String.format("%s%s", Constants.chemicalPhialCreationAdvancementLocationPrefix, pRecipeId.getPath()));
        pFinishedRecipeConsumer.accept(
                new ChemicalPhialRecipeResult(
                        this.group,
                        this.advancementBuilder,
                        recipeLocation,
                        advancementLocation,
                        this.phial,
                        this.fluid,
                        this.result));
    }
}
