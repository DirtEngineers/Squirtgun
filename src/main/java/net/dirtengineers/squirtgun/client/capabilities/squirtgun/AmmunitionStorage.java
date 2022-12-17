package net.dirtengineers.squirtgun.client.capabilities.squirtgun;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

import static net.dirtengineers.squirtgun.registry.ItemRegistration.ammunitionChemicals;

public class AmmunitionStorage implements IAmmunitionCapability, INBTSerializable<CompoundTag> {

    private final ItemStack stack;
    protected Chemical chemical;
    protected String potionKey;
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
            potionKey = "";
            setChemicalTag();
        }
    }

    @Override
    public String getPotionKey() {
        return potionKey;
    }

    @Override
    public void setPotionKey(String pKey) {
        potionKey = pKey;
        chemical = null;
        setPotionKeyTag();
    }

    @Override
    public boolean isChemicalValid(Chemical pChemical) {
        return ammunitionChemicals.contains(pChemical) || pChemical == null;
    }

    public String getAmmoStatus() {
        return (chemical== null && Objects.equals(potionKey, "")) ? "---" : String.format("%s/%s", shotsAvailable, MAX_SHOTS);
    }

    private void setChemicalTag() {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(Constants.CHEMICAL_TAG);
        tag.putString(Constants.CHEMICAL_TAG, chemical != null
                ? String.format("%s:%s", chemical.getClass().getModule().getName(), chemical.asItem())
                : "");
        tag.remove(Constants.POTION_TAG);
        tag.putString(Constants.POTION_TAG, potionKey);
        stack.save(tag);
    }

    private void setPotionKeyTag() {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(Constants.POTION_TAG);
        tag.putString(Constants.POTION_TAG, potionKey);
        tag.remove(Constants.CHEMICAL_TAG);
        tag.putString(Constants.CHEMICAL_TAG, chemical != null
                ? String.format("%s:%s", chemical.getClass().getModule().getName(), chemical.asItem())
                : "");
        stack.save(tag);
    }

    private void setShotsTag() {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(Constants.SHOTS_AVAILABLE_TAG);
        tag.putInt(Constants.SHOTS_AVAILABLE_TAG, shotsAvailable);
        stack.save(tag);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag pTag = new CompoundTag();
        pTag.putString(Constants.CHEMICAL_TAG, chemical != null
                ? String.format("%s:%s", chemical.getClass().getModule().getName(), chemical.asItem())
                : "");
        pTag.putString(Constants.POTION_TAG, potionKey);
        pTag.putInt(Constants.SHOTS_AVAILABLE_TAG, shotsAvailable);
        return pTag;
    }

    @Override
    public void deserializeNBT(CompoundTag pTag) {
        this.chemical = (pTag.contains(Constants.CHEMICAL_TAG)
                && !Objects.requireNonNull(pTag.get(Constants.CHEMICAL_TAG)).getAsString().equals(""))
                ? (Chemical) ForgeRegistries.ITEMS.getValue(new ResourceLocation(pTag.getString(Constants.CHEMICAL_TAG)))
                : null;
        this.potionKey = (pTag.contains(Constants.POTION_TAG))
                ? pTag.getString(Constants.POTION_TAG)
                : "";
        this.shotsAvailable = (pTag.contains(Constants.SHOTS_AVAILABLE_TAG)
                && !Objects.requireNonNull(pTag.get(Constants.SHOTS_AVAILABLE_TAG)).getAsString().equals(""))
                ? pTag.getInt(Constants.SHOTS_AVAILABLE_TAG)
                : 0;
    }
}
