package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.client.TextUtility;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BaseSquirtMagazine extends Item {
    public enum UPGRADES {
        BASE,
        DOUBLESHOTS,
        TRIPLESHOTS
    }

    private final Chemical chemical;
    private Optional<Fluid> optionalFluid = Optional.empty();
    private final UPGRADES upgrades;
    private int maxShots;
    private int shotsAvailable;
    private GenericSquirtSlug slugItem = null;

    public BaseSquirtMagazine(Chemical pChemical, Properties pProperties, UPGRADES pUpgrades) {
        super(pProperties);
        shotsAvailable = 0;
        chemical = pChemical;
        upgrades = pUpgrades;
        applyUpgrades();
    }

    private void applyUpgrades() {
        int baseMaxShots = 10;
        switch (upgrades) {
            case BASE -> maxShots = baseMaxShots;
            case DOUBLESHOTS -> maxShots = baseMaxShots * 2;
            case TRIPLESHOTS -> maxShots = baseMaxShots * 3;
        }
    }

    public GenericSquirtSlug getOrCreateGenericSlugItem() {
        if (slugItem == null)
            slugItem = (GenericSquirtSlug) ItemRegistration.SQUIRTSLUGITEM.get();
        return slugItem;
    }

    public void initializeFluid() {
        chemical.getFluidTypeReference()
                .flatMap(fluidType -> FluidRegistry.getFluidsAsStream()
                    .filter(fluid -> fluid.getFluidType().equals(chemical.getFluidTypeReference().get()))
                    .findFirst())
                    .ifPresent(fluid -> optionalFluid = Optional.of(fluid));
    }

    public Optional<Fluid> getOptionalFluid() {
        if(optionalFluid.isEmpty()){
            initializeFluid();
        }
        return optionalFluid;
    }

    public FluidStack loadFluid(FluidStack pFluidStack) {
        if (isFluidValid(pFluidStack)) {
            if (pFluidStack.getAmount() >= maxShots * SquirtSlug.shotSize) {
                shotsAvailable = maxShots;
                pFluidStack.shrink(maxShots * SquirtSlug.shotSize);
            } else {
                int stackShots = (int) Math.floor((double) pFluidStack.getAmount() / SquirtSlug.shotSize);
                shotsAvailable += stackShots;
                pFluidStack.shrink(stackShots * SquirtSlug.shotSize);
            }
        }
        return pFluidStack;
    }

    public boolean isFluidValid(FluidStack pFluidStack) {
        if (chemical.getFluidTypeReference().isPresent() &&
                pFluidStack != null &&
                pFluidStack != FluidStack.EMPTY) {
            return pFluidStack.getFluid().getFluidType() == chemical.getFluidTypeReference().get();
        }
        return false;
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

    private void consumeAmmunition(Player pPlayer) {
        if (!pPlayer.getAbilities().instabuild) {
            shotsAvailable--;
        }
    }

    public boolean hasAmmunition(Player pPlayer) {
        return shotsAvailable > 0 || pPlayer.getAbilities().instabuild;
    }

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

    public String getAmmoStatus() {
        return shotsAvailable + "/" + maxShots;
    }

    public int getShotsAvailable(){
        return shotsAvailable;
    }
}
