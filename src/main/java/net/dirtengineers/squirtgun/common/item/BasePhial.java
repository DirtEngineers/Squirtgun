package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public abstract class BasePhial extends Item {

    public enum CAPACITY_UPGRADE {
        BASE,
        DOUBLESHOTS,
        TRIPLESHOTS
    }

    Chemical chemical;
    CAPACITY_UPGRADE capacityUpgrade;
    int shotsAvailable;
    int maxShots;
    int baseMaxShots = 10;
    Optional<Fluid> optionalFluid;

    public BasePhial(Properties pProperties) {
        super(pProperties);
        chemical = null;
        shotsAvailable = baseMaxShots;
        maxShots = 0;
        optionalFluid = Optional.empty();
    }

    protected void applyUpgrades() {
        switch (capacityUpgrade) {
            case BASE -> maxShots = baseMaxShots;
            case DOUBLESHOTS -> maxShots = baseMaxShots * 2;
            case TRIPLESHOTS -> maxShots = baseMaxShots * 3;
        }
    }

    public FluidStack loadFluid(FluidStack pFluidStack) {
        initializeFluid();
        if (isFluidValid(pFluidStack)) {
            if (pFluidStack.getAmount() >= maxShots * SquirtSlug.shotSize) {
                shotsAvailable = maxShots;
                pFluidStack.shrink(maxShots * SquirtSlug.shotSize);
            } else {
                int stackShots = (int) Math.floor((double) pFluidStack.getAmount() / SquirtSlug.shotSize);
                shotsAvailable += stackShots;
                pFluidStack.shrink(stackShots * SquirtSlug.shotSize);
            }
        }
        return pFluidStack;
    }

    private void initializeFluid() {
        if (chemical != null) {
            chemical.getFluidTypeReference()
                    .flatMap(fluidType -> FluidRegistry.getFluidsAsStream()
                            .filter(fluid -> fluid.getFluidType().equals(chemical.getFluidTypeReference().get()))
                            .findFirst())
                    .ifPresent(fluid -> optionalFluid = Optional.of(fluid));
        }
    }

    public Optional<Fluid> getOptionalFluid() {
        if(optionalFluid.isEmpty()){
            initializeFluid();
        }
        return optionalFluid;
    }

    protected boolean isFluidValid(FluidStack pFluidStack) {
        if (chemical != null && chemical.getFluidTypeReference().isPresent() && pFluidStack != FluidStack.EMPTY) {
            return pFluidStack.getFluid().getFluidType() == chemical.getFluidTypeReference().get();
        }
        return false;
    }

    protected void consumeAmmunition(Player pPlayer) {
        if (!pPlayer.getAbilities().instabuild) {
            shotsAvailable--;
        }
    }

    public boolean hasAmmunition(Player pPlayer) {
        return false;
    }

    public String getAmmoStatus() {
        return shotsAvailable + "/" + maxShots;
    }

    public int getShotsAvailable(){
        return shotsAvailable;
    }

    public int getCapacityInMb() { return this.maxShots * SquirtSlug.shotSize; }
}
