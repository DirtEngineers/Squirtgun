package net.dirtengineers.squirtgun.client.events;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.registry.BlockRegistration;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import static net.dirtengineers.squirtgun.registry.ItemRegistration.CHEMICAL_PHIALS;
import static net.dirtengineers.squirtgun.registry.ItemRegistration.POTION_PHIALS;

@Mod.EventBusSubscriber(modid = Squirtgun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {

    @SubscribeEvent
    public static void onItemColorHandlerEvent(ColorHandlerEvent.Item pEvent) {
        CHEMICAL_PHIALS.forEach(phial -> pEvent.getItemColors().register((pItemStack, pTintIndex) -> pTintIndex > 0 ? -1 : phial.getChemical().getColor(), phial));
        POTION_PHIALS.forEach(phial -> pEvent.getItemColors().register((pItemStack, pTintIndex) -> pTintIndex > 0 ? -1 : PotionUtils.getColor(Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation(phial.getPotionLocation())))), phial));

        pEvent.getItemColors().register((itemStack, i) -> Constants.BRASS_COLOR,
                ItemRegistration.BRASS_BLEND.get(),
                ItemRegistration.BRASS_NUGGET.get(),
                ItemRegistration.BRASS_INGOT.get(),
                BlockRegistration.BRASS_BLOCK.get()
        );
    }

    @SubscribeEvent
    public static void onBlockColorHandlerEvent(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> Constants.BRASS_COLOR, BlockRegistration.BRASS_BLOCK.get());
    }
}
