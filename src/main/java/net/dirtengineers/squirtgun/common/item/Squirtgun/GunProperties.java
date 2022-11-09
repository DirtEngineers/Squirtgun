package net.dirtengineers.squirtgun.common.item.Squirtgun;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class GunProperties {
    private static final String CAN_FIRE = "canFire";
    public static boolean setCanFire(ItemStack pStack, boolean canFire) {
        pStack.getOrCreateTag().putBoolean(CAN_FIRE, canFire);
        return canFire;
    }

    public static boolean getCanFire(ItemStack pStack) {
        CompoundTag compound = pStack.getOrCreateTag();
        return !compound.contains(CAN_FIRE) ? setCanFire(pStack, true) : compound.getBoolean(CAN_FIRE);
    }
}
