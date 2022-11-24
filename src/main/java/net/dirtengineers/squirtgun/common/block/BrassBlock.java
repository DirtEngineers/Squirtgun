package net.dirtengineers.squirtgun.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BrassBlock extends Block implements BlockBrassColor {

    public BrassBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(5.0F, 12.0F).sound(SoundType.METAL));
    }
}
