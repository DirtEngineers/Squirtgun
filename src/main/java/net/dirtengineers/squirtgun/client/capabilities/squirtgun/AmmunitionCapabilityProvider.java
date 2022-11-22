package net.dirtengineers.squirtgun.client.capabilities.squirtgun;

import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AmmunitionCapabilityProvider implements ICapabilityProvider {

    private ItemStack stack;
    private final LazyOptional<AmmunitionStorage> optional = LazyOptional.of(() -> new AmmunitionStorage(stack));

    public AmmunitionCapabilityProvider(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == SquirtgunCapabilities.SQUIRTGUN_AMMO ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return getCapability(cap, null);
    }
}
