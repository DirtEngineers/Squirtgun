package net.dirtengineers.squirtgun.client.screens;

import com.smashingmods.alchemistry.common.recipe.combiner.CombinerRecipe;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractProcessingBlockEntity;
import com.smashingmods.alchemylib.api.recipe.ProcessingRecipe;
import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.ChemicalItemType;
import com.smashingmods.chemlib.common.items.ChemicalItem;
import com.smashingmods.chemlib.common.items.CompoundItem;
import com.smashingmods.chemlib.common.items.ElementItem;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.common.block.EncapsulatorBlockEntity;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.PotionPhial;
import net.dirtengineers.squirtgun.common.recipe.AbstractPhialRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class RecipeDisplayUtil {
    public RecipeDisplayUtil() {
    }

    public static List<Component> getItemTooltipComponent(ItemStack pItemStack, MutableComponent pComponent) {
        List<Component> components = new ArrayList<>();
        String namespace = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pItemStack.getItem())).getNamespace();
        components.add(pComponent.withStyle(Constants.RECIPE_ITEM_REQUIRED_TEXT_STYLE));

        Item pItemStackItem = pItemStack.getItem();
        if (pItemStackItem instanceof PotionPhial potionPhial) {
            getPotionPhialComponents(potionPhial, pItemStack, components);
        } else {
            if (pItemStackItem instanceof ChemicalPhial chemicalPhial) {
                getChemicalPhialComponents(chemicalPhial, pItemStack, components);
            }
            else
            {
                if(pItemStackItem instanceof PotionItem) {
                    getPotionComponents(pItemStack, components);
                }
                else {
                    if (pItemStackItem instanceof Chemical chemical) {
                        getChemicalComponents(chemical, components);
                    } else {
                        components.add(MutableComponent.create(new LiteralContents(String.format("%dx %s", pItemStack.getCount(), pItemStackItem.getDescription().getString()))));
                    }
                }
            }
        }
        components.add(MutableComponent.create(new LiteralContents(StringUtils.capitalize(namespace))).withStyle(ChatFormatting.BLUE));
        return components;
    }

    private static void getPotionPhialComponents(PotionPhial pPhial, ItemStack pItemStack, List<Component> pComponents) {
        String potionName = new ResourceLocation(Objects.requireNonNull(Objects.requireNonNull(pPhial.getPotionStack().getTag()).get("Potion")).getAsString()).getPath();
        String description = MutableComponent.create(
                new TranslatableContents(
                        String.format("item.minecraft.potion.effect.%s",
                                StringUtils.removeStart(StringUtils.removeStart(potionName, "strong_"), "long_"))
                )).withStyle(Constants.DISPLAY_ITEM_TEXT_STYLE).getString();
        pComponents.add(MutableComponent.create(new LiteralContents(String.format("%dx %s", pItemStack.getCount(), description))));
        pPhial.addEffectsToTooltip(pComponents);
    }

    private static void getChemicalPhialComponents(ChemicalPhial pPhial, ItemStack pItemStack, List<Component> pComponents) {
        pComponents.add(MutableComponent.create(new LiteralContents(String.format("%dx %s", pItemStack.getCount(), pPhial.getDescription().getString()))));
        pPhial.addEffectsToTooltip(pComponents);
    }

    private static void getPotionComponents(ItemStack pItemStack, List<Component> pComponents) {
        String potionName = new ResourceLocation(Objects.requireNonNull(Objects.requireNonNull(pItemStack.getTag()).get("Potion")).getAsString()).getPath();
        String description = MutableComponent.create(
                new TranslatableContents(
                        String.format("item.minecraft.potion.effect.%s",
                                StringUtils.removeStart(StringUtils.removeStart(potionName, "strong_"), "long_"))
                )).withStyle(Constants.DISPLAY_ITEM_TEXT_STYLE).getString();
        pComponents.add(MutableComponent.create(new LiteralContents(String.format("%dx %s", pItemStack.getCount(), description))));
        PotionUtils.addPotionTooltip(pItemStack, pComponents, 1.0F);
    }

    private static void getChemicalComponents(Chemical pChemical, List<Component> pComponents) {
        String abbreviation = pChemical.getAbbreviation();
        if (pChemical instanceof ElementItem element) {
            pComponents.add(MutableComponent.create(new LiteralContents(String.format("%s (%d)", abbreviation, element.getAtomicNumber()))).withStyle(ChatFormatting.DARK_AQUA));
            pComponents.add(MutableComponent.create(new LiteralContents(element.getGroupName())).withStyle(ChatFormatting.GRAY));
        } else {
            label21:
            {
                if (pChemical instanceof ChemicalItem chemicalItem) {
                    if (!chemicalItem.getItemType().equals(ChemicalItemType.COMPOUND)) {
                        Chemical var9 = chemicalItem.getChemical();
                        if (var9 instanceof ElementItem element) {
                            pComponents.add(MutableComponent.create(new LiteralContents(String.format("%s (%d)", chemicalItem.getAbbreviation(), element.getAtomicNumber()))).withStyle(ChatFormatting.DARK_AQUA));
                            pComponents.add(MutableComponent.create(new LiteralContents(element.getGroupName())).withStyle(ChatFormatting.GRAY));
                        }

                        pComponents.add(MutableComponent.create(new LiteralContents(chemicalItem.getAbbreviation())).withStyle(ChatFormatting.DARK_AQUA));
                        break label21;
                    }
                }
                if (pChemical instanceof CompoundItem) {
                    pComponents.add(MutableComponent.create(new LiteralContents(abbreviation)).withStyle(ChatFormatting.DARK_AQUA));
                }
            }
        }
    }

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