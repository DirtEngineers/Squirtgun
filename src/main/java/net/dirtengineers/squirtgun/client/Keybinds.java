package net.dirtengineers.squirtgun.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybinds {

    public static final String KEY_CATEGORY_MOD = "key.category.squirtgun";
    public static final String KEY_GUN_LOAD_AMMO = "key.squirtgun.gun_ammo_load";
    public static final String KEY_GUN_AMMO_STATUS_DISPLAY = "key.squirtgun.gun_display_ammo_status";

    public static final KeyMapping GUN_AMMO_STATUS_DISPLAY_KEY =
            new KeyMapping(KEY_GUN_AMMO_STATUS_DISPLAY
                    , KeyConflictContext.IN_GAME
                    , InputConstants.Type.KEYSYM
                    , GLFW.GLFW_KEY_Y
                    , KEY_CATEGORY_MOD);

    public static final KeyMapping GUN_LOAD_AMMO_KEY =
            new KeyMapping(KEY_GUN_LOAD_AMMO
                    , KeyConflictContext.IN_GAME
                    , InputConstants.Type.KEYSYM
                    , GLFW.GLFW_KEY_G
                    , KEY_CATEGORY_MOD);
}
