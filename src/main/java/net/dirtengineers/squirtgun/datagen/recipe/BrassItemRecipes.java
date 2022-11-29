package net.dirtengineers.squirtgun.datagen.recipe;

import com.smashingmods.chemlib.registry.ItemRegistry;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

import static net.dirtengineers.squirtgun.registry.ItemRegistration.*;

public class BrassItemRecipes extends net.minecraft.data.recipes.RecipeProvider {
    public static final Item COPPER_DUST = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_METAL_DUSTS, "copper_dust").get();
    public static final Item ZINC_DUST = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_METAL_DUSTS, "zinc_dust").get();
    public static final Item BRASS_BLOCK = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Squirtgun.MOD_ID, Constants.brassBlockName)));

    public BrassItemRecipes(DataGenerator pGenerator) { super(pGenerator); }

   public static void buildRecipes(Consumer<FinishedRecipe> pConsumer) {
       generateBrassBlend(pConsumer);
       generateBrassNugget(pConsumer);
       generateBrassIngot(pConsumer);
       generateBrassBlock(pConsumer);
   }

    public static void generateBrassBlend(Consumer<FinishedRecipe> pConsumer) {
        ShapelessRecipeBuilder
                .shapeless(BRASS_BLEND.get(), 3)
                .group(Squirtgun.MOD_ID)
                .requires(COPPER_DUST, 2)
                .requires(ZINC_DUST)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{COPPER_DUST, ZINC_DUST}).build()))
                .save(pConsumer);
    }

    private static void generateBrassNugget(Consumer<FinishedRecipe> pConsumer) {
        ShapelessRecipeBuilder
                .shapeless(BRASS_NUGGET.get(), 9)
                .group(Squirtgun.MOD_ID)
                .requires(BRASS_INGOT.get())
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET.get()}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_NUGGET.get(), BRASS_INGOT.get()));
    }

    private static void generateBrassIngot(Consumer<FinishedRecipe> pConsumer) {
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(BRASS_BLEND.get())
                        , BRASS_INGOT.get()
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLEND.get()}).build()))
                .save(pConsumer, getBlastingRecipeName(BRASS_INGOT.get()));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(BRASS_BLEND.get())
                        , BRASS_INGOT.get()
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLEND.get()}).build()))
                .save(pConsumer, getSmeltingRecipeName(BRASS_INGOT.get()));

        ShapedRecipeBuilder
                .shaped(BRASS_INGOT.get(), 9)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_BLOCK)
                .pattern("   ")
                .pattern(" X ")
                .pattern("   ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLOCK}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_INGOT.get(), BRASS_BLOCK));

        ShapedRecipeBuilder
                .shaped(BRASS_INGOT.get())
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_NUGGET.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET.get()}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_INGOT.get(), BRASS_NUGGET.get()));
    }

    private static void generateBrassBlock(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(BRASS_BLOCK)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_INGOT.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_INGOT.get()}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_BLOCK, BRASS_INGOT.get()));
    }
}
