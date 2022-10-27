package net.dirtengineers.squirtgun.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.Compound;
import com.smashingmods.chemlib.common.items.CompoundItem;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TextUtility {

    public static final Style HOVER_TEXT_STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(ChatFormatting.YELLOW);
    public static String EMPTY_FLUID_NAME = "minecraft:empty";
    public static final String EMPTY_FLUID_NAME_KEY = "fluid.squirtgun.empty_fluid_name";

    public static List<Component> setAmmoHoverText(Optional<Fluid> pFluid, String pAmmoStatus, Item pItem, List<Component> pTooltipComponents) {
        String itemName = MutableComponent.create(new TranslatableContents(pItem.getDescriptionId())).getString();
        pTooltipComponents.add(
                Component.literal(
                                buildAmmoStatus(
                                        TextUtility.isEmptyFluid(pFluid) ?
                                                MutableComponent.create(new TranslatableContents(EMPTY_FLUID_NAME_KEY)).getString()
                                                : pAmmoStatus
                                        , itemName))
                        .withStyle(HOVER_TEXT_STYLE));
        return pTooltipComponents;
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

    public static Component getFriendlyFluidName(Optional<Fluid> pFluid) {
        if (!TextUtility.isEmptyFluid(pFluid)) {
            for (Map.Entry<Chemical, Fluid> entry : ItemRegistration.CHEMICAL_FLUIDS.entrySet()) {
                Chemical chemical = entry.getKey();
                Fluid fluid = entry.getValue();
                Objects.requireNonNull(chemical);
                Objects.requireNonNull(fluid);
                if (pFluid.isPresent() && fluid == pFluid.get()) {
                    if (chemical instanceof Compound) {
                        return MutableComponent.create(
                                new TranslatableContents(
                                        ((CompoundItem) chemical).getDescriptionId()
                                )
                        ).withStyle(HOVER_TEXT_STYLE);
                    } else {
                        return MutableComponent.create(
                                        new TranslatableContents(
                                                chemical.getChemicalName()
                                        )
                                )
                                .withStyle(HOVER_TEXT_STYLE);
                    }
                }
            }
        }
        return MutableComponent.create(new TranslatableContents(EMPTY_FLUID_NAME_KEY)).withStyle(HOVER_TEXT_STYLE);
    }

    public static boolean isEmptyFluid(Optional<Fluid> pFluid) {
        if (pFluid.isEmpty()) return true;
        return Objects.equals(pFluid.get().getFluidType().toString(), EMPTY_FLUID_NAME);
    }
}
