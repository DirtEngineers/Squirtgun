package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
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
                magazine -> this.add(
                        String.format("item.squirtgun.%s", magazine),
                        setText(magazine.toString().replace('_', ' '), ' ')));
    }

    private void addManualTranslations(){
        this.add("itemGroup.squirtguntab", "Squirtgun");
        this.add("item.squirtgun.squirtgunitem", "Squirtgun");
        this.add("item.squirtgun.squirtphialitem", "Empty Phial");
        this.add("item.squirtgun.gun_functionality", "Squirtgun reload/settings");
        this.add("sounds.squirtgun.squirt_slug_hit", "EWWW!");
        this.add("key.category.squirtgun", "Squirtgun");
        this.add("key.squirtgun.gun_ammo_load", "Load Ammunition");
        this.add("key.squirtgun.no_phial", "No Phial");
        this.add("key.squirtgun.gun_display_ammo_status", "Toggle Ammunition Status Display");
        this.add("fluid.squirtgun.empty_fluid_name", "EMPTY");
    }

    private String setText(String text, char delimiter){
        final char[] buffer = text.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (ch == delimiter) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }
}
