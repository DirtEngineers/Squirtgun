package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.common.util.TextUtility;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

public class BaseSquirtMagazine extends Item {
    public enum UPGRADES {
        BASE,
        DOUBLESHOTS,
        TRIPLESHOTS
    }

    private final Chemical chemical;
    private Fluid fluid = null;
    private final UPGRADES upgrades;
    private int maxShots;
    private int shotsAvailable;
    private GenericSquirtSlug slugItem = null;

    public BaseSquirtMagazine(Chemical pChemical, Properties pProperties, UPGRADES pUpgrades) {
        super(pProperties);
        this.shotsAvailable = 0;
        this.chemical = pChemical;
        this.upgrades = pUpgrades;
        this.applyUpgrades();
    }

    private void applyUpgrades() {
        int baseMaxShots = 10;
        switch (this.upgrades) {
            case BASE -> this.maxShots = baseMaxShots;
            case DOUBLESHOTS -> this.maxShots = baseMaxShots * 2;
            case TRIPLESHOTS -> this.maxShots = baseMaxShots * 3;
        }
    }

    public GenericSquirtSlug getGenericSlugItem() {
        if (this.slugItem == null)
            this.slugItem = ItemRegistration.SLUGS.get(this);
        return this.slugItem;
    }

    public void setFluid() {
        this.fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation("minecraft:water"));
        if (this.chemical.getFluidTypeReference().isPresent()) {
            for (Fluid fluid : FluidRegistry.getFluidsAsStream().toList())
                if (fluid.getFluidType() == this.chemical.getFluidTypeReference().get()) {
                    this.fluid = fluid;
                    break;
                }
        }
    }

    public Fluid getFluid() {
        return this.fluid;
    }

    public FluidStack loadFluid(FluidStack pFluidStack) {
        if (this.fluid == null)
            this.setFluid();
        if (isFluidValid(pFluidStack)) {
            if (pFluidStack.getAmount() >= maxShots * SquirtSlug.shotSize) {
                shotsAvailable = maxShots;
                pFluidStack.shrink(maxShots * SquirtSlug.shotSize);
            } else {
                int stackShots = (int) Math.floor((double) pFluidStack.getAmount() / SquirtSlug.shotSize);
                this.shotsAvailable += stackShots;
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
        SquirtSlug slug = null;
        if (this.hasAmmunition((Player) pEntityLiving)) {
            slug = new SquirtSlug(pEntityLiving, pLevel, this.fluid, this.chemical);
            this.consumeAmmunition((Player) pEntityLiving);
        }
        return slug;
    }

    private void consumeAmmunition(Player pPlayer) {
        if (!pPlayer.getAbilities().instabuild) {
            this.shotsAvailable--;
        }
    }

    public boolean hasAmmunition(Player pPlayer) {
        return this.shotsAvailable > 0 || pPlayer.getAbilities().instabuild;
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(
                pStack,
                pLevel,
                TextUtility.setAmmoHoverText(
                        this.fluid,
                        this.getAmmoStatus(),
                        ItemRegistration.getFriendlyItemName(this),
                        pTooltipComponents),
                pIsAdvanced);
    }

    public String getAmmoStatus() {
        return this.shotsAvailable + "/" + this.maxShots;
    }

    public int getShotsAvailable(){
        return this.shotsAvailable;
    }
}
