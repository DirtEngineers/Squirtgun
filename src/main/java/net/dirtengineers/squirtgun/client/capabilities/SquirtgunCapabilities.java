package net.dirtengineers.squirtgun.client.capabilities;

import net.dirtengineers.squirtgun.client.capabilities.squirtgun.AmmunitionStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SquirtgunCapabilities {
    public static Capability<AmmunitionStorage> SQUIRTGUN_AMMO = CapabilityManager.get(new CapabilityToken<>() {});

    public void register(RegisterCapabilitiesEvent pEvent) {
        pEvent.register(AmmunitionStorage.class);
    }
}
