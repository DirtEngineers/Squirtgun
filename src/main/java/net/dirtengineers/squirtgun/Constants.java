package net.dirtengineers.squirtgun;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

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
    public static final String brassBlendItemName = "brass_blend";
    public static final String brassNuggetItemName = "brass_nugget";
    public static final String brassIngotItemName = "brass_ingot";
    public static final String brassBlockItemName = "brass_block_item";
    public static final String brassBlockName = "brass_block";
    public static final String phialCapItemName = "phial_cap";
    public static final String quartzShardItemName = "quartz_shard";
    public static final String actuatorItemName = "actuator";
    public static final String squirtgunTabName = "squirtguntab";

    // sound events
    public static final String SlugHitSoundName = "squirt_slug_hit";
    public static final String ReloadScreenCloseSoundName = "reload_screen_close";
    public static final String PhialSwapSoundName = "phial_swap";
    public static final String GunUseSoundName = "gun_use";
    public static final String GunFireSoundName = "gun_fire";
    public static final String GunDryFireSoundName = "gun_dry_fire";
    public static final String PhialCompleteSoundName = "phial_complete";
    public static final String EncapsulatorProcessingSoundName = "encapsulator_processing";

    // resource locations
    public static final String slugTextureLocation = "textures/entity/projectile/blank_slug.png";
    public static final String chemicalPhialCreationRecipeLocationPrefix = "encapsulator/chemical/";
    public static final String chemicalPhialCreationAdvancementLocationPrefix = "recipes/encapsulator/chemical/";
    public static final String potionPhialCreationRecipeLocationPrefix = "encapsulator/potion/";
    public static final String potionPhialCreationAdvancementLocationPrefix = "recipes/encapsulator/potion/";
    public static final String encapsulatorMenuScreenTexture = "textures/gui/encapsulator_gui.png";

    // translation keys
    public static final String gunFunctionality = "item.squirtgun.gun_functionality";
    public static final String emptyFluidNameKey = "fluid.squirtgun.empty_fluid_name";
    public static final String encapsulatorEnergyRequirementTooltipKey = "tooltip.squirtgun.energy_requirement";
    public static final String phialItemNameTranslationPrefix = "item.squirtgun.";
    public static final String encapsulatorMenuScreenTitle = String.format("%s.container.encapsulator", Squirtgun.MOD_ID);
    public static final String recipeRequiredInput = String.format("%s.container.required_input", Squirtgun.MOD_ID);
    public static final String currentSelectedRecipe = String.format("%s.container.current_recipe", Squirtgun.MOD_ID);
    public static final String guiSelectRecipe = String.format("%s.container.select_recipe", Squirtgun.MOD_ID);
    public static final String guiSearch = String.format("%s.container.search", Squirtgun.MOD_ID);
    public static final String gunGuiPhialIsLoaded = "tooltip.squirtgun.gui.phial_is_loaded";
    public static final String gunGuiPhialLoaded = "tooltip.squirtgun.gui.phial_loaded";
    public static final String gunGuiPhialAmmoLossWarning = "tooltip.squirtgun.gui.phial_ammo_loss";
    public static final String gunGuiCancelButtonMessage = "tooltip.squirtgun.gui.cancel_button_message";
    public static final String gunGuiAgreeButtonMessage = "tooltip.squirtgun.gui.agree_button_message";
    public static final String reloadScreenCurrentAmmunition = String.format("%s.reload_screen.current_ammunition", Squirtgun.MOD_ID);
    public static final String reloadScreenInventoryWarning = String.format("%s.reload_screen.inventory_warning", Squirtgun.MOD_ID);

    // general
    public static String EMPTY_FLUID_NAME = "minecraft:empty";
    public static final int DROP_ITEM_INDEX = Integer.MAX_VALUE;
    public static final int OFF_HAND_INDEX = -1;
    public static int SLUG_SHOT_SIZE_MB = 100;

    //Keybinds
    public static final String KEY_CATEGORY_MOD = "key.category.squirtgun";
    public static final String KEY_GUN_AMMO_STATUS_DISPLAY = "key.squirtgun.gun_display_ammo_status";
    public static final String openGunGui = String.format("%s.text.open_gui", Squirtgun.MOD_ID);

    //NBT
    public static final String SHOTS_AVAILABLE_TAG = Squirtgun.MOD_ID + ".shots_available";
    public static final String CHEMICAL_TAG = Squirtgun.MOD_ID + ".chemical";
    public static final String POTION_TAG = Squirtgun.MOD_ID + ".potion";

    //Recipes
    public static final String encapsulatorRecipeGroupName = String.format("%s:encapsulator", Squirtgun.MOD_ID);

    //Text style
    public static final Style HOVER_TEXT_STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(ChatFormatting.YELLOW);
    public static final Style MOD_ID_TEXT_STYLE = Style.EMPTY.withFont(Style.DEFAULT_FONT).withItalic(true).withColor(ChatFormatting.BLUE);
    public static final Style DISPLAY_ITEM_TEXT_STYLE = Style.EMPTY.withColor(ChatFormatting.WHITE);
    public static final Style RECIPE_ITEM_REQUIRED_TEXT_STYLE = Style.EMPTY.withUnderlined(true).withColor(ChatFormatting.YELLOW);

    //Colors
    public static int BRASS_COLOR = 0XC6A874;

    public enum HUD_DISPLAY_SETTING {
        OFF,
        ON,
        FADE
    }
}
