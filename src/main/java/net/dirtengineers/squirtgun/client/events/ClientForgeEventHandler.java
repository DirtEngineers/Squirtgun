package net.dirtengineers.squirtgun.client.events;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.Keybinds;
import net.dirtengineers.squirtgun.client.overlay.AmmunitionHudOverlay;
import net.dirtengineers.squirtgun.client.screens.ModScreens;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.dirtengineers.squirtgun.client.Keybinds.GUN_AMMO_STATUS_DISPLAY_KEY;

@Mod.EventBusSubscriber(modid = Squirtgun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventHandler {
    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        if (event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().getItem() instanceof SquirtgunItem) {
            float fovModifier = 1f;
            int ticksUsingItem = event.getPlayer().getTicksUsingItem();
            float deltaTicks = (float) ticksUsingItem / 20.0F;
            if (deltaTicks > 1.0F) {
                deltaTicks = 1.0F;
            } else {
                deltaTicks *= deltaTicks;
            }
            fovModifier *= 1.0F - deltaTicks * 0.15F;
            event.setNewFovModifier(fovModifier);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            boolean hasSquirtGun = player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SquirtgunItem;

            if (Keybinds.SQUIRTGUN_MENU.consumeClick() && Minecraft.getInstance().screen == null && hasSquirtGun) {
                ModScreens.openGunSettingsScreen();
            }
            if (GUN_AMMO_STATUS_DISPLAY_KEY.consumeClick() && hasSquirtGun) {
                if(AmmunitionHudOverlay.displaySetting == Constants.HUD_DISPLAY_SETTING.ON) {
                    AmmunitionHudOverlay.resetFadeTicks();
                    AmmunitionHudOverlay.displaySetting = Constants.HUD_DISPLAY_SETTING.FADE;
                }
                if(AmmunitionHudOverlay.displaySetting == Constants.HUD_DISPLAY_SETTING.OFF) {
                    AmmunitionHudOverlay.displaySetting = Constants.HUD_DISPLAY_SETTING.ON;
                }
                AmmunitionHudOverlay.display = !AmmunitionHudOverlay.display;
            }
        }
    }
}
