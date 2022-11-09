package net.dirtengineers.squirtgun.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class EmptyPhialItem extends BasePhial {

    public EmptyPhialItem(Properties pProperties) {
        super(pProperties);
        capacityUpgrade = CAPACITY_UPGRADE.BASE;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected) {}

    @Override
    public void setStatusChanged(){}

    @Override
    public FluidStack loadFluid(FluidStack pFluidStack){
        return pFluidStack;
    }

    @Override
    public boolean isFluidValid(FluidStack pFluidStack) {
        return false;
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



