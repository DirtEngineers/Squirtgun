package net.dirtengineers.squirtgun.common.recipe;

import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class ChemicalPhialRecipe extends AbstractPhialRecipe {
    private final ItemStack phialStack;
    private final FluidStack fluidStack;
    private final ItemStack outputStack;

    public ChemicalPhialRecipe(ResourceLocation pId, String pGroup, ItemStack pPhial, FluidStack pFluidStack, ItemStack pOutput) {
        super(pId, pGroup);
        this.phialStack = pPhial;
        this.fluidStack = pFluidStack;
        this.outputStack = pOutput;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistration.CHEMICAL_PHIAL_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return RecipeRegistration.CHEMICAL_PHIAL_TYPE;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull Inventory pContainer) {
        return this.outputStack.copy();
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return outputStack;
    }

    @Override
    public String toString() {
        return String.format("input1=%s, input2=%s outputs=%s", this.phialStack, this.fluidStack, this.outputStack);
    }

    public ItemStack getPhialInput() {
        return phialStack;
    }

    public FluidStack getFluidInput() {
        return fluidStack;
    }

    @Override
    @Nonnull
    public ChemicalPhialRecipe copy() {
        return new ChemicalPhialRecipe(this.getId(), this.getGroup(), this.phialStack, this.fluidStack, this.outputStack);
    }

    @Override
    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.of(this.phialStack.getItem(), this.fluidStack.getFluid().getBucket()));
    }

    @Override
    @Nonnull
    public List<ItemStack> getInput() {
        LinkedList<ItemStack> outList = new LinkedList<>();
        outList.add(new ItemStack(this.phialStack.getItem()));
        outList.add(new ItemStack(this.fluidStack.getFluid().getBucket()));
        return outList;
    }

    @Nonnull
    public List<ItemStack> getOutput() {
        return List.of(outputStack);
    }

    //TODO: resolve this
    @Override
    public int compareTo(@Nonnull AbstractProcessingRecipe abstractProcessingRecipe) {
        return getId().compareNamespaced(abstractProcessingRecipe.getId());
    }

    @Override
    public boolean matchInputs(List<ItemStack> pStacks) {
        boolean hasPhial = false;
        boolean hasFluid = false;
        for (ItemStack pStack : pStacks) {
            if (pStack.getItem() instanceof BasePhial) {
                hasPhial = true;
            }
            if (pStack.getItem() instanceof BucketItem) {
                if (((BucketItem) pStack.getItem()).getFluid() == fluidStack.getFluid()) {
                    hasFluid = true;
                }
            }
        }
        return hasPhial && hasFluid;
    }
}
