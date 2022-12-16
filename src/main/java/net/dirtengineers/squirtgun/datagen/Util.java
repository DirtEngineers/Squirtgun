package net.dirtengineers.squirtgun.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class Util {

    public static void itemStackToJson(JsonObject pJson, String pKey, ItemStack pItemStack) {
        if (!pItemStack.isEmpty()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pItemStack.getItem())).toString());
            if (pItemStack.getCount() > 1) {
                jsonObject.addProperty("count", pItemStack.getCount());
            }
            pJson.add(pKey, jsonObject);
        }
    }

    public static void fluidStacktoJson(JsonObject pJson, String pKey, FluidStack pFluidStack) {
        if (!pFluidStack.isEmpty()) {
            JsonObject jsonObject = new JsonObject();
            ResourceLocation fluidLocation = ForgeRegistries.FLUIDS.getKey(pFluidStack.getFluid());
            String amount = String.valueOf(pFluidStack.getAmount());
            jsonObject.addProperty("fluid", Objects.requireNonNull(fluidLocation).toString());
            jsonObject.addProperty("amount", amount);
            pJson.add(pKey, jsonObject);
        }
    }

    public static void potionToJson(JsonObject pJson, String pKey, Potion pPotion) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("potion", Objects.requireNonNull(ForgeRegistries.POTIONS.getKey(pPotion)).toString());
        pJson.add(pKey, jsonObject);
    }

    public static Potion toPotion(String pString) {
        return Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation(pString)));
    }

    public static ItemStack toItemStack(String pString) {
        return toItemStack(pString, 1);
    }

    public static ItemStack toItemStack(String pString, int pCount) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(pString)), pCount);
    }
}
