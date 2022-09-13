package net.dirtengineers.squirtgun.client.events;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.render.SquirtAmmunitionRenderer;
import net.dirtengineers.squirtgun.common.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/*
    SET THESE UP FOR ALL AMMUNITION TYPES
    ALSO MAKE MODELS AND TEXTURES

    For examples:
    https://github.com/TeamTwilight/twilightforest/blob/1.19.x/src/main/java/twilightforest/init/TFEntities.java#L384
 */
@Mod.EventBusSubscriber(modid = Squirtgun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event){
        EntityRenderers.register(EntityRegistry.SQUIRT_SLUG.get(), SquirtAmmunitionRenderer::new);
    }
}
