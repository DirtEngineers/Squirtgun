package net.dirtengineers.squirtgun.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import static net.dirtengineers.squirtgun.client.keybinds.Common.*;

public class GunAmmoLoadKeybind {
    public static final KeyMapping GUN_LOAD_AMMO_KEY = new KeyMapping(KEY_GUN_LOAD_AMMO, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_MOD);
}
