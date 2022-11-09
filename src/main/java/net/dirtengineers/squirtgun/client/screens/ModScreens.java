package net.dirtengineers.squirtgun.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ModScreens {
    public static void openGunSettingsScreen(ItemStack itemstack) {
        Minecraft.getInstance().setScreen(new SquirtgunReloadScreen(itemstack));
    }
}
