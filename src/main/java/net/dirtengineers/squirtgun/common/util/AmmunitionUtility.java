package net.dirtengineers.squirtgun.common.util;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.Compound;
import com.smashingmods.chemlib.common.items.CompoundItem;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.Objects;

public class AmmunitionUtility {

    public static final int EMPTY_FLUID_COLOR = 0XFFFFFFFF;
    public static String EMPTY_FLUID_NAME = "minecraft:empty";

    public static String getFriendlyFluidName(Fluid pFluid){
        if(AmmunitionUtility.isEmptyFluid(pFluid)) return "empty";
        else {
            Chemical chemical = Common.Ammunition.get(pFluid);
            return chemical instanceof Compound ?
                    I18n.get(((CompoundItem) chemical).getDescriptionId()) :
                    chemical.getChemicalName();
        }
    }

    public static boolean isEmptyFluid(Fluid pFluid) {
        if (pFluid == null) return true;
        return Objects.equals(pFluid.getFluidType().toString(), EMPTY_FLUID_NAME);
    }

    public static int getFluidColor(Fluid pFluid) {
        if (AmmunitionUtility.isEmptyFluid(pFluid)) return EMPTY_FLUID_COLOR;
        return IClientFluidTypeExtensions.of(pFluid.getFluidType()).getTintColor();
    }
}
