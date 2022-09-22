package net.dirtengineers.squirtgun.common.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {

    public static void register(IEventBus modEventBus) {
        ItemRegistration.register(modEventBus);
        EntityRegistration.register(modEventBus);
        SoundEventRegistration.register(modEventBus);
//        BlockRegistry.register(modEventBus);
//        FluidRegistry.register(modEventBus);

    }
}
