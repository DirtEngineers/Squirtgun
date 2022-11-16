package net.dirtengineers.squirtgun.client.overlay;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.common.item.Squirtgun.SquirtgunItem;
import net.dirtengineers.squirtgun.util.TextUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

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
            if (player != null && player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SquirtgunItem pSquirtgun) {
                Component chemicalName = TextUtility.getFriendlyChemicalName(pSquirtgun.getChemical());
                TextUtility.drawCenteredStringNoShadow(poseStack, font, chemicalName, x, y - font.lineHeight);
                if(pSquirtgun.getChemical() != null) {
                    Component status = Component.literal(pSquirtgun.getAmmoStatus()).withStyle(Constants.HOVER_TEXT_STYLE);
                    TextUtility.drawCenteredStringNoShadow(poseStack, font, status, x, y);
                }
            }
        }
    };
}
