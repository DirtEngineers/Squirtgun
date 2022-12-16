package net.dirtengineers.squirtgun.client.events;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.common.items.CompoundItem;
import com.smashingmods.chemlib.common.items.ElementItem;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.overlay.AmmunitionHudOverlay;
import net.dirtengineers.squirtgun.client.render.SquirtSlugRenderer;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.registry.BlockRegistration;
import net.dirtengineers.squirtgun.registry.EntityRegistration;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
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
import static net.dirtengineers.squirtgun.client.Keybinds.SQUIRTGUN_MENU;

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
        event.register(GUN_AMMO_STATUS_DISPLAY_KEY);
        event.register(SQUIRTGUN_MENU);
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

        event.register((itemStack, i) -> Constants.BRASS_COLOR
                , ItemRegistration.BRASS_BLEND.get()
                , ItemRegistration.BRASS_NUGGET.get()
                , ItemRegistration.BRASS_INGOT.get()
                , BlockRegistration.BRASS_BLOCK.get()
        );
    }

    @SubscribeEvent
    public static void onBlockColorHandlerEvent(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex1) -> Constants.BRASS_COLOR, BlockRegistration.BRASS_BLOCK.get());
    }
}
