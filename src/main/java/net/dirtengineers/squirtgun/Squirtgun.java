package net.dirtengineers.squirtgun;

import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.common.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static net.dirtengineers.squirtgun.common.util.Common.AmmunitionFluids;

@Mod(Squirtgun.MOD_ID)
public class Squirtgun
{
    public static final String MOD_ID = "squirtgun";

    public Squirtgun()
    {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);
        modEventBus.addListener(this::commonSetupEvent);
    }

    private void commonSetupEvent(final FMLCommonSetupEvent event){
        AmmunitionFluids  = FluidRegistry.getLiquidSourceFluids();
    }
}
