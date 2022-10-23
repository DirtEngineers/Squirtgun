package net.dirtengineers.squirtgun;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.common.registry.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod(Squirtgun.MOD_ID)
public class Squirtgun {
    public static final String MOD_ID = "squirtgun";

    public Squirtgun() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);
        modEventBus.addListener(this::commonSetupEvent);
        modEventBus.addListener(ItemRegistration::registerMagsAndSlugs);
    }

    private void commonSetupEvent(final FMLCommonSetupEvent event) {
        for (Chemical chemical : ItemRegistration.ammunitionChemicals)
            if (chemical.getFluidTypeReference().isPresent()) {
                String location = String.valueOf(chemical.getFluidTypeReference().get());
                if (!Objects.equals(location, "minecraft:water")) location += "_fluid";
                ItemRegistration.CHEMICAL_FLUIDS.put(chemical, ForgeRegistries.FLUIDS.getValue(new ResourceLocation(location)));
            }
    }
}