package net.dirtengineers.squirtgun.common.util;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.common.item.SquirtMagazine;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.function.Predicate;

public class Common {
    public static Map<Fluid, Chemical> Ammunition = new HashMap<>();

    public static final Predicate<FluidStack> SQUIRT_AMMUNITION_ONLY =
            (fluidStack) -> Common.Ammunition.containsKey(fluidStack.getFluid());

    public static final Style hoverTextStyle = Style.EMPTY.withFont(Style.DEFAULT_FONT);

    public static List<Component> setAmmoHoverText(SquirtMagazine pMagazine, String pItemName, List<Component> pTooltipComponents){
        if (Screen.hasShiftDown()) {
            int textTint = 0XFFFFFFFF;
            if (Objects.equals(pMagazine.getFluidType().toString(), "minecraft:empty"))
                pTooltipComponents.add(Component.literal("  empty").withStyle(Common.hoverTextStyle.withColor(textTint)));
            else {
                String fluidName = padFluidNameForDisplay(pMagazine.getFriendlyFluidName(), pItemName);

                textTint = IClientFluidTypeExtensions.of(pMagazine.getFluidType()).getTintColor();
                pTooltipComponents.add(Component.literal(fluidName).withStyle(Common.hoverTextStyle.withColor(textTint)));
                pTooltipComponents.add(Component.literal(buildAmmoStatus(pMagazine, fluidName.length())).withStyle(Common.hoverTextStyle.withColor(textTint)));
            }
        } else {
            pTooltipComponents.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }
        return pTooltipComponents;
    }

    private static String padFluidNameForDisplay(String pFluidName, String pItemName){
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
}

