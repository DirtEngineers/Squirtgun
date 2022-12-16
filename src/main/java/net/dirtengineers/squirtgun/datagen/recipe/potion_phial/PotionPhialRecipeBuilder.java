package net.dirtengineers.squirtgun.datagen.recipe.potion_phial;

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
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PotionPhialRecipeBuilder implements RecipeBuilder {
    private String group;
    private final ResourceLocation recipeId;
    private final ItemStack phial;
    private final Potion potion;
    private final ItemStack phialOutput;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    public PotionPhialRecipeBuilder(ItemStack pPhial, Potion pPotion, ItemStack pPhialOutput, ResourceLocation pRecipeId) {
        phial = pPhial;
        potion = pPotion;
        phialOutput = pPhialOutput;
        recipeId = pRecipeId;
    }

    public static PotionPhialRecipeBuilder createRecipe(ItemStack pInput1, Potion pInput2, ItemStack pPhialOutput, ResourceLocation pRecipeId) {
        return new PotionPhialRecipeBuilder(pInput1, pInput2, pPhialOutput, pRecipeId);
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
        return this.phialOutput.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        ResourceLocation recipeLocation = new ResourceLocation(
                Squirtgun.MOD_ID, String.format("%s%s", Constants.potionPhialCreationRecipeLocationPrefix, pRecipeId.getPath()));
        ResourceLocation advancementLocation = new ResourceLocation(
                Squirtgun.MOD_ID, String.format("%s%s", Constants.potionPhialCreationAdvancementLocationPrefix, pRecipeId.getPath()));
        pFinishedRecipeConsumer.accept(
                new PotionPhialRecipeResult(
                        group
                        ,advancementBuilder
                        ,recipeLocation
                        ,advancementLocation
                        ,phial
                        ,potion
                        ,phialOutput));
    }
}
