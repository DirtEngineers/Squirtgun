package net.dirtengineers.squirtgun.common.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public abstract class AbstractSquirtgunRecipe  implements Recipe<Inventory> {
    private final ResourceLocation recipeId;
    private final String group;

    public AbstractSquirtgunRecipe(ResourceLocation pRecipeId, String pGroup) {
        this.recipeId = pRecipeId;
        this.group = pGroup;
    }

    public ResourceLocation getId() {
        return this.recipeId;
    }

    public String getGroup() {
        return this.group;
    }

    public boolean matches(Inventory pContainer, Level pLevel) {
        return false;
    }

    public ItemStack assemble(Inventory pContainer) {
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }
}
