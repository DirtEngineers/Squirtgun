package net.dirtengineers.squirtgun.common.recipe;

import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import com.smashingmods.alchemylib.api.recipe.ProcessingRecipe;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class PhialRecipe extends AbstractProcessingRecipe {
    private final ItemStack phial;
    private final FluidStack fluid;
    private final ItemStack output;

    public PhialRecipe(ResourceLocation pId, String pGroup, ItemStack pPhial, FluidStack pFluidStack, ItemStack pOutput) {
        super(pId, pGroup);
        this.phial = pPhial;
        this.fluid = pFluidStack;
        this.output = pOutput;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistration.PHIAL_CREATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistration.PHIAL_CREATION_RECIPE_TYPE.get();
    }

    @Override
    public ItemStack assemble(Inventory pContainer) {
        return this.output.copy();
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.of(this.phial.getItem(), this.fluid.getFluid().getBucket()));
    }

    @Override
    public String toString() {
        return String.format("input1=%s, input2=%s outputs=%s", this.phial, this.fluid, this.output);
    }

    public ItemStack getPhialInput() {
        return this.phial;
    }

    public FluidStack getFluidInput() {
        return this.fluid;
    }

    @Override
    public ProcessingRecipe copy() {
        return null;
    }

    @Override
    public Object getInput() {
        return null;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    //TODO: resolve this
    @Override
    public int compareTo(@NotNull AbstractProcessingRecipe abstractProcessingRecipe) {
        return getId().compareNamespaced(abstractProcessingRecipe.getId());
    }
}
