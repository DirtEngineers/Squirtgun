package net.dirtengineers.squirtgun.client.capabilities.squirtgun;

import com.smashingmods.chemlib.api.Chemical;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IAmmunitionCapability {

    void setShotsAvailable(int pShots);

    boolean hasAmmunition();

    void decrementShots();

    Chemical getChemical();

    void setChemical(Chemical pChemical);

    String getPotionKey();

    void setPotionKey(String pKey);

    boolean isChemicalValid(Chemical pChemical);

    String getAmmoStatus();
}
