package net.dirtengineers.squirtgun.client.capabilities.squirtgun;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.ammunitionChemicals;

public class AmmunitionStorage implements IAmmunitionCapability, INBTSerializable<CompoundTag> {

    private final ItemStack stack;
    protected Chemical chemical;
    protected int shotsAvailable;
    protected final int MAX_SHOTS = 10;
    protected final int MIN_SHOTS = 0;

    public AmmunitionStorage(ItemStack stack) {
        this.stack = stack;
        this.deserializeNBT(stack.getOrCreateTag());
    }

    @Override
    public void setShotsAvailable(int pShots) {
        shotsAvailable = Math.min(pShots, MAX_SHOTS);
        setShotsTag();
    }

    @Override
    public int getMaxShots() {
        return MAX_SHOTS;
    }

    @Override
    public int getShotsAvailable() {
        return shotsAvailable;
    }

    @Override
    public boolean hasAmmunition() {
        return shotsAvailable > MIN_SHOTS;
    }

    @Override
    public void decrementShots() {
        this.setShotsAvailable(Math.max(shotsAvailable - 1, MIN_SHOTS));
    }

    @Override
    public Chemical getChemical() {
        return chemical;
    }

    @Override
    public void setChemical(Chemical pChemical) {
        if (isChemicalValid(pChemical)) {
            chemical = pChemical;
            setchemicalTag();
        }
    }

    @Override
    public boolean isChemicalValid(Chemical pChemical) {
        return ammunitionChemicals.contains(pChemical) || pChemical == null;
    }

    public String getAmmoStatus() {
        return (chemical== null) ? "---" : String.format("%s/%s", shotsAvailable, MAX_SHOTS);
    }

    private void setchemicalTag() {
        CompoundTag tag = stack.getOrCreateTag();
        stack.getOrCreateTag().putString(Constants.CHEMICAL_TAG, chemical != null
                ? String.format("%s:%s", chemical.getClass().getModule().getName(), chemical.asItem())
                : "");
        stack.save(tag);
    }

    private void setShotsTag() {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(Constants.SHOTS_AVAILABLE_TAG, shotsAvailable);
        stack.save(tag);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag pTag = new CompoundTag();
        pTag.putString(Constants.CHEMICAL_TAG, chemical != null
                ? String.format("%s:%s", chemical.getClass().getModule().getName(), chemical.asItem())
                : "");
        pTag.putInt(Constants.SHOTS_AVAILABLE_TAG, shotsAvailable);
        return pTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.chemical = (nbt.contains(Constants.CHEMICAL_TAG)
                && !Objects.requireNonNull(nbt.get(Constants.CHEMICAL_TAG)).getAsString().equals(""))
                ? (Chemical) ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString(Constants.CHEMICAL_TAG)))
                : null;
        this.shotsAvailable = (nbt.contains(Constants.SHOTS_AVAILABLE_TAG)
                && !Objects.requireNonNull(nbt.get(Constants.SHOTS_AVAILABLE_TAG)).getAsString().equals(""))
                ? nbt.getInt(Constants.SHOTS_AVAILABLE_TAG)
                : 0;
    }
}
