package net.dirtengineers.squirtgun.common.recipe;

import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractPhialRecipe extends AbstractProcessingRecipe {
    public AbstractPhialRecipe(ResourceLocation pRecipeId, String pGroup) {
        super(pRecipeId, pGroup);
    }

    @Override
    public AbstractPhialRecipe copy() {
        return null;
    }

    @Override
    public List<ItemStack> getInput() {
        return null;
    }

    @Override
    public List<ItemStack> getOutput() {
        return null;
    }

    @Override
    public int compareTo(@NotNull AbstractProcessingRecipe abstractProcessingRecipe) {
        return 0;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    public boolean matchInputs(List<ItemStack> pStacks) {
        return false;
    }
}
