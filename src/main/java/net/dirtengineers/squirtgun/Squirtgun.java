package net.dirtengineers.squirtgun;

import net.dirtengineers.squirtgun.client.overlay.AmmunitionHudOverlay;
import net.dirtengineers.squirtgun.client.render.SquirtSlugRenderer;
import net.dirtengineers.squirtgun.common.block.EncapsulatorScreen;
import net.dirtengineers.squirtgun.common.network.PacketHandler;
import net.dirtengineers.squirtgun.registry.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.dirtengineers.squirtgun.client.Keybinds.GUN_AMMO_STATUS_DISPLAY_KEY;
import static net.dirtengineers.squirtgun.client.Keybinds.shiftClickGuiBinding;

@Mod(Squirtgun.MOD_ID)
public class Squirtgun {
    public static final String MOD_ID = "squirtgun";
    public static final PacketHandler PACKET_HANDLER = new PacketHandler().register();

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Squirtgun() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);
        modEventBus.addListener(this::commonSetupEvent);
        modEventBus.addListener(this::clientSetupEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        Config.loadConfig(Config.COMMON_SPEC, FMLPaths.CONFIGDIR.get().resolve(String.format("%s-common.toml", Squirtgun.MOD_ID)));
        ForgeMod.enableMilkFluid();
    }

    public void clientSetupEvent(FMLClientSetupEvent pEvent){
        pEvent.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(GUN_AMMO_STATUS_DISPLAY_KEY);
            ClientRegistry.registerKeyBinding(shiftClickGuiBinding);
            EntityRenderers.register(EntityRegistration.SQUIRT_SLUG.get(), SquirtSlugRenderer::new);
            MenuScreens.register(MenuRegistration.ENCAPSULATOR_MENU.get(), EncapsulatorScreen::new);
            OverlayRegistry.registerOverlayTop(Constants.hudOverlayId, AmmunitionHudOverlay.HUD_AMMUNITION);
        });
    }

    private void commonSetupEvent(final FMLCommonSetupEvent event) {
        ItemRegistration.buildChemical_Fluids();
        RecipeRegistration.CHEMICAL_PHIAL_TYPE = RecipeType.register(String.format("%s:chemical_phial", MOD_ID));
        RecipeRegistration.POTION_PHIAL_TYPE = RecipeType.register(String.format("%s:potion_phial", MOD_ID));
    }
}