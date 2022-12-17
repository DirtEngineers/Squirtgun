package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ChemicalPhial extends BasePhial {

    public ChemicalPhial(Chemical pChemical, CAPACITY_UPGRADE pCapacityUpgrade) {
        super(ItemRegistration.ITEM_PROPERTIES_WITH_TAB);
        chemical = pChemical;
        capacityUpgrade = pCapacityUpgrade;
        applyUpgrades();
        shotsAvailable = maxShots;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        addEffectsToTooltip(pTooltipComponents);
    }

    public void addEffectsToTooltip(List<Component> pTooltips) {
        addTooltipEffects(chemical.getEffects(), pTooltips);
    }
}
