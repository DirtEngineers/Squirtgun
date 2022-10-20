package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class BaseSquirtMagazine extends Item {
    private Predicate<FluidStack> validator;
    private Chemical chemicalReference;
    private int maxShots;
    private int shotsAvailable;

    public BaseSquirtMagazine(Properties pProperties) {
        super(pProperties);
    }

    public BaseSquirtMagazine(Chemical pChemical, Properties pProperties) {
        this(pProperties);
        this.chemicalReference = pChemical;
    }

    public boolean isFluidValid(FluidStack pFluidStack){
        // get a fluid reference to the chemical and match for this magazine type
        return true;
    }
}
