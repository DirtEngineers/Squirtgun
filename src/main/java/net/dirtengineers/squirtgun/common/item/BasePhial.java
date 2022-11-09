package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    private boolean statusChanged;

    public BasePhial(Properties pProperties) {
        super(pProperties);
        chemical = null;
        shotsAvailable = 0;
        maxShots = baseMaxShots;
        optionalFluid = Optional.empty();
        capacityUpgrade = CAPACITY_UPGRADE.BASE;
        statusChanged = true;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected) {
        if (!pLevel.isClientSide) {
            if(optionalFluid.isEmpty()) {
                loadFromNBT(pStack);
            }
            if(statusChanged)
                setTag(pStack);
        }
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
            statusChanged = true;
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
            statusChanged = true;
        }
    }

    public Optional<Fluid> getOptionalFluid() {
        if(optionalFluid.isEmpty()){
            initializeFluid();
        }
        return optionalFluid;
    }

    public boolean isFluidValid(FluidStack pFluidStack) {
        if (chemical != null && chemical.getFluidTypeReference().isPresent() && pFluidStack != FluidStack.EMPTY) {
            return pFluidStack.getFluid().getFluidType() == chemical.getFluidTypeReference().get();
        }
        return false;
    }

    protected void consumeAmmunition(Player pPlayer) {
        if (!pPlayer.getAbilities().instabuild) {
            shotsAvailable--;
            statusChanged = true;
        }
    }

    public void setStatusChanged(){
        statusChanged = true;
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

    private void loadFromNBT(ItemStack pStack) {
        CompoundTag stackTag = pStack.getOrCreateTag();
        if (stackTag.contains(Constants.PHIAL_SHOTS_AVAILABLE_TAG) && stackTag.contains(Constants.PHIAL_UPGRADE_TAG)) {
            this.shotsAvailable = stackTag.getInt(Constants.PHIAL_SHOTS_AVAILABLE_TAG);
            this.capacityUpgrade = CAPACITY_UPGRADE.valueOf(stackTag.getString(Constants.PHIAL_UPGRADE_TAG));
            initializeFluid();
            applyUpgrades();
        }
        setTag(pStack);
        statusChanged = false;
//        pStack.setTag(new CompoundTag());
    }

    private void setTag(ItemStack pStack) {
        CompoundTag tag = pStack.getOrCreateTag();
            tag.putInt(Constants.PHIAL_SHOTS_AVAILABLE_TAG, this.shotsAvailable);
            tag.putString(Constants.PHIAL_UPGRADE_TAG, String.valueOf(this.capacityUpgrade));
        statusChanged = false;
    }
}
