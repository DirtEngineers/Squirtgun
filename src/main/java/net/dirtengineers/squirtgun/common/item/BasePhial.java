package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
import net.minecraft.world.item.Item;

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

    public BasePhial(Properties pProperties) {
        super(pProperties);
        chemical = null;
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

    public int getShotsAvailable() {
        return shotsAvailable;
    }

    public int getFluidCapacityInMb() {
        return maxShots * SquirtSlug.shotSize;
    }

    public Chemical getChemical() {
        return chemical;
    }
}
