package net.dirtengineers.squirtgun.datagen.recipe.potion_phial;

import com.google.gson.JsonObject;
import net.dirtengineers.squirtgun.datagen.Util;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class PotionPhialRecipeResult implements FinishedRecipe {
    private final String group;
    private final Advancement.Builder advancementBuilder;
    private final ResourceLocation id;
    private final ResourceLocation advancementId;
    private final ItemStack phial;
    private final Potion potion;
    private final ItemStack phialResult;
    private final ItemStack bottleResult;

    public PotionPhialRecipeResult(String pGroup
            , Advancement.Builder pBuilder
            , ResourceLocation pId
            , ResourceLocation pAdvancementId
            , ItemStack pPhial
            , Potion pPotion
            , ItemStack pPhialResult
            , ItemStack pBottleResult) {
        group = pGroup;
        advancementBuilder = pBuilder;
        id = pId;
        advancementId = pAdvancementId;
        phial = pPhial;
        potion = pPotion;
        phialResult = pPhialResult;
        bottleResult = pBottleResult;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        if (!this.group.isEmpty()) {
            pJson.addProperty("group", group);
        }
        Util.itemStackToJson(pJson, "input1", phial);
        Util.potionToJson(pJson, "input2", potion);
        Util.itemStackToJson(pJson, "output1", phialResult);
        Util.itemStackToJson(pJson, "output2", bottleResult);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return RecipeRegistration.POTION_PHIAL_SERIALIZER.get();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return this.advancementBuilder.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return this.advancementId;
    }
}
