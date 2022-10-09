package net.dirtengineers.squirtgun.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Text {

    public static final Style HOVER_TEXT_STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT);

    public static List<Component> setAmmoHoverText(AmmunitionContainer container, String pItemName, List<Component> pTooltipComponents) {
        int textTint = CustomFluidStorage.EMPTY_FLUID_COLOR;
        if (container.isEmptyFluid())
            pTooltipComponents.add(Component.literal(padFluidNameForDisplay("empty", pItemName )).withStyle(HOVER_TEXT_STYLE.withColor(textTint)));
        else {
            String fluidName = padFluidNameForDisplay(container.getFriendlyFluidName(), pItemName);

            textTint = container.getFluidColor();
            pTooltipComponents.add(Component.literal(fluidName).withStyle(HOVER_TEXT_STYLE.withColor(textTint)));
            pTooltipComponents.add(Component.literal(buildAmmoStatus(container, fluidName.length())).withStyle(HOVER_TEXT_STYLE.withColor(textTint)));
        }
        return pTooltipComponents;
    }

    public static String padFluidNameForDisplay(String pFluidName, String pItemName){
        StringBuilder name = new StringBuilder();
        for (String s : Arrays.asList(" ".repeat(Math.max(0, (((pItemName).length() - pFluidName.length()) / 2))), pFluidName))
            name.append(s);
        return name.toString();
    }

    private static String buildAmmoStatus(AmmunitionContainer container, int fluidNameLength){
        StringBuilder ammoStatus = new StringBuilder();
        String status = container.getAmmoStatus();
        int padding = (fluidNameLength - status.length()) / 2;
        ammoStatus.append(" ".repeat(Math.max(0, padding + 1)));
        ammoStatus.append(status);
        return ammoStatus.toString();
    }

    public static Component ammoOverlayFluidName(AmmunitionContainer storage){
        return Component.literal(storage.getFriendlyFluidName()).withStyle(HOVER_TEXT_STYLE.withColor(storage.getFluidColor()));
    }

    public static Component ammoOverlayStatus(AmmunitionContainer storage){
        return Component.literal(storage.getAmmoStatus()).withStyle(HOVER_TEXT_STYLE.withColor(storage.getFluidColor()));
    }

    public static int drawCenteredStringNoShadow(PoseStack pPoseStack, Font pFont, Component pText, int pX, int pY){
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        return pFont.draw(
                pPoseStack,
                formattedcharsequence,
                (float)(pX - pFont.width(formattedcharsequence) / 2),
                (float)pY,
                Objects.requireNonNull(pText.getStyle().getColor()).getValue());
    }
}
