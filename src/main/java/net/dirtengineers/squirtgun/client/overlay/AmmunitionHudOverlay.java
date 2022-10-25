package net.dirtengineers.squirtgun.client.overlay;

import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.dirtengineers.squirtgun.common.util.TextUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Objects;
import java.util.Optional;

public class AmmunitionHudOverlay {

    public static boolean display = true;
    public static final IGuiOverlay HUD_AMMUNITION = (gui, poseStack, partialTick, width, height) -> {
        if(AmmunitionHudOverlay.display) {
            int x = width / 2;
            int y = 15;
            Font font = gui.getFont();

            Player player = Minecraft.getInstance().player;
            assert player != null;
            if (player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SquirtgunItem pSquirtgun) {
                Optional<Fluid> fluid = pSquirtgun.getFluid();
                Component fluidName = Component.literal(TextUtility.getFriendlyFluidName(fluid)).withStyle(TextUtility.HOVER_TEXT_STYLE.withColor(TextUtility.getFluidColor(fluid)));
                if (Objects.equals(fluidName.getString(), "empty"))
                    TextUtility.drawCenteredStringNoShadow(poseStack, font, fluidName, x, y - font.lineHeight);
                else {
                    Component status = Component.literal(pSquirtgun.getAmmoStatus()).withStyle(TextUtility.HOVER_TEXT_STYLE.withColor(TextUtility.getFluidColor(fluid)));
                    TextUtility.drawCenteredStringNoShadow(poseStack, font, fluidName, x, y - font.lineHeight);
                    TextUtility.drawCenteredStringNoShadow(poseStack, font, status, x, y);
                }
            }
        }
    };
}
