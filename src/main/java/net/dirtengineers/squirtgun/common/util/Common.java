package net.dirtengineers.squirtgun.common.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Common {

    public static List<Fluid> AmmunitionFluids = new ArrayList<>();

    public static final Predicate<ItemStack> SQUIRT_MAGAZINE_ONLY =
            (itemStack) -> Common.AmmunitionFluids.contains(itemStack.getItem());

    public static final Predicate<ItemStack> SQUIRT_FLUIDS_ONLY =
            (itemStack) -> Common.AmmunitionFluids.contains(itemStack.getItem());
}
