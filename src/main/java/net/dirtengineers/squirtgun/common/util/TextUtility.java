package net.dirtengineers.squirtgun.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.Compound;
import com.smashingmods.chemlib.common.items.CompoundItem;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TextUtility {

    public static final Style HOVER_TEXT_STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT);
    public static final int EMPTY_FLUID_COLOR = 0XFFFFFFFF;
    public static String EMPTY_FLUID_NAME = "minecraft:empty";

    public static List<Component> setAmmoHoverText(Optional<Fluid> pFluid, String pAmmoStatus, String pItemName, List<Component> pTooltipComponents) {
        //TODO: Translation?
        //Can modify item name
        //create translation key for "empty"
        int textTint = TextUtility.EMPTY_FLUID_COLOR;
        if (TextUtility.isEmptyFluid(pFluid))
            pTooltipComponents.add(Component.literal(padFluidNameForDisplay("empty", pItemName )).withStyle(HOVER_TEXT_STYLE.withColor(textTint)));
        else {
            String fluidName = padFluidNameForDisplay(TextUtility.getFriendlyFluidName(pFluid), pItemName);

            textTint = TextUtility.getFluidColor(pFluid);
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

    public static void drawCenteredStringNoShadow(PoseStack pPoseStack, Font pFont, Component pText, int pX, int pY){
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        pFont.draw(
                pPoseStack,
                formattedcharsequence,
                (float) (pX - pFont.width(formattedcharsequence) / 2),
                (float) pY,
                Objects.requireNonNull(pText.getStyle().getColor()).getValue());
    }

    public static String getFriendlyFluidName(Optional<Fluid> pFluid) {
        String result = "empty";
        if (!TextUtility.isEmptyFluid(pFluid)) {
            for (Map.Entry<Chemical, Fluid> entry : ItemRegistration.CHEMICAL_FLUIDS.entrySet()) {
                Chemical chemical = entry.getKey();
                Fluid fluid = entry.getValue();
                Objects.requireNonNull(chemical);
                Objects.requireNonNull(fluid);
                if (pFluid.isPresent() && fluid == pFluid.get()) {
                    result = chemical instanceof Compound ?
                            I18n.get(((CompoundItem) chemical).getDescriptionId()) :
                            chemical.getChemicalName();
                }
            }
        }
        return result;
    }

    public static boolean isEmptyFluid(Optional<Fluid> pFluid) {
        if (pFluid.isEmpty()) return true;
        return Objects.equals(pFluid.get().getFluidType().toString(), EMPTY_FLUID_NAME);
    }

    public static int getFluidColor(Optional<Fluid> pFluid) {
        AtomicInteger fluidColor = new AtomicInteger();
        if (TextUtility.isEmptyFluid(pFluid)) {
            fluidColor.set(EMPTY_FLUID_COLOR);
        }
        pFluid.ifPresentOrElse(
                fluid -> fluidColor.set(IClientFluidTypeExtensions.of(fluid.getFluidType()).getTintColor()),
                () -> fluidColor.set(EMPTY_FLUID_COLOR));
        return fluidColor.get();
    }
}
