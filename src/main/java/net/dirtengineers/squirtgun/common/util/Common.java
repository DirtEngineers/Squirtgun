package net.dirtengineers.squirtgun.common.util;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.Compound;
import com.smashingmods.chemlib.common.items.CompoundItem;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Common {
    public static Map<Fluid, Chemical> Ammunition = new HashMap<>();

    public static final Predicate<FluidStack> SQUIRT_AMMUNITION_ONLY =
            (fluidStack) -> Common.Ammunition.containsKey(fluidStack.getFluid());

    public static String getFriendlyFluidName(Fluid pFluid){
        Chemical chemical = Common.Ammunition.get(pFluid);
        return chemical instanceof Compound ? I18n.get(((CompoundItem) chemical).getDescriptionId()) : chemical.getChemicalName();
    }

    public static int getFluidColor(Fluid pFluid){
        return IClientFluidTypeExtensions.of(pFluid.getFluidType()).getTintColor();
    }
}

