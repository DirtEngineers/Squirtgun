package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.world.InteractionResultHolder.success;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class SquirtMagazine extends Item {

    public static final int baseCapacity = 1000;
    private final FluidTank container;

    public SquirtMagazine(){
        super(new Item.Properties().tab(ItemRegistration.SQUIRTGUN_TAB));
        this.container = new FluidTank(baseCapacity);
    }

    public SquirtMagazine(int pCapacity) {
        super(new Item.Properties().tab(ItemRegistration.SQUIRTGUN_TAB));
        this.container = new FluidTank(pCapacity);
    }

    public SquirtMagazine(SquirtMagazine pMag){
        super(new Item.Properties().tab(ItemRegistration.SQUIRTGUN_TAB));
        this.container = new FluidTank(pMag.getFluidCapacity());
//        this.setFluid(pMag.container.drain(pMag.container.getFluidAmount(), FluidAction.EXECUTE));
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

    public void loadFromMagazine(SquirtMagazine pMag) {
        this.container.drain(this.container.getCapacity(), EXECUTE);
        this.container.setCapacity(pMag.getFluidCapacity());
        this.setFluid(pMag.container.drain(pMag.container.getFluidAmount(), EXECUTE));
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    TANK UTILITIES

    private void setFluidCapacity(int capacity){
        this.container.setCapacity(capacity);
    }

    public int getFluidCapacity(){
        return this.container.getCapacity();
    }

    public FluidStack drainContainer(FluidAction pAction){
        return this.container.drain(FluidStack.EMPTY, pAction);
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
        ForgeRegistries.FLUIDS.getEntries().stream().filter(key ->{return key.getKey().location().getNamespace() == "chemlib" &&
                key.getKey().location().getPath().contains("ethanol_fluid");}).toList();
        //(itemStack) -> Common.AmmunitionFluids.contains(itemStack.getItem().getRegistryName())
        //setFluid(new FluidStack());
//        }chemlib
        return success(new ItemStack((Item) null));
    }

    public void setFluid(FluidStack pFluidStack) {
        container.drain(container.getCapacity(), EXECUTE);
        container.fill(pFluidStack, EXECUTE);
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



