package net.dirtengineers.squirtgun.client.overlay;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.dirtengineers.squirtgun.client.capabilities.squirtgun.IAmmunitionCapability;
import net.dirtengineers.squirtgun.client.utility.TextUtility;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Objects;

public class AmmunitionHudOverlay {

    private static int fadeTicks = 0;
    private static final int maxFadeTicks = 200;
    public static boolean display = true;
    public static Constants.HUD_DISPLAY_SETTING displaySetting = Constants.HUD_DISPLAY_SETTING.ON;

    public static void resetFadeTicks() {
        fadeTicks = 0;
    }

    public static final IGuiOverlay HUD_AMMUNITION = (gui, poseStack, partialTick, width, height) -> {
        if(AmmunitionHudOverlay.displaySetting == Constants.HUD_DISPLAY_SETTING.FADE) {
            fadeTicks ++;
            if(fadeTicks >= maxFadeTicks) {
                AmmunitionHudOverlay.displaySetting = Constants.HUD_DISPLAY_SETTING.OFF;
                resetFadeTicks();
            }
        }

        if(AmmunitionHudOverlay.displaySetting != Constants.HUD_DISPLAY_SETTING.OFF) {
            int x = width / 2;
            int y = 15;
            Font font = gui.getFont();
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ItemStack pStack = player.getItemInHand(player.getUsedItemHand());

                if (pStack.getItem() instanceof SquirtgunItem) {
                    IAmmunitionCapability ammunitionHandler = pStack.getCapability(SquirtgunCapabilities.SQUIRTGUN_AMMO, null).orElse(null);

                    Component ammoName = TextUtility.getFriendlyChemicalName(ammunitionHandler.getChemical());
                    if (!Objects.equals(ammunitionHandler.getPotionKey(), "")) {
                        ammoName = TextUtility.getFriendlyPotionName(ammunitionHandler.getPotionKey());
                    }
                    TextUtility.drawCenteredStringNoShadow(poseStack, font, ammoName, x, y - font.lineHeight);
                        Component status = Component.literal(ammunitionHandler.getAmmoStatus()).withStyle(Constants.HOVER_TEXT_STYLE);
                        TextUtility.drawCenteredStringNoShadow(poseStack, font, status, x, y);
                }
            }
        }
    };
}
