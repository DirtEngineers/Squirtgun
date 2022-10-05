package net.dirtengineers.squirtgun.client.overlay;

import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.dirtengineers.squirtgun.common.util.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import java.util.Objects;

public class AmmunitionHudOverlay {
    public static final IGuiOverlay HUD_AMMUNITION = (gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height - 30;
        Font font = gui.getFont();

        Player player = Minecraft.getInstance().player;
        assert player != null;
        if(player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SquirtgunItem pSquirtgun) {
            Component fluidName = Text.ammoOverlayFluidName(pSquirtgun.getMagazine().getFluid());
            if (Objects.equals(fluidName.getString(), "empty"))
                Text.drawCenteredStringNoShadow(poseStack, font, fluidName, x, y - font.lineHeight);
            else {
                Component status = Text.ammoOverlayStatus(pSquirtgun.getMagazine(), Objects.requireNonNull(fluidName.getStyle().getColor()).getValue());
                Text.drawCenteredStringNoShadow(poseStack, font, fluidName, x, y - font.lineHeight);
                Text.drawCenteredStringNoShadow(poseStack, font, status, x, y);
            }
        }
    };
}
