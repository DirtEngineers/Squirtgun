package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.util.TextUtility;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LocalizationGenerator extends LanguageProvider {
    public LocalizationGenerator(DataGenerator gen, String locale) {
        super(gen, Squirtgun.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        addManualTranslations();
        ItemRegistration.PHIALS.keySet().forEach(
                phial -> this.add(
                        Constants.phialItemNameTranslationPrefix +  phial,
                        TextUtility.capitalizeText(phial.toString().replace('_', ' '), ' ')));
    }

    private void addManualTranslations(){
        this.add(Constants.squirtgunTabItemGroup, "Squirtgun");
        this.add(String.format("item.squirtgun.%s", Constants.gunItemName), "Squirtgun");
        this.add(String.format("item.squirtgun.%s", Constants.phialItemName), "Empty Phial");
        this.add("item.squirtgun.gun_functionality", "Squirtgun reload/settings");
        this.add("item.chemlib.milk", "Milk");
        this.add("item.chemlib.lava", "Lava");
        this.add("sounds.squirtgun.squirt_slug_hit", "EWWW!");
        this.add("sounds.squirtgun.reload_screen_close", "YEEEEHAAAA!");
        this.add("sounds.squirtgun.phial_swap", "Phial Swapped");
        this.add("sounds.squirtgun.gun_use", "Ready to roll!");
        this.add("sounds.squirtgun.gun_fire", "BOOM!");
        this.add("sounds.squirtgun.phial_complete", "Ding!  Fries are done.");
        this.add("sounds.squirtgun.encapsulator_processing", "Time to make the doughnuts.");
        this.add("key.category.squirtgun", "Squirtgun");
        this.add("key.squirtgun.gun_ammo_load", "Load Ammunition");
        this.add("key.squirtgun.gun_display_ammo_status", "Toggle Ammunition Status Display");
        this.add("fluid.squirtgun.empty_fluid_name", "EMPTY");
        this.add("tooltip.squirtgun.energy_requirement", "Requires %d FE/t");
        this.add(Constants.encapsulatorMenuScreenTitle, "Dr. Clark's Fully Automatic Fluid Encapsulation Matrix");
        this.add(Constants.encapsulatorBlockNameKey, "Dr. Clark's Fully Automatic Fluid Encapsulation Matrix");
        this.add(Constants.openGunGui, "Open Squirtgun Settings");
        this.add(Constants.squirtgunItemGroup, "Squirtgun");
        this.add(Constants.reloadScreenCurrentAmmunition, "Current Ammunition: ");
        this.add(Constants.reloadScreenInventoryWarning, "No room in inventory for ");
    }
}
