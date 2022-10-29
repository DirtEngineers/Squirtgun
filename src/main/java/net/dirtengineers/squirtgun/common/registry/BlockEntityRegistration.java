package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.block.fluid_encapsulator.FluidEncapsulatorBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistration {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
    public static final RegistryObject<BlockEntityType<FluidEncapsulatorBlockEntity>> FLUID_ENCAPSULATOR_BLOCK_ENTITY;

    public BlockEntityRegistration() {}

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    static{
        BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Squirtgun.MOD_ID);
        FLUID_ENCAPSULATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
                "fluid_encapsulator_block_entity",
                () -> BlockEntityType.Builder.of(
                        FluidEncapsulatorBlockEntity::new,
                        new Block[]{BlockRegistration.FLUID_ENCAPSULATOR.get()}).build(null));
    }
}