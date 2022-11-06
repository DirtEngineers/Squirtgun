package net.dirtengineers.squirtgun.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class EmptyPhialItem extends BasePhial {

    public EmptyPhialItem(Properties pProperties) {
        super(pProperties);
        capacityUpgrade = CAPACITY_UPGRADE.BASE;
    }

    @Override
    public FluidStack loadFluid(FluidStack pFluidStack){
        return pFluidStack;
    }

    @Override
    public Optional<Fluid> getOptionalFluid(){
        return Optional.empty();
    }

    @Override
    public boolean hasAmmunition(Player pPlayer) {
        return false;
    }
}



