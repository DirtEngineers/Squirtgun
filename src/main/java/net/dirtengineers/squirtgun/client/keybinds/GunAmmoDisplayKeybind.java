package net.dirtengineers.squirtgun.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import static net.dirtengineers.squirtgun.client.keybinds.Common.*;

public class GunAmmoDisplayKeybind {
    public static final KeyMapping GUN_AMMO_STATUS_DISPLAY_KEY = new KeyMapping(KEY_GUN_AMMO_STATUS_DISPLAY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, KEY_CATEGORY_MOD);
}
