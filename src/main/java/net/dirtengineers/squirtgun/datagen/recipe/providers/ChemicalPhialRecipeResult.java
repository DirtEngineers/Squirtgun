package net.dirtengineers.squirtgun.datagen.recipe.providers;

import com.google.gson.JsonObject;
import net.dirtengineers.squirtgun.common.registry.RecipeRegistration;
import net.dirtengineers.squirtgun.datagen.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class ChemicalPhialRecipeResult implements FinishedRecipe {
    private final String group;
    private final Advancement.Builder advancementBuilder;
    private final ResourceLocation id;
    private final ResourceLocation advancementId;
    private final ItemStack phial;
    private final FluidStack fluid;
    private final ItemStack result;

    public ChemicalPhialRecipeResult(String pGroup, Advancement.Builder pBuilder, ResourceLocation pId, ResourceLocation pAdvancementId, ItemStack pPhial, FluidStack pFluid, ItemStack pResult) {
        this.group = pGroup;
        this.advancementBuilder = pBuilder;
        this.id = pId;
        this.advancementId = pAdvancementId;
        this.phial = pPhial;
        this.fluid = pFluid;
        this.result = pResult;
    }

    public void serializeRecipeData(JsonObject pJson) {
        if (!this.group.isEmpty()) {
            pJson.addProperty("group", this.group);
        }
        Util.itemStackToJson(pJson, "input1", this.phial);
        Util.fluidStacktoJson(pJson, "input2", this.fluid);
        Util.itemStackToJson(pJson, "result", this.result);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getType() { return RecipeRegistration.PHIAL_CREATION_SERIALIZER.get(); }

    public @Nullable JsonObject serializeAdvancement() {
        return this.advancementBuilder.serializeToJson();
    }

    public @Nullable ResourceLocation getAdvancementId() {
        return this.advancementId;
    }
}
