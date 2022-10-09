package net.dirtengineers.squirtgun.common.util;

import com.smashingmods.chemlib.api.Chemical;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Common {
    public static Map<Fluid, Chemical> Ammunition = new HashMap<>();

    public static final Predicate<FluidStack> SQUIRT_AMMUNITION_ONLY =
            (fluidStack) -> Common.Ammunition.containsKey(fluidStack.getFluid());


    public static String getFriendlyItemName(Item pItem){
        return I18n.get(pItem.getDescriptionId());
    }




}

