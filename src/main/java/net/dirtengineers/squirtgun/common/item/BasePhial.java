package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
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
        shotsAvailable = 0;
        maxShots = baseMaxShots;
        optionalFluid = Optional.empty();
        capacityUpgrade = CAPACITY_UPGRADE.BASE;
    }

    protected void applyUpgrades() {
        switch (capacityUpgrade) {
            case BASE -> maxShots = baseMaxShots;
            case DOUBLESHOTS -> maxShots = baseMaxShots * 2;
            case TRIPLESHOTS -> maxShots = baseMaxShots * 3;
        }
    }

    public void setCapacityUpgrade(CAPACITY_UPGRADE pUpgrade) {
        capacityUpgrade = pUpgrade;
        applyUpgrades();
    }

    public CAPACITY_UPGRADE getCapacityUpgrade() {
        return capacityUpgrade;
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
        return shotsAvailable > 0 || pPlayer.getAbilities().instabuild;
    }

    public String getAmmoStatus() {
        return shotsAvailable + "/" + maxShots;
    }

    public int getShotsAvailable(){
        return shotsAvailable;
    }

    public int getFluidCapacityInMb() { return maxShots * SquirtSlug.shotSize; }

    public int getFluidUsed() { return (maxShots - shotsAvailable)  * SquirtSlug.shotSize; }
}
