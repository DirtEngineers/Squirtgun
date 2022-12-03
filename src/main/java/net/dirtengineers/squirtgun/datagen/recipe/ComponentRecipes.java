package net.dirtengineers.squirtgun.datagen.recipe;

import com.smashingmods.chemlib.registry.ItemRegistry;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

import static net.dirtengineers.squirtgun.registry.BlockRegistration.ENCAPSULATOR_BLOCK;
import static net.dirtengineers.squirtgun.registry.ItemRegistration.*;

public class ComponentRecipes extends net.minecraft.data.recipes.RecipeProvider {

    public static final Item GRAPHITE_DUST = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_COMPOUND_DUSTS, "graphite_dust").get();
    public static final Item PVC_PLATE = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_PLATES, "polyvinyl_chloride_plate").get();

    public ComponentRecipes(DataGenerator pGenerator) {
        super(pGenerator);
    }

    public static void buildRecipes(Consumer<FinishedRecipe> pConsumer) {
        generateActuator(pConsumer);
        generateGun(pConsumer);
        generatePhialCap(pConsumer);
        generateEmptyPhial(pConsumer);
        generateEncapsulator(pConsumer);
    }

    private static void generateActuator(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(GUN_ACTUATOR.get())
                .group(Squirtgun.MOD_ID)
                .define('B', BRASS_INGOT.get())
                .define('I', Items.GOLD_INGOT)
                .define('R', Items.REDSTONE)
                .define('G', GRAPHITE_DUST)
                .pattern("B B")
                .pattern("GRI")
                .pattern("B B")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_INGOT.get(), Items.GOLD_INGOT, Items.REDSTONE, GRAPHITE_DUST}).build()))
                .save(pConsumer, getSimpleRecipeName(GUN_ACTUATOR.get()));
    }

    private static void generateGun(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(SQUIRTGUN.get())
                .group(Squirtgun.MOD_ID)
                .define('V', PVC_PLATE)
                .define('A', GUN_ACTUATOR.get())
                .define('B', BRASS_INGOT.get())
                .define('P', PHIAL.get())
                .pattern("VP ")
                .pattern("ABB")
                .pattern("BV ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{PVC_PLATE, GUN_ACTUATOR.get(), BRASS_INGOT.get(), PHIAL.get()}).build()))
                .save(pConsumer);
    }

    private static void generatePhialCap(Consumer<FinishedRecipe> pConsumer) {
        ShapelessRecipeBuilder
                .shapeless(PHIAL_CAP.get(), 3)
                .group(Squirtgun.MOD_ID)
                .requires(BRASS_INGOT.get(), 2)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_INGOT.get()}).build()))
                .save(pConsumer);
    }

    private static void generateEmptyPhial(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(ItemRegistration.PHIAL.get().asItem())
                .group(Squirtgun.MOD_ID)
                .define('C', PHIAL_CAP.get())
                .define('Q', FUSED_QUARTZ_SHARD.get())
                .pattern(" C ")
                .pattern("Q Q")
                .pattern(" Q ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{PHIAL_CAP.get(), FUSED_QUARTZ_SHARD.get()}).build()))
                .save(pConsumer);
    }

    private static void generateEncapsulator(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Squirtgun.MOD_ID, Constants.encapsulatorBlockName))).asItem())
                .group(Squirtgun.MOD_ID)
                .define('U', Items.BUCKET)
                .define('P', Items.PISTON)
                .define('R', Items.REDSTONE)
                .define('C', Items.COPPER_INGOT)
                .define('A', GUN_ACTUATOR.get())
                .define('B', BRASS_INGOT.get())
                .pattern("BRB")
                .pattern("UAP")
                .pattern("BCB")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.BUCKET, Items.PISTON, Items.REDSTONE, Items.COPPER_INGOT, GUN_ACTUATOR.get(), BRASS_INGOT.get()}).build()))
                .save(pConsumer, getSimpleRecipeName(ENCAPSULATOR_BLOCK.get()));
    }
}
