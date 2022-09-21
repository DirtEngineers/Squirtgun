package net.dirtengineers.squirtgun.common.util;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Common {

    public static List<Fluid> AmmunitionFluids = new ArrayList<>();

    public static final Predicate<FluidStack> SQUIRT_FLUIDS_ONLY =
            (fluidStack) -> Common.AmmunitionFluids.contains(fluidStack.getFluid());
}

