package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.block.fluid_encapsulator.FluidEncapsulatorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistration {
    public static final DeferredRegister<Block> BLOCKS;
    public static final RegistryObject<Block> FLUID_ENCAPSULATOR;

    public BlockRegistration() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    static{
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Squirtgun.MOD_ID);
        FLUID_ENCAPSULATOR = BLOCKS.register("fluid_encapsulator", FluidEncapsulatorBlock::new);
    }
}
