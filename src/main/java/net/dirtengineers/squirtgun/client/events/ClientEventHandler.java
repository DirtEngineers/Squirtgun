package net.dirtengineers.squirtgun.client.events;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.render.SquirtSlugRenderer;
import net.dirtengineers.squirtgun.common.registry.EntityRegistration;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


//@Mod.EventBusSubscriber(modid = Squirtgun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
//    @SubscribeEvent
//    public static void doSetup(FMLClientSetupEvent event){
//        EntityRenderers.register(EntityRegistration.SQUIRT_SLUG.get(), SquirtSlugRenderer::new);
//    }
//
//    @SubscribeEvent
//    public static float onComputeFovModifierEvent(ComputeFovModifierEvent event) {
//        float f = 1F;
//        Player player = event.getPlayer();
//        ItemStack itemstack = player.getUseItem();
//        if (player.isUsingItem()) {
//            if (itemstack.is(ItemRegistration.SQUIRTGUN.get())) {
//                int i = player.getTicksUsingItem();
//                float f1 = (float) i / 20.0F;
//                if (f1 > 1.0F) {
//                    f1 = 1.0F;
//                } else {
//                    f1 *= f1;
//                }
//
//                f *= 1.0F - f1 * 0.15F;
//            } else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping()) {
//                return 0.1F;
//            }
//        }
//        return net.minecraftforge.client.ForgeHooksClient.getFieldOfViewModifier(player, f);
//    }
}



