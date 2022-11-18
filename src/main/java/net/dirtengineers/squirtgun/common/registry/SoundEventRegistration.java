package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundEventRegistration {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS;
    public static RegistryObject<SoundEvent> SQUIRT_SLUG_HIT;
    public static RegistryObject<SoundEvent> RELOAD_SCREEN_CLOSE;
    public static RegistryObject<SoundEvent> PHIAL_SWAP;
    public static RegistryObject<SoundEvent> GUN_USE;
    public static RegistryObject<SoundEvent> GUN_FIRE;
    public static RegistryObject<SoundEvent> PHIAL_COMPLETE;
    public static RegistryObject<SoundEvent> ENCAPSULATOR_PROCESSING;

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Squirtgun.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    static {
        SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Squirtgun.MOD_ID);
        SQUIRT_SLUG_HIT = registerSoundEvent(Constants.SlugHitSoundName);
        RELOAD_SCREEN_CLOSE = registerSoundEvent(Constants.ReloadScreenCloseSoundName);
        PHIAL_SWAP = registerSoundEvent(Constants.PhialSwapSoundName);
        GUN_USE = registerSoundEvent(Constants.GunUseSoundName);
        GUN_FIRE = registerSoundEvent(Constants.GunFireSoundName);
        PHIAL_COMPLETE = registerSoundEvent(Constants.PhialCompleteSoundName);
        ENCAPSULATOR_PROCESSING = registerSoundEvent(Constants.EncapsulatorProcessingSoundName);
    }
}
