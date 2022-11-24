package net.dirtengineers.squirtgun.datagen;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.datagen.recipe.providers.ChemicalPhialRecipeProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

public class RecipeGenerator extends net.minecraft.data.recipes.RecipeProvider {

    Item COPPER_DUST = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("chemlib", "copper_dust"))).asItem();
    Item ZINC_DUST = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("chemlib", "zinc_dust"))).asItem();
    Item BRASS_BLEND = ItemRegistration.BRASS_BLEND_ITEM.get().asItem();
    Item BRASS_NUGGET = ItemRegistration.BRASS_NUGGET_ITEM.get().asItem();
    Item BRASS_INGOT = ItemRegistration.BRASS_INGOT_ITEM.get().asItem();
    Item BRASS_BLOCK = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Squirtgun.MOD_ID, "brass_block"))).asItem();
    Item QUARTZ_SHARD = ItemRegistration.QUARTZ_SHARD_ITEM.get().asItem();
    Item SILICA = ForgeRegistries.ITEMS.getValue(new ResourceLocation("chemlib", "silicon_dioxide"));
    Item PHIAL_CAP = ItemRegistration.PHIAL_CAP_ITEM.get().asItem();

    public RecipeGenerator(DataGenerator pGenerator) {
        super(pGenerator);
    }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pConsumer) {
        this.generateGun(pConsumer);
        this.generateBrassBlend(pConsumer);
        this.generateBrassIngot(pConsumer);
        this.generateBrassNugget(pConsumer);
        this.generateBrassBlock(pConsumer);
        this.generateQuartzShard(pConsumer);
        this.generatePhialCap(pConsumer);
        this.generateEmptyPhial(pConsumer);
        ChemicalPhialRecipeProvider.register(pConsumer);

        //TODO: Brass block from brass ingot
    }

    private void generateBrassBlend(Consumer<FinishedRecipe> pConsumer) {
        ShapelessRecipeBuilder
                .shapeless(BRASS_BLEND, 3)
                .group(Squirtgun.MOD_ID)
                .requires(COPPER_DUST, 2)
                .requires(ZINC_DUST)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{COPPER_DUST, ZINC_DUST}).build()))
                .save(pConsumer);
    }

    private void generateBrassNugget(Consumer<FinishedRecipe> pConsumer) {
        //TODO: nuggets from ingots
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(BRASS_BLEND)
                        , BRASS_NUGGET
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLEND}).build()))
                .save(pConsumer, getBlastingRecipeName(BRASS_NUGGET));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(BRASS_BLEND)
                        , BRASS_NUGGET
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLEND}).build()))
                .save(pConsumer, getSmeltingRecipeName(BRASS_NUGGET));

        ShapelessRecipeBuilder
                .shapeless(BRASS_NUGGET, 9)
                .group(Squirtgun.MOD_ID)
                .requires(BRASS_INGOT)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_NUGGET, BRASS_INGOT));

    }

    private void generateBrassIngot(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(BRASS_INGOT, 9)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_BLOCK)
                .pattern("   ")
                .pattern(" X ")
                .pattern("   ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_BLOCK}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_INGOT, BRASS_BLOCK));

        ShapedRecipeBuilder
                .shaped(BRASS_INGOT)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_NUGGET)
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_INGOT, BRASS_NUGGET));
    }

    private void generateBrassBlock(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(BRASS_BLOCK)
                .group(Squirtgun.MOD_ID)
                .define('X', BRASS_INGOT)
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_INGOT}).build()))
                .save(pConsumer, getConversionRecipeName(BRASS_BLOCK, BRASS_INGOT));
    }

    private void generateQuartzShard(Consumer<FinishedRecipe> pConsumer) {
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(SILICA)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{SILICA}).build()))
                .save(pConsumer, String.format("%s_from_blasting", getConversionRecipeName(QUARTZ_SHARD, SILICA)));

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(Items.QUARTZ)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 100)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.QUARTZ}).build()))
                .save(pConsumer, String.format("%s_from_blasting", getConversionRecipeName(QUARTZ_SHARD, Items.QUARTZ)));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(SILICA)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{SILICA}).build()))
                .save(pConsumer, String.format("%s_from_smelting", getConversionRecipeName(QUARTZ_SHARD, SILICA)));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(Items.QUARTZ)
                        , QUARTZ_SHARD
                        , 0.5F
                        , 200)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.QUARTZ}).build()))
                .save(pConsumer, String.format("%s_from_smelting", getConversionRecipeName(QUARTZ_SHARD, Items.QUARTZ)));
    }

    private void generatePhialCap(Consumer<FinishedRecipe> pConsumer) {
        ShapelessRecipeBuilder
                .shapeless(PHIAL_CAP)
                .group(Squirtgun.MOD_ID)
                .requires(BRASS_NUGGET, 2)
                .group(Squirtgun.MOD_ID)
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{BRASS_NUGGET}).build()))
                .save(pConsumer);
    }

    private void generateGun(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(ItemRegistration.SQUIRTGUNITEM.get().asItem())
                .group(Squirtgun.MOD_ID)
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('R', Items.REDSTONE)
                .pattern("IRP")
                .pattern("IP ")
                .pattern("I  ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{Items.PISTON, Items.REDSTONE, Items.IRON_INGOT}).build()))
                .save(pConsumer);
    }

    private void generateEmptyPhial(Consumer<FinishedRecipe> pConsumer) {
        ShapedRecipeBuilder
                .shaped(ItemRegistration.PHIAL.get().asItem())
                .group(Squirtgun.MOD_ID)
                .define('C', PHIAL_CAP)
                .define('Q', QUARTZ_SHARD)
                .pattern(" C ")
                .pattern("Q Q")
                .pattern(" Q ")
                .unlockedBy(
                        "has_item",
                        inventoryTrigger(
                                ItemPredicate.Builder.item().of(new ItemLike[]{PHIAL_CAP, QUARTZ_SHARD}).build()))
                .save(pConsumer);
    }
}
