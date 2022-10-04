package net.dirtengineers.squirtgun.common.item;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.Compound;
import com.smashingmods.chemlib.common.items.CompoundItem;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.common.util.Common;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.InteractionResultHolder.success;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class SquirtMagazine extends Item {

    public static final int baseCapacity = 1000;
    private final FluidTank container;

    public SquirtMagazine(){
        super(new Item.Properties().tab(ItemRegistration.SQUIRTGUN_TAB));
        this.container = new FluidTank(baseCapacity, Common.SQUIRT_AMMUNITION_ONLY);
    }

    public SquirtMagazine(int pCapacity) {
        super(new Item.Properties().tab(ItemRegistration.SQUIRTGUN_TAB));
        this.container = new FluidTank(pCapacity, Common.SQUIRT_AMMUNITION_ONLY);
    }

    public SquirtMagazine(SquirtMagazine pMag){
        super(new Item.Properties().tab(ItemRegistration.SQUIRTGUN_TAB));
        this.container = new FluidTank(pMag.getFluidCapacity());
        this.container.fill(pMag.container.getFluid(), EXECUTE);
    }

    public boolean hasAmmunition(Player pPlayer){
        return this.getFluidLevel() >= SquirtSlug.shotSize || pPlayer.getAbilities().instabuild;
    }

    public SquirtSlug makeSlugToFire(Level pLevel, LivingEntity pEntityLiving){
        SquirtSlug slug  = new SquirtSlug(pEntityLiving, pLevel, this.container.getFluid().getFluid());
        return this.fillSlug(pEntityLiving, slug);
    }

     public SquirtSlug fillSlug(LivingEntity pEntityLiving, SquirtSlug pSlug) {
         if (pEntityLiving instanceof Player player) {
             if (hasAmmunition(player)) {
                 pSlug.setAmmoType(this.container.getFluid().getFluid());
                 if (!player.getAbilities().instabuild)
                     container.drain(SquirtSlug.shotSize, EXECUTE);
             }
         }
         return pSlug;
     }

    public FluidStack emptyMagazine(){
        return this.container.drain(this.container.getCapacity(), EXECUTE);
    }

    public int getMaxShots(){
        return (int) Math.floor((float)this.getFluidCapacity()/SquirtSlug.shotSize);
    }

    public int getShotsAvailable(){
        return (int) Math.floor((float)this.getFluidLevel()/SquirtSlug.shotSize);
    }

    public String getFriendlyFluidName(){
        Chemical chemical = Common.Ammunition.get(this.container.getFluid().getFluid());
        return chemical instanceof Compound ? I18n.get(((CompoundItem) chemical).getDescriptionId()) : chemical.getChemicalName();
    }

    private String getFriendlyName(){
        return I18n.get(this.getDescriptionId());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, Common.setAmmoHoverText(this, getFriendlyName(), pTooltipComponents), pIsAdvanced);
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    TANK UTILITIES

    public FluidType getFluidType(){
        return this.container.getFluid().getFluid().getFluidType();
    }

    private void setFluidCapacity(int capacity){
        this.container.setCapacity(capacity);
    }

    public int getFluidCapacity(){
        return this.container.getCapacity();
    }

    public FluidStack drainContainer(int maxDrain, FluidAction action){
        return this.container.drain(maxDrain, EXECUTE);

    }

    public int getFluidLevel(){
        return this.container.getFluidAmount();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        if (!(container.getFluid() == FluidStack.EMPTY)) {
//            container.getFluid().grow(1);
//            nbtData = container.writeToNBT(nbtData);
//
//        }
//        else{
//            Fluid blah = ForgeRegistries.FLUIDS.getValue(Common.AmmunitionFluids.get(1));
//        ForgeRegistries.FLUIDS.getEntries().stream().filter(key ->{return key.getKey().location().getNamespace() == "chemlib" &&
//                key.getKey().location().getPath().contains("ethanol_fluid");}).toList();
        //(itemStack) -> Common.AmmunitionFluids.contains(itemStack.getItem().getRegistryName())
        //setFluid(new FluidStack());
//        }chemlib
        return success(new ItemStack((Item) null));
    }

    public void setFluid(FluidStack pFluidStack) {
        container.drain(container.getCapacity(), EXECUTE);
        container.fill(pFluidStack, EXECUTE);
    }

    public void setFluid(Chemical chemical, int amount) {
        container.drain(container.getCapacity(), EXECUTE);
        Optional<FluidType> fluidType = chemical.getFluidTypeReference();
//        if (fluidType.isPresent()) {
//            Fluid type = fluidType.get();
//            container.fill(new FluidStack(fluidType.get(), amount), EXECUTE);
//        }
    }

    public static class Builder{
        int capacity;
        public Builder capacity(int pCapacity){
            capacity = pCapacity;
            return this;
        }

        public SquirtMagazine build(){
            return new SquirtMagazine(capacity);
        }
    }
}



