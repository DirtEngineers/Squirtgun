package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.world.InteractionResultHolder.success;

public class SquirtCartridge extends Item {
    private final int baseCapacity = 10;
    private int fluidCapacity;
    private int fluidLevel;
    private final String fluidLevelDataKey = Squirtgun.MOD_ID + ".fluid_cartridge_level";
    private CompoundTag nbtData = new CompoundTag();
    private FluidTank container = new FluidTank(baseCapacity);

    public SquirtCartridge(Properties pProperties) {
        super(pProperties);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    TANK UTILITIES

    private void setFluidCapacity(int capacity){
        this.container.setCapacity(capacity);
    }

    public int getFluidCapacity(){
        return this.container.getCapacity();
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
        container.drain(container.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
        container.fill(pFluidStack, IFluidHandler.FluidAction.EXECUTE);
    }

}



