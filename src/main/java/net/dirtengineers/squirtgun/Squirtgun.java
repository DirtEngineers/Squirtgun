package net.dirtengineers.squirtgun;

import net.dirtengineers.squirtgun.common.block.EncapsulatorScreen;
import net.dirtengineers.squirtgun.common.network.PacketHandler;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.dirtengineers.squirtgun.registry.MenuRegistration;
import net.dirtengineers.squirtgun.registry.Registry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Squirtgun.MOD_ID)
public class Squirtgun {
    public static final String MOD_ID = "squirtgun";
    public static final PacketHandler PACKET_HANDLER = new PacketHandler().register();

    public Squirtgun() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);
        modEventBus.addListener(this::commonSetupEvent);
        modEventBus.addListener(this::clientSetupEvent);
        modEventBus.addListener(ItemRegistration::registerPhialsAndSlugs);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        Config.loadConfig(Config.COMMON_SPEC, FMLPaths.CONFIGDIR.get().resolve(String.format("%s-common.toml", Squirtgun.MOD_ID)));
        ForgeMod.enableMilkFluid();
    }

    @SubscribeEvent
    public void clientSetupEvent(FMLClientSetupEvent event){
        event.enqueueWork(() -> MenuScreens.register(MenuRegistration.ENCAPSULATOR_MENU.get(), EncapsulatorScreen::new));
    }

    private void commonSetupEvent(final FMLCommonSetupEvent event) {
        ItemRegistration.buildChemical_Fluids();
    }
}