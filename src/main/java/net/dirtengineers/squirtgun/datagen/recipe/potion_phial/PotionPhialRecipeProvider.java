package net.dirtengineers.squirtgun.datagen.recipe.potion_phial;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.PotionPhial;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

public class PotionPhialRecipeProvider {
    Consumer<FinishedRecipe> consumer;

    public PotionPhialRecipeProvider(Consumer<FinishedRecipe> pConsumer) {
        this.consumer = pConsumer;
    }

    public static void register(Consumer<FinishedRecipe> pConsumer) {
        (new PotionPhialRecipeProvider(pConsumer)).register();
    }

    private void register() {
        for (PotionPhial phial : ItemRegistration.POTION_PHIALS) {
            this.buildPhialRecipe(
                    new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId()), 1)
                    , Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation(phial.getPotionLocation())))
                    , new ItemStack(phial, 1)
                    , new ItemStack(Items.GLASS_BOTTLE));
        }
    }

    private void buildPhialRecipe(ItemStack pPhial, Potion pPotion, ItemStack pPhialOutput, ItemStack pBottleOutput) {
        PotionPhialRecipeBuilder
                .createRecipe(pPhial
                        , pPotion
                        , pPhialOutput
                        , pBottleOutput
                        , new ResourceLocation(Squirtgun.MOD_ID, (Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pPhialOutput.getItem()))).getPath())
                )
                .group(Constants.encapsulatorRecipeGroupName)
                .unlockedBy("has_the_recipe",
                        RecipeUnlockedTrigger
                                .unlocked(new ResourceLocation(Squirtgun.MOD_ID,
                                                String.format("%s%s"
                                                        , Constants.potionPhialCreationRecipeLocationPrefix
                                                        , Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pPhialOutput.getItem())).getPath())
                                        )
                                )
                )
                .save(this.consumer);
    }
}
