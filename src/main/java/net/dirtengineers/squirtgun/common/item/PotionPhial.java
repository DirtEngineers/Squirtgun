package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PotionPhial extends BasePhial {
    private final ItemStack potionStack;
    public PotionPhial(String pPotionKey, CAPACITY_UPGRADE pCapacityUpgrade) {
        super(ItemRegistration.ITEM_PROPERTIES_WITH_TAB);
        potionLocation = pPotionKey;
        potionStack = PotionUtils.setPotion(new ItemStack(Items.POTION, 1), Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation(pPotionKey))));
        capacityUpgrade = pCapacityUpgrade;
        applyUpgrades();
        shotsAvailable = maxShots;
        ItemRegistration.POTION_PHIALS.add(this);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        PotionUtils.addPotionTooltip(potionStack, pTooltipComponents, 1.0F);
    }

    @Override
    public void addEffectsToTooltip(List<Component> pTooltips) {
        addTooltipEffects(PotionUtils.getMobEffects(potionStack), pTooltips);
    }

    public ItemStack getPotionStack() {
        return potionStack;
    }
}
