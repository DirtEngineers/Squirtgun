package net.dirtengineers.squirtgun.common.util;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.Compound;
import com.smashingmods.chemlib.common.items.CompoundItem;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Objects;
import java.util.function.Predicate;

public class CustomFluidStorage extends FluidTank {

    public static final int EMPTY_FLUID_COLOR = 0XFFFFFFFF;
    public static String EMPTY_FLUID_NAME = "minecraft:empty";

    public CustomFluidStorage(int pCapacity, FluidStack pFluidStack) {
        super(pCapacity);
        this.fill(pFluidStack, FluidAction.EXECUTE);
    }

    public CustomFluidStorage(int pCapacity, Predicate<FluidStack> pValidator) {
        super(pCapacity);
        this.setValidator(pValidator);
    }

    public FluidStack drainContainer(int maxDrain, FluidAction action) {
        return this.drain(maxDrain, action);
    }

    public FluidType getFluidType() { return this.fluid.getFluid().getFluidType(); }

    public Fluid getRawFluid(){ return this.getFluid().getFluid(); }

    public void setAmount(int pValue) {
        this.fluid.setAmount(pValue);
    }

    public void setFluid(FluidStack pFluidStack) {
        this.drain(this.capacity, FluidAction.EXECUTE);
        this.fill(pFluidStack, FluidAction.EXECUTE);
    }

    public int getFluidColor() {
        if (this.isEmptyFluid()) return EMPTY_FLUID_COLOR;
        return IClientFluidTypeExtensions.of(this.getFluidType()).getTintColor();
    }

    public boolean isEmptyFluid() {
        return Objects.equals(this.getFluidType().toString(), EMPTY_FLUID_NAME);
    }

    public String getFriendlyFluidName(){
        if(this.isEmptyFluid()) return "empty";
        else {
            Chemical chemical = Common.Ammunition.get(this.getRawFluid());
            return chemical instanceof Compound ? I18n.get(((CompoundItem) chemical).getDescriptionId()) : chemical.getChemicalName();
        }
    }

    public FluidStack getFluidStack() {
        return this.fluid;
    }
}
