package net.dirtengineers.squirtgun.common.recipe;

import com.google.gson.JsonObject;
import net.dirtengineers.squirtgun.datagen.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class PotionPhialRecipeSerializer<T extends PotionPhialRecipe> implements RecipeSerializer<T> {
    private final PotionPhialRecipeSerializer.IFactory<T> factory;
    public PotionPhialRecipeSerializer(PotionPhialRecipeSerializer.IFactory<T> pFactory) { this.factory = pFactory; }

    public T fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        String group = pSerializedRecipe.get("group").getAsString();
        ItemStack input1 = ShapedRecipe.itemStackFromJson(pSerializedRecipe.getAsJsonObject("input1"));
        Potion input2 =  Util.toPotion(pSerializedRecipe.getAsJsonObject("input2").get("potion").getAsString());
        ItemStack output1 = ShapedRecipe.itemStackFromJson(pSerializedRecipe.getAsJsonObject("output1"));
        return this.factory.create(pRecipeId, group, input1, input2, output1);
    }

    public @Nullable T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        String group = pBuffer.readUtf(32767);
        ItemStack input1 = pBuffer.readItem();
        Potion input2 = PotionUtils.getPotion(pBuffer.readItem());
        ItemStack phialOutput = pBuffer.readItem();
        return this.factory.create(pRecipeId, group, input1, input2, phialOutput);
    }

    public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
        pBuffer.writeUtf(pRecipe.getGroup());
        pBuffer.writeItem(pRecipe.getPhialInput());
        pBuffer.writeItem(pRecipe.getPotionInput());
        pBuffer.writeItem(pRecipe.getOutputPhial());
    }

    public interface IFactory<T extends Recipe<Inventory>> {
        T create(ResourceLocation location, String group, ItemStack phial, Potion potion, ItemStack phialOutput);
    }
}
