package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.registry.ItemRegistration;

public class ChemicalPhial extends BasePhial {

    public ChemicalPhial(Chemical pChemical, CAPACITY_UPGRADE pCapacityUpgrade) {
        super(ItemRegistration.ITEM_PROPERTIES_WITH_TAB);
        chemical = pChemical;
        capacityUpgrade = pCapacityUpgrade;
        applyUpgrades();
        shotsAvailable = maxShots;
    }
}
