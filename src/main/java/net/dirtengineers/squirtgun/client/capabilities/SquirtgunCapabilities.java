package net.dirtengineers.squirtgun.client.capabilities;

import net.dirtengineers.squirtgun.client.capabilities.squirtgun.AmmunitionStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class SquirtgunCapabilities {
    public static Capability<AmmunitionStorage> SQUIRTGUN_AMMO = CapabilityManager.get(new CapabilityToken<>() {});
}
