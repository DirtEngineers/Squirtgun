package net.dirtengineers.squirtgun.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class GunAmmoLoadKeybind {
    public static final String KEY_CATEGORY_MOD = "key.category.squirtgun";
    public static final String KEY_GUN_LOAD_AMMO = "key.squirtgun.gun_ammo_load";

    public static final KeyMapping GUN_LOAD_AMMO_KEY = new KeyMapping(KEY_GUN_LOAD_AMMO, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_MOD);
}
