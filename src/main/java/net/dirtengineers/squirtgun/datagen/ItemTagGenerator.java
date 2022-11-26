package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemTagGenerator  extends ForgeRegistryTagsProvider<Item> {
    public ItemTagGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, ForgeRegistries.ITEMS, Squirtgun.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagKey<Item> key = Objects.requireNonNull(ForgeRegistries.ITEMS.tags())
                .createTagKey(new ResourceLocation("forge", String.format("%ss/%s", "dust", ItemRegistration.BRASS_BLEND.get())));
        this.tag(key).add(ItemRegistration.BRASS_BLEND.get());

        key = Objects.requireNonNull(ForgeRegistries.ITEMS.tags())
                .createTagKey(new ResourceLocation("forge", String.format("%ss/%s", "ingot", ItemRegistration.BRASS_INGOT.get())));
        this.tag(key).add(ItemRegistration.BRASS_INGOT.get());

        key = Objects.requireNonNull(ForgeRegistries.ITEMS.tags())
                .createTagKey(new ResourceLocation("forge", String.format("%ss/%s", "nugget", ItemRegistration.BRASS_NUGGET.get())));
        this.tag(key).add(ItemRegistration.BRASS_NUGGET.get());

        key = Objects.requireNonNull(ForgeRegistries.ITEMS.tags())
                .createTagKey(new ResourceLocation("forge", String.format("%s/%s", "glass/silica", ItemRegistration.QUARTZ_SHARD.get())));
        this.tag(key).add(ItemRegistration.QUARTZ_SHARD.get());
    }
}
