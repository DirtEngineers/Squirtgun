package net.dirtengineers.squirtgun.common.util;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class AmmunitionContainer extends CustomFluidStorage{

    public static int BASE_CAPACITY = 1000;

    public AmmunitionContainer(int pCapacity, Predicate<FluidStack> pValidator) {
        super(pCapacity, pValidator);
    }

    public AmmunitionContainer(Predicate<FluidStack> pValidator) {
        super(BASE_CAPACITY, pValidator);
    }

    public boolean hasAmmunition(Player pPlayer){
        return this.getFluidAmount() >= SquirtSlug.shotSize || pPlayer.getAbilities().instabuild;
    }

    public int getMaxShots() {
        return (int) Math.floor((float) this.getCapacity() / SquirtSlug.shotSize);
    }

    public int getShotsAvailable() { return (int) Math.floor((float) this.getFluidAmount() / SquirtSlug.shotSize); }

    public String getAmmoStatus(){ return this.getShotsAvailable() + "/" +  this.getMaxShots(); }
}
