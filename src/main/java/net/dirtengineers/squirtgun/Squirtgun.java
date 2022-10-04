package net.dirtengineers.squirtgun;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.common.registry.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.smashingmods.chemlib.api.MatterState.LIQUID;
import static com.smashingmods.chemlib.registry.ItemRegistry.getCompounds;
import static com.smashingmods.chemlib.registry.ItemRegistry.getElements;
import static net.dirtengineers.squirtgun.common.util.Common.Ammunition;

@Mod(Squirtgun.MOD_ID)
public class Squirtgun {
    public static final String MOD_ID = "squirtgun";

    public Squirtgun() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);
        modEventBus.addListener(this::commonSetupEvent);
    }

    private void commonSetupEvent(final FMLCommonSetupEvent event) {
        List<Chemical> AmmunitionLiquids = new ArrayList<>();
        AmmunitionLiquids.addAll(getCompounds().stream().filter(compound -> compound.getMatterState() == LIQUID).toList());
        AmmunitionLiquids.addAll(getElements().stream().filter(element -> element.getMatterState() == LIQUID).toList());

        for (Chemical chemical : AmmunitionLiquids)
            if (chemical.getFluidTypeReference().isPresent()) {
                String location = String.valueOf(chemical.getFluidTypeReference().get());
                if (!Objects.equals(location, "minecraft:water")) location += "_fluid";
                Ammunition.put(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(location)), chemical);
            }
    }
}