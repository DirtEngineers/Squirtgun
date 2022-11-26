package net.dirtengineers.squirtgun.datagen.recipe;

import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static net.dirtengineers.squirtgun.datagen.recipe.ItemDefinitions.*;

public class GunComponentRecipes extends net.minecraft.data.recipes.RecipeProvider {
    public GunComponentRecipes(DataGenerator pGenerator) {
        super(pGenerator);
    }

    public static void buildRecipes(Consumer<FinishedRecipe> pConsumer) {
        generateGunGrip(pConsumer);
        generateBarrelSection(pConsumer);
        generatePhialMount(pConsumer);
        generateActuator(pConsumer);
        generateGun(pConsumer);
    }

    private static void generateGunGrip(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(GUN_GRIP)
                .group(Squirtgun.MOD_ID)
                .define('I', Items.IRON_NUGGET)
                .define('B', BRASS_NUGGET)
                .define('P', PVC)
                .pattern(" B ")
                .pattern("P I")
                .pattern(" I ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.IRON_NUGGET, BRASS_NUGGET, PVC}).build()))
                .save(pConsumer, getSimpleRecipeName(GUN_GRIP));
    }

    private static void generateBarrelSection(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(GUN_BARREL_SECTION)
                .group(Squirtgun.MOD_ID)
                .define('I', Items.IRON_NUGGET)
                .define('B', BRASS_NUGGET)
                .define('P', PVC)
                .pattern(" B ")
                .pattern("PII")
                .pattern("   ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.IRON_NUGGET, BRASS_NUGGET, PVC}).build()))
                .save(pConsumer, getSimpleRecipeName(GUN_BARREL_SECTION));
    }

    private static void generatePhialMount(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(PHIAL_MOUNT)
                .group(Squirtgun.MOD_ID)
                .define('B', BRASS_NUGGET)
                .define('P', PVC)
                .pattern(" P ")
                .pattern("B B")
                .pattern(" P ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET, PVC}).build()))
                .save(pConsumer, getSimpleRecipeName(PHIAL_MOUNT));
    }

    private static void generateActuator(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(GUN_ACTUATOR)
                .group(Squirtgun.MOD_ID)
                .define('B', BRASS_NUGGET)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.REDSTONE)
                .define('G', GRAPHITE_DUST)
                .define('A', RADIUM_DUST)
                .pattern(" A ")
                .pattern("NBN")
                .pattern("RGR")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET, GRAPHITE_DUST, RADIUM_DUST}).build()))
                .save(pConsumer, getSimpleRecipeName(GUN_ACTUATOR));
    }

    private static void generateGun(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(GUN)
                .group(Squirtgun.MOD_ID)
                .define('G', GUN_GRIP)
                .define('A', GUN_ACTUATOR)
                .define('B', GUN_BARREL_SECTION)
                .define('P', PHIAL_MOUNT)
                .pattern("PP ")
                .pattern("ABB")
                .pattern("G  ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.PISTON, Items.REDSTONE, Items.IRON_INGOT}).build()))
                .save(pConsumer);
    }
}
