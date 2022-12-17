package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.utility.TextUtility;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class LocalizationGenerator extends LanguageProvider {
    public LocalizationGenerator(DataGenerator gen, String locale) {
        super(gen, Squirtgun.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        addManualTranslations();
        ItemRegistration.CHEMICAL_PHIALS.forEach(
                phial -> this.add(
                        Constants.phialItemNameTranslationPrefix + phial,
                        TextUtility.capitalizeText(phial.toString().replace('_', ' '), ' ')));

        ItemRegistration.POTION_PHIALS.forEach(
                phial ->
                    this.add(
                            Constants.phialItemNameTranslationPrefix + phial,
                            TextUtility.capitalizeText(phial.toString().replace('_', ' '), ' ')));

        for(Map.Entry<ResourceKey<Potion>, Potion> potion : ForgeRegistries.POTIONS.getEntries().stream().toList()) {
            if (potion.getValue().getEffects().size() > 0) {
                this.add(
                        String.format("%s:%s", potion.getKey().location().getNamespace(), potion.getKey().location().getPath()),
                        String.format("%s%s", TextUtility.capitalizeText(potion.getKey().location().getPath().replace('_', ' '), ' '), " Potion"));
            }
        }
    }

    private void addManualTranslations(){
        //ITEMS
        this.add(String.format("item.squirtgun.%s", Constants.gunItemName), "Squirtgun");
        this.add(String.format("item.squirtgun.%s", Constants.phialItemName), "Empty Phial");
        this.add(String.format("item.squirtgun.%s", Constants.brassBlendItemName), "Brass Blend");
        this.add(String.format("item.squirtgun.%s", Constants.brassNuggetItemName), "Brass Nugget");
        this.add(String.format("item.squirtgun.%s", Constants.brassIngotItemName), "Brass Ingot");
        this.add(String.format("item.squirtgun.%s", Constants.brassBlockItemName), "Block of Brass");
        this.add(String.format("item.squirtgun.%s", Constants.phialCapItemName), "Phial Cap");
        this.add(String.format("item.squirtgun.%s", Constants.quartzShardItemName), "Fuzed Quartz Shard");
        this.add(String.format("block.squirtgun.%s", Constants.brassBlockName), "Block of Brass");
        this.add(String.format("item.squirtgun.%s", Constants.actuatorItemName), "Actuator");
        this.add(String.format("block.squirtgun.%s", Constants.encapsulatorBlockName), "Dr. Clark's Fluid Encapsulation Matrix");
        //Sounds
        this.add("sounds.squirtgun.squirt_slug_hit", "EWWW!");
        this.add("sounds.squirtgun.reload_screen_close", "YEEEEHAAAA!");
        this.add("sounds.squirtgun.phial_swap", "Phial Swapped");
        this.add("sounds.squirtgun.gun_use", "Ready to roll!");
        this.add("sounds.squirtgun.gun_dry_fire", "Oops.  No ammo.");
        this.add("sounds.squirtgun.gun_fire", "BOOM!");
        this.add("sounds.squirtgun.phial_complete", "Ding!  Fries are done.");
        this.add("sounds.squirtgun.encapsulator_processing", "Time to make the doughnuts.");
        //Keybinds
        this.add("key.category.squirtgun", "Squirtgun");
        this.add("key.squirtgun.gun_ammo_load", "Load Ammunition");
        this.add("key.squirtgun.gun_display_ammo_status", "Toggle Ammunition Status Display");

        this.add("item.squirtgun.gun_functionality", "Squirtgun reload/settings");
        this.add("fluid.squirtgun.empty_fluid_name", "EMPTY");
        this.add("tooltip.squirtgun.energy_requirement", "Requires %d FE/t");
        this.add(Constants.encapsulatorMenuScreenTitle, "Dr. Clark's Fluid Encapsulation Matrix");
        this.add(Constants.openGunGui, "Open Squirtgun Settings");
        this.add(Constants.reloadScreenCurrentAmmunition, "Current Ammunition: ");
        this.add(Constants.reloadScreenInventoryWarning, "No room in inventory for ");
        this.add(Constants.gunGuiPhialIsLoaded, "Loaded");
        this.add(Constants.gunGuiPhialLoaded, "Currently Loaded");
        this.add(Constants.gunGuiPhialAmmoLossWarning, "Warning: Currrently loaded ammo will be lost upon change.");
        this.add(Constants.gunGuiCancelButtonMessage, "Cancel Loading");
        this.add(Constants.gunGuiAgreeButtonMessage, "Load it!");
        this.add(Constants.recipeRequiredInput, "Required input item:");
        this.add(Constants.currentSelectedRecipe, "Current recipe:");
        this.add(Constants.guiSearch, "Search...");
        this.add(Constants.guiSelectRecipe, "Select recipe:");
    }
}
