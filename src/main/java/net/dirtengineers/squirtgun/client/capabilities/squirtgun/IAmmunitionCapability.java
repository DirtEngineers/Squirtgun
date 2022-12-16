package net.dirtengineers.squirtgun.client.capabilities.squirtgun;

import com.smashingmods.chemlib.api.Chemical;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IAmmunitionCapability {

    void setShotsAvailable(int pShots);

    boolean hasAmmunition();

    int getMaxShots();

    int getShotsAvailable();

    void decrementShots();

    Chemical getChemical();

    void setChemical(Chemical pChemical);

    boolean isChemicalValid(Chemical pChemical);

    String getAmmoStatus();

}
