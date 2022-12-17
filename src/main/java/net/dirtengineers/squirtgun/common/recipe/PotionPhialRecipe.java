package net.dirtengineers.squirtgun.common.recipe;

import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class PotionPhialRecipe extends AbstractPhialRecipe {
    private final ItemStack inputPhial;
    private final Potion potion;
    private final ItemStack outputPhial;

    public PotionPhialRecipe(ResourceLocation pId, String pGroup, ItemStack pPhial, Potion pPotion, ItemStack pPhialOutput) {
        super(pId, pGroup);
        inputPhial = pPhial;
        potion = pPotion;
        outputPhial = pPhialOutput;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistration.POTION_PHIAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistration.POTION_PHIAL_TYPE.get();
    }

    @Override
    public ItemStack assemble(Inventory pContainer) {
        return outputPhial.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.of(inputPhial.getItem(), PotionUtils.setPotion(new ItemStack(Items.POTION), potion).getItem()));
    }

    @Override
    public String toString() {
        return String.format("input1=%s, input2=%s, %s", inputPhial, potion, outputPhial);
    }

    //TODO: resolve this
    @Override
    public int compareTo(@NotNull AbstractProcessingRecipe abstractProcessingRecipe) {
        return getId().compareNamespaced(abstractProcessingRecipe.getId());
    }

    public PotionPhialRecipe copy() {
        return new PotionPhialRecipe(getId(), getGroup(), inputPhial, potion, outputPhial);
    }

    @Override
    public List<ItemStack> getInput() {
        LinkedList<ItemStack> outList = new LinkedList<>();
        outList.add(inputPhial);
        outList.add(PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
        return outList;
    }

    @Override
    public ItemStack getResultItem() {
        return outputPhial;
    }

    @Override
    public List<ItemStack> getOutput() { return List.of(outputPhial); }

    public ItemStack getPhialInput() {
        return inputPhial;
    }

    public ItemStack getPotionInput() {
        return PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion);
    }

    public ItemStack getOutputPhial() { return outputPhial;}

    @Override
    public boolean matchInputs(List<ItemStack> pStacks) {
        boolean hasPhial = false;
        boolean hasPotion = false;
        for(ItemStack pStack : pStacks) {
            if(pStack.getItem() instanceof BasePhial) {
                hasPhial = true;
            }
            if(PotionUtils.getPotion(pStack) == potion) {
                hasPotion = true;
            }
        }
        return hasPhial && hasPotion;
    }
}
