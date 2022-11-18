package net.dirtengineers.squirtgun;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class Constants {
    //Ids
    public static final String hudOverlayId = "ammunition_readout";
    public static final String categoryEncapsulator = "encapsulator";
    public static final String encapsulatorBlockName = "encapsulator_block";
    public static final String encapsulatorBlockEntityName = "encapsulator_block_entity";
    public static final String slugEntityEntityName = "slug_entity";
    public static final String phialItemName = "phial";
    public static final String slugItemName = "slugitem";
    public static final String gunItemName = "squirtgunitem";
    public static final String squirtgunTabName = "squirtguntab";
    public static final String encapsulatorMenuName = "encapsulator_menu";
    public static final String phialCreationSerializerName = "phial_creation_serializer";
    public static final String phialCreationRecipeName = "phial_creation_recipe";

    // sound events
    public static final String SlugHitSoundName = "squirt_slug_hit";
    public static final String ReloadScreenCloseSoundName = "reload_screen_close";
    public static final String PhialSwapSoundName = "phial_swap";
    public static final String GunUseSoundName = "gun_use";
    public static final String GunFireSoundName = "gun_fire";
    public static final String PhialCompleteSoundName = "phial_complete";
    public static final String EncapsulatorProcessingSoundName = "encapsulator_processing";

    // resource locations
    public static final String slugTextureLocation = "textures/entity/projectile/blank_slug.png";
    public static final String encapsulatorPhialCreationRecipeLocationPrefix = "encapsulator/";
    public static final String encapsulatorPhialCreationAdvancementLocationPrefix = "recipes/encapsulator/";
    public static final String encapsulatorMenuScreenTexture = "textures/gui/encapsulator_gui.png";
    public static final ResourceLocation phialReloadScreenButtonTexture = new ResourceLocation(Squirtgun.MOD_ID, "textures/item/squirt_phial_white.png");

    // translation keys
    public static final String gunFunctionality = "item.squirtgun.gun_functionality";
    public static final String emptyFluidNameKey = "fluid.squirtgun.empty_fluid_name";
    public static final String encapsulatorEnergyRequirementTooltipKey = "tooltip.squirtgun.energy_requirement";
    public static final String phialItemNameTranslationPrefix = "item.squirtgun.";
    public static final String encapsulatorMenuScreenTitle = String.format("%s.container.encapsulator", Squirtgun.MOD_ID);
    public static final String encapsulatorBlockNameKey = String.format("block.%s.%s", Squirtgun.MOD_ID, encapsulatorBlockName);
    public static final String openGunGui = String.format("%s.text.open_gui", Squirtgun.MOD_ID);

    // Translation strings
    public static final String reloadScreenCurrentAmmunition = String.format("%s.reload_screen.current_ammunition", Squirtgun.MOD_ID);
    public static final String reloadScreenInventoryWarning = String.format("%s.reload_screen.inventory_warning", Squirtgun.MOD_ID);

    // general
    public static String EMPTY_FLUID_NAME = "minecraft:empty";
    public static int DROP_ITEM_INDEX = Integer.MAX_VALUE;
    public static int OFF_HAND_INDEX = -1;
    public static int SLUG_SHOT_SIZE_MB = 100;

    //Keybinds
    public static final String KEY_CATEGORY_MOD = "key.category.squirtgun";
    public static final String KEY_GUN_AMMO_STATUS_DISPLAY = "key.squirtgun.gun_display_ammo_status";

    //Item Groups
    public static final String squirtgunTabItemGroup = "itemGroup.squirtguntab";
    public static final String squirtgunItemGroup = "itemGroup.squirtgun";

    //NBT
    public static final String SHOTS_AVAILABLE_TAG = Squirtgun.MOD_ID + ".shots_available";
    public static final String MAX_SHOTS_TAG = Squirtgun.MOD_ID + ".max_shots";
    public static final String CHEMICAL_TAG = Squirtgun.MOD_ID + ".chemical";

    //Recipes
    public static final String encapsulatorRecipeGroupName = String.format("%s:encapsulator", Squirtgun.MOD_ID);

    //Text style
    public static final Style HOVER_TEXT_STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(ChatFormatting.YELLOW);

    public enum HUD_DISPLAY_SETTING {
        OFF,
        ON,
        FADE;

        HUD_DISPLAY_SETTING() {}

        public HUD_DISPLAY_SETTING getNext() {
            return values()[(ordinal() + 1) % values().length];
        }
    }
}
