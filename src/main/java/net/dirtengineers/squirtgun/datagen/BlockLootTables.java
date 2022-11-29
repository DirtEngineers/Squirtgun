package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.registry.BlockRegistration;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

public class BlockLootTables extends BlockLoot {
    BlockLootTables(){}

    protected void addTables() {
        BlockRegistration.BLOCKS.getEntries().forEach((block) -> this.dropSelf(block.get()));
    }

    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        Stream<Block> blockStream = BlockRegistration.BLOCKS.getEntries().stream().map(RegistryObject::get);
        Objects.requireNonNull(blockStream);
        return blockStream::iterator;
    }
}
