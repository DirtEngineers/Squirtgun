package net.dirtengineers.squirtgun.datagen;

import com.google.gson.JsonObject;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class DatagenUtility {

    public static ResourceLocation getLocation(ItemStack pItemStack, String pType) {
        return new ResourceLocation(Squirtgun.MOD_ID,
                String.format("%s/%s", pType, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pItemStack.getItem())).getPath()));
    }

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

    public static ItemStack toItemStack(String pString) {
        return toItemStack(pString, 1);
    }

    public static ItemStack toItemStack(String pString, int pCount) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(pString)), pCount);
    }
}
