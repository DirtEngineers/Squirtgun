package net.dirtengineers.squirtgun.common.util;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class AmmunitionContainer_OLD extends FluidTank {

    public static int BASE_CAPACITY = 1000;
    private final Item parent;

    public AmmunitionContainer_OLD(Predicate<FluidStack> pValidator, Item pParent) {
        super(BASE_CAPACITY, pValidator);
        parent = pParent;
    }

//    public boolean hasAmmunition(Player pPlayer){
//        return this.getFluidAmount() >= SquirtSlug.shotSize || pPlayer.getAbilities().instabuild;
//    }

    public int getMaxShots() {
        return (int) Math.floor((float) this.getCapacity() / SquirtSlug.shotSize);
    }

    public int getShotsAvailable() { return (int) Math.floor((float) this.getFluidAmount() / SquirtSlug.shotSize); }

    public String getAmmoStatus(){ return this.getShotsAvailable() + "/" +  this.getMaxShots(); }
}
