package net.dirtengineers.squirtgun.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Text {

    public static final Style HOVER_TEXT_STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT);

    public static List<Component> setAmmoHoverText(Fluid pFluid, String pAmmoStatus, String pItemName, List<Component> pTooltipComponents) {
        //Can modify item name
        //create translation key for "empty"
        int textTint = AmmunitionUtility.EMPTY_FLUID_COLOR;
        if (AmmunitionUtility.isEmptyFluid(pFluid))
            pTooltipComponents.add(Component.literal(padFluidNameForDisplay("empty", pItemName )).withStyle(HOVER_TEXT_STYLE.withColor(textTint)));
        else {
            String fluidName = padFluidNameForDisplay(AmmunitionUtility.getFriendlyFluidName(pFluid), pItemName);

            textTint = AmmunitionUtility.getFluidColor(pFluid);
            pTooltipComponents.add(Component.literal(fluidName).withStyle(HOVER_TEXT_STYLE.withColor(textTint)));
            pTooltipComponents.add(Component.literal(buildAmmoStatus(pAmmoStatus, fluidName)).withStyle(HOVER_TEXT_STYLE.withColor(textTint)));
        }
        return pTooltipComponents;
    }

    public static String padFluidNameForDisplay(String pFluidName, String pItemName){
        StringBuilder name = new StringBuilder();
        for (String s : Arrays.asList(" ".repeat(Math.max(0, (((pItemName).length() - pFluidName.length()) / 2))), pFluidName))
            name.append(s);
        return name.toString();
    }

    private static String buildAmmoStatus(String pAmmoStatus, String pFluidName){
        StringBuilder ammoStatus = new StringBuilder();
        int padding = (pFluidName.length() - pAmmoStatus.length()) / 2;
        ammoStatus.append(" ".repeat(Math.max(0, padding + 1)));
        ammoStatus.append(pAmmoStatus);
        return ammoStatus.toString();
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
