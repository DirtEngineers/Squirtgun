package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.registry.BlockRegistration;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class BlockTagGenerator extends ForgeRegistryTagsProvider<Block> {

    public BlockTagGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, ForgeRegistries.BLOCKS, Squirtgun.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (RegistryObject<Block> blockRegistryObject : BlockRegistration.BLOCKS.getEntries()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockRegistryObject.get());
            this.tag(BlockTags.NEEDS_STONE_TOOL).add(blockRegistryObject.get());
        }
    }

    @Override
    public String getName() {
        return "";
    }
}