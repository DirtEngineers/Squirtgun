package net.dirtengineers.squirtgun.common.registry;

import com.smashingmods.chemlib.registry.FluidRegistry;
import net.minecraftforge.fluids.FluidType;

public class FluidRegistration extends FluidRegistry {
    public static void registerFluids(String pName, FluidType.Properties pFluidProperties, int pColor, int pSlopeFindDistance, int pDecreasePerBlock) {
        registerFluid(pName, pFluidProperties, pColor, pSlopeFindDistance, pDecreasePerBlock);
    }
}