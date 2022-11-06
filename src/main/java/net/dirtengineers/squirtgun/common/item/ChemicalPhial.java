package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.client.TextUtility;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChemicalPhial extends BasePhial {
    private GenericSlug slugItem = null;

    public ChemicalPhial(Chemical pChemical, CAPACITY_UPGRADE pCapacityUpgrade) {
        super(ItemRegistration.ITEM_PROPERTIES_WITH_TAB);
        chemical = pChemical;
        capacityUpgrade = pCapacityUpgrade;
        applyUpgrades();
    }

    public GenericSlug getOrCreateGenericSlugItem() {
        if (slugItem == null)
            slugItem = (GenericSlug) ItemRegistration.SQUIRTSLUGITEM.get();
        return slugItem;
    }

    public SquirtSlug makeSlugToFire(Level pLevel, LivingEntity pEntityLiving) {
        // TODO: Use optional rather than anonymous reference object
        var ref = new Object() {
            SquirtSlug slug = null;
        };
        optionalFluid.ifPresent(fluid -> {
            if (hasAmmunition((Player) pEntityLiving)) {
                ref.slug = new SquirtSlug(pEntityLiving, pLevel, fluid, chemical);
                consumeAmmunition((Player) pEntityLiving);
            }
        });
        return ref.slug;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(
                pStack,
                pLevel,
                TextUtility.setAmmoHoverText(
                        optionalFluid,
                        getAmmoStatus(),
                        this,
                        pTooltipComponents),
                pIsAdvanced);
    }
}
