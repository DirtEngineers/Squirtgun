package net.dirtengineers.squirtgun.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    public static Component getFriendlyFluidName(Optional<Fluid> pFluid) {
        if (!TextUtility.isEmptyFluid(pFluid)) {
            for (Map.Entry<Chemical, Fluid> entry : ItemRegistration.CHEMICAL_FLUIDS.entrySet()) {
                Chemical chemical = entry.getKey();
                Fluid fluid = entry.getValue();
                Objects.requireNonNull(chemical);
                Objects.requireNonNull(fluid);
                if (pFluid.isPresent() && fluid == pFluid.get()) {
                    return MutableComponent.create(
                            new TranslatableContents(
                                    StringUtils.capitalize(chemical.getChemicalName()))
                    ).withStyle(Constants.HOVER_TEXT_STYLE);
                }
            }
        }
        return MutableComponent.create(new TranslatableContents(Constants.emptyFluidNameKey)).withStyle(Constants.HOVER_TEXT_STYLE);
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

    public static boolean isEmptyFluid(Optional<Fluid> pFluid) {
        if (pFluid.isEmpty()) return true;
        return Objects.equals(pFluid.get().getFluidType().toString(), Constants.EMPTY_FLUID_NAME);
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
