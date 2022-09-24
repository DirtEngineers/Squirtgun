package net.dirtengineers.squirtgun.common.util;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Common {

    public static List<Fluid> AmmunitionFluids = new ArrayList<>();

    public static List<FluidType> AmmunitionFluids1 = new ArrayList<>();

    public static final Predicate<FluidStack> SQUIRT_FLUIDS_ONLY =
            (fluidStack) -> Common.AmmunitionFluids.contains(fluidStack.getFluid());

//    public static Stream<Fluid> getFluidsAsStream() {
//        return FLUIDS.getEntries().stream().map(RegistryObject::get);
//    }
//
//    public static Stream<FluidType> getFluidTypesAsStream() {
//        return getFluidsAsStream().map(Fluid::getFluidType);
//    }

    public static Fluid getAmmunitionFluidByName(String pName) {
        Fluid result = null;
        for(Fluid fluid : AmmunitionFluids){
            if(fluid.getFluidType().toString().equals(pName)) {
                result = fluid;
                break;
            }
        }
        return result;
    }

//    public static Optional<Fluid> getFluidTypeByName(String pName) {
//        return AmmunitionFluids.stream().filter((fluidType) -> {
//            return pName.equals(Objects.<ResourceLocation>requireNonNull(((IForgeRegistry) ForgeRegistries.FLUID_TYPES.get()).getKey(fluidType))
//                    .getPath());
//        }).findFirst();
//    }
}

