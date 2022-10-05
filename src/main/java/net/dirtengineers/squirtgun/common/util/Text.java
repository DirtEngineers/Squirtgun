package net.dirtengineers.squirtgun.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.common.item.SquirtMagazine;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.material.Fluid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Text {
    public static final Style hoverTextStyle = Style.EMPTY.withFont(Style.DEFAULT_FONT);

    public static List<Component> setAmmoHoverText(SquirtMagazine pMagazine, String pItemName, List<Component> pTooltipComponents) {
        AtomicReference<Integer> textTint = new AtomicReference<>(0XFFFFFFFF);
        if (Objects.equals(pMagazine.getFluidType().toString(), "minecraft:empty"))
            pTooltipComponents.add(Component.literal("  empty").withStyle(hoverTextStyle.withColor(textTint.get())));
        else {
            String fluidName = padFluidNameForDisplay(Common.getFriendlyFluidName(pMagazine.getFluid()), pItemName);

            textTint.set(Common.getFluidColor(pMagazine.getFluid()));
            pTooltipComponents.add(Component.literal(fluidName).withStyle(hoverTextStyle.withColor(textTint.get())));
            pTooltipComponents.add(Component.literal(buildAmmoStatus(pMagazine, fluidName.length())).withStyle(hoverTextStyle.withColor(textTint.get())));
        }
        return pTooltipComponents;
    }

    public static String padFluidNameForDisplay(String pFluidName, String pItemName){
        StringBuilder name = new StringBuilder();
        for (String s : Arrays.asList(" ".repeat(Math.max(0, (((pItemName).length() - pFluidName.length()) / 2))), pFluidName))
            name.append(s);
        return name.toString();
    }

    private static String buildAmmoStatus(SquirtMagazine pMagazine, int fluidNameLength){
        StringBuilder ammoStatus = new StringBuilder();
        String status = pMagazine.getShotsAvailable() + "/" + pMagazine.getMaxShots();
        int padding = (fluidNameLength - status.length()) / 2;
        ammoStatus.append(" ".repeat(Math.max(0, padding + 1)));
        ammoStatus.append(status);
        return ammoStatus.toString();
    }

    public static Component ammoOverlayFluidName(Fluid pFluid){
        if (Objects.equals(pFluid.getFluidType().toString(), "minecraft:empty"))
            return Component.literal("empty").withStyle(hoverTextStyle.withColor(0XFFFFFFFF));
        else
            return Component.literal(Common.getFriendlyFluidName(pFluid)).withStyle(hoverTextStyle.withColor(Common.getFluidColor(pFluid)));
    }

    public static Component ammoOverlayStatus(SquirtMagazine pMagazine, int color){
        return Component.literal(pMagazine.getShotsAvailable() + "/" + pMagazine.getMaxShots()).withStyle(hoverTextStyle.withColor(color));
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
