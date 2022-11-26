package net.dirtengineers.squirtgun.common.registry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.network.SquirtgunPacketHandler;
import net.minecraftforge.eventbus.api.IEventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

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

    public static JsonObject getStreamAsJsonObject(String pPath) {
        return JsonParser.parseReader(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Squirtgun.class.getResourceAsStream(pPath))))).getAsJsonObject();
    }
}
