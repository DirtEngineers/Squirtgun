package net.dirtengineers.squirtgun.common.recipe.fluid_encapsulator;

import net.dirtengineers.squirtgun.common.recipe.AbstractSquirtgunRecipe;
import net.dirtengineers.squirtgun.common.registry.RecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public class FluidEncapsulatorRecipe  extends AbstractSquirtgunRecipe {
    private final ItemStack phial;
    private final FluidStack fluid;
    private final ItemStack output;

    public FluidEncapsulatorRecipe(ResourceLocation pId, String pGroup, ItemStack pPhial, FluidStack pFluidStack, ItemStack pOutput) {
        super(pId, pGroup);
        this.phial = pPhial;
        this.fluid = pFluidStack;
        this.output = pOutput;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistration.FLUID_ENCAPSULATOR_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return RecipeRegistration.FLUID_ENCAPSULATOR_TYPE.get();
    }

    public ItemStack assemble(Inventory pContainer) {
        return this.output;
    }

    public ItemStack getResultItem() {
        return this.output;
    }

    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.of(this.phial.getItem(), this.fluid.getFluid().getBucket()));
    }

    public String toString() {
        return String.format("input1=%s, input2=%s outputs=%s", this.phial, this.fluid, this.output);
    }

    public ItemStack getInput1() {
        return this.phial;
    }

    public FluidStack getInput2() {
        return this.fluid;
    }

    public ItemStack getOutput() {
        return this.output;
    }
}
