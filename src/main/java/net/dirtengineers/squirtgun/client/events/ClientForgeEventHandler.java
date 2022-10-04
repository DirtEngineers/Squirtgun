package net.dirtengineers.squirtgun.client.events;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Squirtgun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventHandler {
    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        if (event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().getItem() instanceof SquirtgunItem) {
            float f = 1f;
            int ticksUsingItem = event.getPlayer().getTicksUsingItem();
            if(ticksUsingItem >= 15){
                int x = 1;
            }
            float f1 = (float) ticksUsingItem / 20.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 *= f1;
            }

            f *= 1.0F - f1 * 0.15F;

            event.setNewFovModifier(f);
        }
    }

//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.side == LogicalSide.SERVER) {
//            if (event.player.getItemInHand(event.player.getUsedItemHand()).getItem() instanceof SquirtgunItem) {
////                HUD_AMMUNITION
//                String blah = "I'M HERE!";
//            }
////        if(event.player.getItemInHand(event.player.getUsedItemHand()).equals(new ItemStack(ItemRegistration.SQUIRTGUN.get()))){
////            String blah = "I'M HERE!";
////        }
//        }
//    }
}
