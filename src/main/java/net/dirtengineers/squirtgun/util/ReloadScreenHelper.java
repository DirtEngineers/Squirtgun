package net.dirtengineers.squirtgun.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.LinkedList;

public class ReloadScreenHelper {

    public static ItemStack phialSwapStack = ItemStack.EMPTY;

    public static LinkedList<ItemStack> phials = new LinkedList<>();

    public static int offhandLocationIndex = -1;


    public static void addToPhials(ItemStack pStack) {
        if (phialsGetStackIndex(pStack) < 0) {
            phials.add(pStack);
        }
    }

    public static int phialsGetStackIndex(ItemStack pStack) {
        for (ItemStack phial : ReloadScreenHelper.phials) {
            if (ForgeRegistries.ITEMS.getResourceKey(phial.getItem()).equals(ForgeRegistries.ITEMS.getResourceKey(pStack.getItem()))) {
                return ReloadScreenHelper.phials.indexOf(phial);
            }
        }
        return -1;
    }

    public static class ItemStackComparator implements Comparator<ItemStack> {

        @Override
        public int compare(ItemStack itemStack1, ItemStack itemStack2) {
            return Integer.compare(getItemPath(itemStack1).compareToIgnoreCase(getItemPath(itemStack2)), 0);
        }

        public static String getItemPath(ItemStack pStack) {
            ResourceKey<Item> key = ForgeRegistries.ITEMS.getResourceKey(pStack.getItem()).orElse(null);
            return key != null ? key.location().getPath() : "";
        }
    }
}
