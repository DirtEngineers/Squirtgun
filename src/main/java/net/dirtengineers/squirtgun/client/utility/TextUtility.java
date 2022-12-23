package net.dirtengineers.squirtgun.client.utility;

import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.ChemicalItemType;
import com.smashingmods.chemlib.common.items.ChemicalItem;
import com.smashingmods.chemlib.common.items.CompoundItem;
import com.smashingmods.chemlib.common.items.ElementItem;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.PotionPhial;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TextUtility {

    public static List<Component> getRecipeItemTooltipComponent(ItemStack pItemStack, MutableComponent pComponent) {
        List<Component> components = new ArrayList<>();
        components.add(pComponent.withStyle(Constants.RECIPE_ITEM_REQUIRED_TEXT_STYLE));
        getTooltipComponents(pItemStack, components, true);

        String namespace = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(pItemStack.getItem())).getNamespace();
        components.add(new TextComponent(StringUtils.capitalize(namespace)).withStyle(Constants.MOD_ID_TEXT_STYLE));
        return components;
    }

    public static void getTooltipComponents(ItemStack pItemStack, List<Component> pComponents, boolean showStackCount) {
        Item pItemStackItem = pItemStack.getItem();
        if (pItemStackItem instanceof PotionPhial potionPhial) {
            getPotionPhialComponents(potionPhial, pItemStack, pComponents, showStackCount);
        } else {
            if (pItemStackItem instanceof ChemicalPhial chemicalPhial) {
                getChemicalPhialComponents(chemicalPhial, pItemStack, pComponents, showStackCount);
            }
            else
            {
                if(pItemStackItem instanceof PotionItem) {
                    getPotionComponents(pItemStack, pComponents);
                }
                else {
                    if (pItemStackItem instanceof Chemical chemical) {
                        getChemicalComponents(chemical, pComponents);
                    } else {
                        pComponents.add(new TextComponent(String.format("%dx %s", pItemStack.getCount(), pItemStackItem.getDescription().getString())));
                    }
                }
            }
        }
    }

    private static void getPotionPhialComponents(PotionPhial pPhial, ItemStack pItemStack, List<Component> pComponents, boolean showStackCount) {
        String potionName = new ResourceLocation(Objects.requireNonNull(Objects.requireNonNull(pPhial.getPotionStack().getTag()).get("Potion")).getAsString()).getPath();

        String description = new TextComponent(
                String.format("item.minecraft.potion.effect.%s", StringUtils.removeStart(StringUtils.removeStart(potionName, "strong_"), "long_"))
        ).withStyle(Constants.DISPLAY_ITEM_TEXT_STYLE).getString();

        if (showStackCount) {
            pComponents.add(new TextComponent(String.format("%dx %s", pItemStack.getCount(), description)));
        } else {
            pComponents.add(new TextComponent(String.format("%s", description)));
        }
        pPhial.addEffectsToTooltip(pComponents);
    }

    private static void getChemicalPhialComponents(ChemicalPhial pPhial, ItemStack pItemStack, List<Component> pComponents, boolean showStackCount) {
        if (showStackCount) {
            pComponents.add(new TextComponent(String.format("%dx %s", pItemStack.getCount(), pPhial.getDescription().getString())));
        } else {
            pComponents.add(new TextComponent(String.format("%s", pPhial.getDescription().getString())));
        }
        pPhial.addEffectsToTooltip(pComponents);
    }

    private static void getPotionComponents(ItemStack pItemStack, List<Component> pComponents) {
        String potionName = new ResourceLocation(Objects.requireNonNull(Objects.requireNonNull(pItemStack.getTag()).get("Potion")).getAsString()).getPath();
        String description = new TextComponent(
                    String.format("item.minecraft.potion.effect.%s", StringUtils.removeStart(StringUtils.removeStart(potionName, "strong_"), "long_"))
                ).withStyle(Constants.DISPLAY_ITEM_TEXT_STYLE).getString();

        pComponents.add(new TextComponent(String.format("%dx %s", pItemStack.getCount(), description)));
        PotionUtils.addPotionTooltip(pItemStack, pComponents, 1.0F);
    }

    private static void getChemicalComponents(Chemical pChemical, List<Component> pComponents) {
        String abbreviation = pChemical.getAbbreviation();

        if (pChemical instanceof ElementItem element) {
            pComponents.add(new TextComponent(String.format("%s (%d)", abbreviation, element.getAtomicNumber())).withStyle(ChatFormatting.DARK_AQUA));
            pComponents.add(new TextComponent(element.getGroupName()).withStyle(ChatFormatting.GRAY));
        } else if (pChemical instanceof ChemicalItem chemicalItem && !chemicalItem.getItemType().equals(ChemicalItemType.COMPOUND)) {
            ElementItem element = (ElementItem) chemicalItem.getChemical();
            pComponents.add(new TextComponent(String.format("%s (%d)", chemicalItem.getAbbreviation(), element.getAtomicNumber())).withStyle(ChatFormatting.DARK_AQUA));
            pComponents.add(new TextComponent(element.getGroupName()).withStyle(ChatFormatting.GRAY));
        } else if (pChemical instanceof CompoundItem) {
            pComponents.add(new TextComponent(abbreviation).withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    public static LinkedList<String> padStrings(LinkedList<String> pStrings) {
        LinkedList<String> outList = new LinkedList<>();
        if (!pStrings.isEmpty()) {
            int max = pStrings.stream().map(String::length).max(Integer::compareTo).get();
            for (String pString : pStrings) {
                if (pString.length() < max) {
                    outList.add(" ".repeat(Math.max(0, ((max - pString.length()) / 2))) + pString);
                } else {
                    outList.add(pString);
                }
            }
        }
        return outList;
    }

    public static void drawCenteredStringNoShadow(PoseStack pPoseStack, Font pFont, Component pText, int pX, int pY) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        pFont.draw(
                pPoseStack,
                formattedcharsequence,
                (float) (pX - pFont.width(formattedcharsequence) / 2),
                (float) pY,
                Objects.requireNonNull(pText.getStyle().getColor()).getValue());
    }

    public static Component getFriendlyChemicalName(Chemical pChemical) {
        return new TranslatableComponent(pChemical != null ?
                                String.format("item.%s.%s", pChemical.getClass().getModule().getName(), pChemical.asItem())
                                : Constants.emptyFluidNameKey)
                .withStyle(Constants.HOVER_TEXT_STYLE);
    }

    public static Component getFriendlyPotionName(String pPotionKey) {
        return new TranslatableComponent(!Objects.equals(pPotionKey, "") ?
                                pPotionKey
                                : Constants.emptyFluidNameKey)
                .withStyle(Constants.HOVER_TEXT_STYLE);
    }

    public static String capitalizeText(String text, char delimiter) {
        final char[] buffer = text.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (ch == delimiter) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }
}
