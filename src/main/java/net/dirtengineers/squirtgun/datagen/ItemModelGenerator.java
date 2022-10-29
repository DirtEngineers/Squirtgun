package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModelGenerator extends ItemModelProvider  {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Squirtgun.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.generatePhialModel();
        ItemRegistration.PHIALS.keySet().forEach(this::generatePhial);
        String emptyPhial = ForgeRegistries.ITEMS.getValue(new ResourceLocation("squirtgun:squirtphialitem")).asItem().toString();
        this.withExistingParent(String.format("item/%s", emptyPhial), this.modLoc("item/phial_model"));
    }

    private void generatePhialModel(){
        this.withExistingParent("item/phial_model", this.mcLoc("item/generated"))
                .texture("layer0", this.modLoc("item/phial_layer_0"))
                .texture("layer1", this.modLoc("item/phial_layer_1"));
    }

    private void generatePhial(BasePhial phial) {
        this.withExistingParent(String.format("item/%s", phial), this.modLoc("item/phial_model"));
    }
}
