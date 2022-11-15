package net.dirtengineers.squirtgun.client.screens;

import net.minecraft.client.Minecraft;

public class ModScreens {
    public static void openGunSettingsScreen() {
        Minecraft.getInstance().setScreen(new SquirtgunReloadScreen());
    }
}
