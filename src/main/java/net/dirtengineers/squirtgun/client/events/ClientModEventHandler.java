package net.dirtengineers.squirtgun.client.events;

import com.smashingmods.chemlib.common.items.CompoundItem;
import com.smashingmods.chemlib.common.items.ElementItem;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.keybinds.GunAmmoDisplayKeybind;
import net.dirtengineers.squirtgun.client.keybinds.GunAmmoLoadKeybind;
import net.dirtengineers.squirtgun.client.overlay.AmmunitionHudOverlay;
import net.dirtengineers.squirtgun.client.render.SquirtSlugRenderer;
import net.dirtengineers.squirtgun.common.registry.EntityRegistration;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Squirtgun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event){
        EntityRenderers.register(EntityRegistration.SQUIRT_SLUG.get(), SquirtSlugRenderer::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("ammunition_readout", AmmunitionHudOverlay.HUD_AMMUNITION);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(GunAmmoLoadKeybind.GUN_LOAD_AMMO_KEY);
        event.register(GunAmmoDisplayKeybind.GUN_AMMO_STATUS_DISPLAY_KEY);
    }

    @SubscribeEvent
    public static void onItemColorHandlerEvent(RegisterColorHandlersEvent.Item event) {

        ItemRegistration.MAGAZINES.forEach((magazine, chemical) -> {
            Objects.requireNonNull(magazine);
            Objects.requireNonNull(chemical);

            if(chemical instanceof ElementItem)
                event.register(((ElementItem)chemical)::getColor, magazine);
            else
                event.register(((CompoundItem)chemical)::getColor, magazine);
                });
    }
}
