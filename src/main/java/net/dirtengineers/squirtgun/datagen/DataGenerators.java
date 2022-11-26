package net.dirtengineers.squirtgun.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber( bus = EventBusSubscriber.Bus.MOD )
public class DataGenerators {

    public DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent pEvent) {
        DataGenerator generator = pEvent.getGenerator();
        generator.addProvider(pEvent.includeServer(), new RecipeGenerator(generator));
        generator.addProvider(pEvent.includeClient(), new ItemModelGenerator(generator, pEvent.getExistingFileHelper()));
        generator.addProvider(pEvent.includeClient(), new LocalizationGenerator(generator, "en_us"));
        generator.addProvider(pEvent.includeServer(), new BlockTagGenerator(generator, pEvent.getExistingFileHelper()));
        generator.addProvider(pEvent.includeServer(), new ItemTagGenerator(generator, pEvent.getExistingFileHelper()));
        generator.addProvider(pEvent.includeServer(), new LootTableGenerator(generator));
//        generator.addProvider(pEvent.includeClient(), new BlockStateGenerator(generator, pEvent.getExistingFileHelper()));
    }
}
