package net.dirtengineers.squirtgun.client.utility;

import com.smashingmods.alchemistry.common.recipe.combiner.CombinerRecipe;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractProcessingBlockEntity;
import com.smashingmods.alchemylib.api.recipe.ProcessingRecipe;
import net.dirtengineers.squirtgun.common.block.EncapsulatorBlockEntity;
import net.dirtengineers.squirtgun.common.recipe.AbstractPhialRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class RecipeDisplayUtil {
    public RecipeDisplayUtil() {}

    public static Pair<ResourceLocation, String> getSearchablePair(ProcessingRecipe pRecipe) {
        ResourceLocation left;
        String right;
        if (pRecipe instanceof AbstractPhialRecipe phialRecipe) {
            left = ForgeRegistries.ITEMS.getKey(phialRecipe.getResultItem().getItem());
            right = phialRecipe.getResultItem().getDisplayName().getString().toLowerCase();
            return Pair.of(left, right);
        }
        return Pair.of(new ResourceLocation("minecraft:empty"), "");
    }

    public static ItemStack getTarget(ProcessingRecipe pRecipe) {
        if (pRecipe instanceof AbstractPhialRecipe phialRecipe) {
            return phialRecipe.getOutput().get(0);
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getRecipeInputByIndex(ProcessingRecipe pRecipe, int pIndex) {
        AtomicReference<ItemStack> toReturn = new AtomicReference<>(ItemStack.EMPTY);
        if (pRecipe instanceof CombinerRecipe combinerRecipe) {
            if (pIndex >= 0 && pIndex < combinerRecipe.getInput().size()) {
                new Random().ints(0, combinerRecipe.getInput().get(pIndex).toStacks().size())
                        .findFirst()
                        .ifPresent(random -> toReturn.set(combinerRecipe.getInput().get(pIndex).toStacks().get(random)));
            }
        }
        if (pRecipe instanceof AbstractPhialRecipe phialRecipe) {
            if (pIndex >= 0 && pIndex < phialRecipe.getInput().size()) {
                    toReturn.set(phialRecipe.getInput().get(pIndex));
            }
        }
            return toReturn.get();
    }

    public static int getInputSize(AbstractProcessingBlockEntity pBlockEntity) {
        if (pBlockEntity instanceof EncapsulatorBlockEntity) {
            return 2;
        }
        return 0;
    }
}