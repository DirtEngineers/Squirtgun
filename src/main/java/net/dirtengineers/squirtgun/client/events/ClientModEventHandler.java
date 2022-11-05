package net.dirtengineers.squirtgun.client.events;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.common.items.CompoundItem;
import com.smashingmods.chemlib.common.items.ElementItem;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.overlay.AmmunitionHudOverlay;
import net.dirtengineers.squirtgun.client.render.SquirtSlugRenderer;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
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

import java.util.Map;
import java.util.Objects;

import static net.dirtengineers.squirtgun.client.Keybinds.GUN_AMMO_STATUS_DISPLAY_KEY;
import static net.dirtengineers.squirtgun.client.Keybinds.GUN_LOAD_AMMO_KEY;

@Mod.EventBusSubscriber(modid = Squirtgun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityRegistration.SQUIRT_SLUG.get(), SquirtSlugRenderer::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(Constants.hudOverlayId, AmmunitionHudOverlay.HUD_AMMUNITION);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(GUN_LOAD_AMMO_KEY);
        event.register(GUN_AMMO_STATUS_DISPLAY_KEY);
    }

    @SubscribeEvent
    public static void onItemColorHandlerEvent(RegisterColorHandlersEvent.Item event) {
        for (Map.Entry<ChemicalPhial, Chemical> entry : ItemRegistration.PHIALS.entrySet()) {
            ChemicalPhial phial = entry.getKey();
            Chemical chemical = entry.getValue();
            Objects.requireNonNull(phial);
            if (chemical instanceof ElementItem) {
                event.register(((ElementItem) chemical)::getColor, phial);
            } else if (chemical instanceof CompoundItem){
                event.register(((CompoundItem) chemical)::getColor, phial);
            }
        }
    }
}
