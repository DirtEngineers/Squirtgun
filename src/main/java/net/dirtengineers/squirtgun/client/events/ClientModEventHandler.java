package net.dirtengineers.squirtgun.client.events;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.keybinds.GunAmmoLoadKeybind;
import net.dirtengineers.squirtgun.client.overlay.AmmunitionHudOverlay;
import net.dirtengineers.squirtgun.client.render.SquirtSlugRenderer;
import net.dirtengineers.squirtgun.common.registry.EntityRegistration;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
    }
}
