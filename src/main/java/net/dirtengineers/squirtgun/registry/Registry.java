package net.dirtengineers.squirtgun.registry;

import net.dirtengineers.squirtgun.common.network.SquirtgunPacketHandler;
import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {

    public static void register(IEventBus modEventBus) {
//        CompoundRegistration.register();
        BlockRegistration.register(modEventBus);
        ItemRegistration.register(modEventBus);
        BlockEntityRegistration.register(modEventBus);
        EntityRegistration.register(modEventBus);
        SoundEventRegistration.register(modEventBus);
        MenuRegistration.register(modEventBus);
        RecipeRegistration.register(modEventBus);
        SquirtgunPacketHandler.register();
    }
}