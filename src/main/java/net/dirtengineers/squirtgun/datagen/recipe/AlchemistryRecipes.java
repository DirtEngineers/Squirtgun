package net.dirtengineers.squirtgun.datagen.recipe;

import com.smashingmods.alchemistry.datagen.recipe.compactor.CompactorRecipeProvider;
import com.smashingmods.chemlib.api.ChemicalItemType;
import com.smashingmods.chemlib.registry.ItemRegistry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class AlchemistryRecipes extends CompactorRecipeProvider {
    public AlchemistryRecipes(Consumer<FinishedRecipe> pConsumer) {
        super(pConsumer);
    }

    public static void register(Consumer<FinishedRecipe> pConsumer) {
        new AlchemistryRecipes(pConsumer).register();
    }

    protected void register() {

        ItemRegistry.getCompoundByName("polyvinyl_chloride").ifPresent((pvc) -> {
            ItemRegistry.getChemicalItemByNameAndType("polyvinyl_chloride", ChemicalItemType.PLATE).ifPresent((pvc_plate) -> {
                this.compactor(new ItemStack(pvc, 8), new ItemStack(pvc_plate));
            });
        });
    }
}
