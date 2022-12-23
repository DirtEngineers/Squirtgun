package net.dirtengineers.squirtgun.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber( bus = EventBusSubscriber.Bus.MOD )
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent pEvent) {
        DataGenerator generator = pEvent.getGenerator();
        generator.addProvider(new RecipeGenerator(generator));
        generator.addProvider(new ItemModelGenerator(generator, pEvent.getExistingFileHelper()));
        generator.addProvider(new LocalizationGenerator(generator, "en_us"));
        generator.addProvider(new BlockTagGenerator(generator, pEvent.getExistingFileHelper()));
        generator.addProvider(new ItemTagGenerator(generator, pEvent.getExistingFileHelper()));
        generator.addProvider(new LootTableGenerator(generator));
    }
}
