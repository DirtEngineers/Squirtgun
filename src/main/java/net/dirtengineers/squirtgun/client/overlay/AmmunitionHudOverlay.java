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

    public static boolean display = true;
    public static final IGuiOverlay HUD_AMMUNITION = (gui, poseStack, partialTick, width, height) -> {
        if(AmmunitionHudOverlay.display) {
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
