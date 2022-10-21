package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BaseSquirtMagazine;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider  {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Squirtgun.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.generateMagazineModel();
        ItemRegistration.MAGAZINES.keySet().forEach(this::generateMagazine);
    }

    private void generateMagazineModel(){
        this.withExistingParent("item/squirt_magazine_model", this.mcLoc("item/generated"))
                .texture("layer0", this.modLoc("item/magazine_layer_0"))
                .texture("layer1", this.modLoc("item/magazine_layer_1"));
    }

    private void generateMagazine(BaseSquirtMagazine magazine) {
        this.withExistingParent(String.format("item/%s", magazine.toString()), this.modLoc("item/squirt_magazine_model"));
    }
}
