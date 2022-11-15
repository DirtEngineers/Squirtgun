package net.dirtengineers.squirtgun.common.item;

public class EmptyPhialItem extends BasePhial {

    public EmptyPhialItem(Properties pProperties) {
        super(pProperties);
        capacityUpgrade = CAPACITY_UPGRADE.BASE;
        chemical = null;
        shotsAvailable = 0;
    }
}



