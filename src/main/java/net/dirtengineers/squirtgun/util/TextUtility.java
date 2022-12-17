package net.dirtengineers.squirtgun.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;

import java.util.LinkedList;
import java.util.Objects;

public class TextUtility {

    public static LinkedList<String> padStrings(LinkedList<String> pStrings) {
        LinkedList<String> outList = new LinkedList<>();
        if (!pStrings.isEmpty()) {
            int max = pStrings.stream().map(String::length).max(Integer::compareTo).get();
            for (String pString : pStrings) {
                if (pString.length() < max) {
                    outList.add(" ".repeat(Math.max(0, ((max - pString.length()) / 2))) + pString);
                } else {
                    outList.add(pString);
                }
            }
        }
        return outList;
    }

    public static void drawCenteredStringNoShadow(PoseStack pPoseStack, Font pFont, Component pText, int pX, int pY) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        pFont.draw(
                pPoseStack,
                formattedcharsequence,
                (float) (pX - pFont.width(formattedcharsequence) / 2),
                (float) pY,
                Objects.requireNonNull(pText.getStyle().getColor()).getValue());
    }

    public static Component getFriendlyChemicalName(Chemical pChemical) {
        return MutableComponent.create(
                        new TranslatableContents(pChemical != null ?
                                String.format("item.%s.%s", pChemical.getClass().getModule().getName(), pChemical.asItem())
                                : Constants.emptyFluidNameKey))
                .withStyle(Constants.HOVER_TEXT_STYLE);
    }

    public static Component getFriendlyPotionName(String pPotionKey) {
        return MutableComponent.create(
                        new TranslatableContents(!Objects.equals(pPotionKey, "") ?
                                pPotionKey
                                : Constants.emptyFluidNameKey))
                .withStyle(Constants.HOVER_TEXT_STYLE);
    }

    public static String capitalizeText(String text, char delimiter) {
        final char[] buffer = text.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (ch == delimiter) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }
}
