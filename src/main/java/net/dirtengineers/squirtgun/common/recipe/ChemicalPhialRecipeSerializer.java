package net.dirtengineers.squirtgun.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.dirtengineers.squirtgun.datagen.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ChemicalPhialRecipeSerializer<T extends ChemicalPhialRecipe> implements RecipeSerializer<T> {
    private final IFactory<T> factory;

    public ChemicalPhialRecipeSerializer(IFactory<T> pFactory) { this.factory = pFactory; }

    public T fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        String group = pSerializedRecipe.has("group") ? pSerializedRecipe.get("group").getAsString() : "fluid_encapsulator";
        if (!pSerializedRecipe.has("input1")) {
            throw new JsonSyntaxException("Missing input1, expected to find an object.");
        } else {
            ItemStack pPhial = Util.toItemStack(pSerializedRecipe.getAsJsonObject("input1").get("item").getAsString());
            if (!pSerializedRecipe.has("input2")) {
                throw new JsonSyntaxException("Missing input2, expected to find an object.");
            } else {
                JsonObject inputObject = pSerializedRecipe.getAsJsonObject("input2");
                ResourceLocation fluidLocation = new ResourceLocation(inputObject.get("fluid").getAsString());
                int fluidAmount = inputObject.has("amount") ? inputObject.get("amount").getAsInt() : 1000;
                FluidStack pFluid = new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(fluidLocation)), fluidAmount);
                if (!pSerializedRecipe.has("result")) {
                    throw new JsonSyntaxException("Missing result, expected to find an object.");
                } else {
                    ItemStack output = new ItemStack(
                            Objects.requireNonNull(
                                    ForgeRegistries.ITEMS.getValue(
                                            new ResourceLocation(pSerializedRecipe.getAsJsonObject("result").get("item").getAsString())))
                    , 1);
                    return this.factory.create(pRecipeId, group, pPhial, pFluid, output);
                }
            }
        }
    }

    public @Nullable T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        String group = pBuffer.readUtf(32767);
        ItemStack input1 = pBuffer.readItem();
        FluidStack input2 = pBuffer.readFluidStack();
        ItemStack output = pBuffer.readItem();
        return this.factory.create(pRecipeId, group, input1, input2, output);
    }

    public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
        pBuffer.writeUtf(pRecipe.getGroup());
        pBuffer.writeItem(pRecipe.getPhialInput());
        pBuffer.writeFluidStack(pRecipe.getFluidInput());
        pBuffer.writeItem(pRecipe.getResultItem());
    }

    public interface IFactory<T extends Recipe<Inventory>> {
        T create(ResourceLocation location, String group, ItemStack phial, FluidStack fluid, ItemStack output);
    }
}
