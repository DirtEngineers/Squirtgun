package net.dirtengineers.squirtgun.common.recipe;

import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractPhialRecipe extends AbstractProcessingRecipe {
    public AbstractPhialRecipe(ResourceLocation pRecipeId, String pGroup) {
        super(pRecipeId, pGroup);
    }

    @Override
    @Nonnull
    public abstract List<ItemStack> getInput();

    @Override
    @Nonnull
    public abstract List<ItemStack> getOutput();

    @Override
    public int compareTo(@NotNull AbstractProcessingRecipe abstractProcessingRecipe) {
        return 0;
    }

    public boolean matchInputs(List<ItemStack> pStacks) {
        return false;
    }
}
