package net.dirtengineers.squirtgun.common.util;

import com.smashingmods.chemlib.api.Chemical;
import com.smashingmods.chemlib.api.Compound;
import com.smashingmods.chemlib.common.items.CompoundItem;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Objects;
import java.util.function.Predicate;

public class CustomFluidStorage extends FluidTank {

    private CompoundTag tag;
    public static final String FLUID_NBT_KEY = "Fluid";
    protected Item parent;
    public static final int EMPTY_FLUID_COLOR = 0XFFFFFFFF;
    public static String EMPTY_FLUID_NAME = "minecraft:empty";

    public final int DEFAULT_WATER_AMOUNT = 1000;

//    public CustomFluidStorage(int pCapacity, FluidStack pFluidStack) {
//        super(pCapacity);
//        this.fill(pFluidStack, FluidAction.EXECUTE);
//        initializeFromNbt();
//    }

    public CustomFluidStorage(int pCapacity, Predicate<FluidStack> pValidator, Item pParent) {
        super(pCapacity);
        this.setValidator(pValidator);
        parent = pParent;
//        this.readFromNBT(makeBlankFluidtag());
        //initializeFromNbt();
    }

    public FluidStack drainContainer(int maxDrain, FluidAction action) {
        return this.drain(maxDrain, action);
    }

    public FluidType getFluidType() { return this.fluid.getFluid().getFluidType(); }

    public Fluid getRawFluid(){ return this.getFluid().getFluid(); }

    public void setAmount(int pValue) {
        this.fluid.setAmount(pValue);
    }

    public void setFluid(FluidStack pFluidStack) {
        this.drain(this.capacity, FluidAction.EXECUTE);
        this.fill(pFluidStack, FluidAction.EXECUTE);
    }

//    public int getFluidColor() {
//        if (this.isEmptyFluid()) return EMPTY_FLUID_COLOR;
//        return IClientFluidTypeExtensions.of(this.getFluidType()).getTintColor();
//    }

//    public boolean isEmptyFluid() {
//        return Objects.equals(this.getFluidType().toString(), EMPTY_FLUID_NAME);
//    }

//    public String getFriendlyFluidName(){
//        if(this.isEmptyFluid()) return "empty";
//        else {
//            Chemical chemical = Common.Ammunition.get(this.getRawFluid());
//            return chemical instanceof Compound ? I18n.get(((CompoundItem) chemical).getDescriptionId()) : chemical.getChemicalName();
//        }
//    }
//
//    @Override
//    protected void onContentsChanged(){
//        this.writeToNBT(new CompoundTag());
//    }

// this.fluidStorage.readFromNBT(pTag.getCompound("fluid"));
//    {
//        energy:0,
//        fluid:{Amount:0,FluidName:"minecraft:empty"},
//        id:"alchemistry:liquifier_block_entity",
//        item:{Items:[],Size:1},
//        keepPacked:0b,
//        locked:0b,
//        maxProgress:100,
//        paused:0b,
//        progress:0,
//        x:-56,
//        y:-60,
//        z:127
//    }
//
//    private CompoundTag makeBlankFluidtag(){
//        CompoundTag tag = new CompoundTag();
//        tag.putString("FluidName", "_");
//        tag.putInt("Amount", 0);
//        CompoundTag fluid = new CompoundTag();
//        fluid.put("fluid", tag);
//        return fluid;
//    }
//
//    private void initializeFromNbt() {
//        CompoundTag tag = new CompoundTag();
//        tag.putString("Fluidname", "");
//        tag.putInt("Amount", 0);
//        CompoundTag fluid = new CompoundTag();
//        fluid.put("fluid", tag);
////        CompoundTag nbt = this.fluid.getTag();
////        if (nbt == null || (!nbt.contains(FLUID_NBT_KEY) && nbt.getString("id").endsWith(".custom_fluid_storage"))) {
////            nbt = new CompoundTag();
////            this.setFluid(new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("minecraft:water"))), DEFAULT_WATER_AMOUNT));
////            tag = createTag(nbt, true);
////        } else {
////            this.parent = nbt.getString("parent");
////            CompoundTag fluidTag = new CompoundTag();
////            fluidTag.putString("Fluidname", nbt.getString("Fluidname"));
////            fluidTag.putInt("Amount", nbt.getInt("Amount"));
////            this.readFromNBT(fluidTag);
////        }
//    }

//    private void saveToNBT(CompoundTag nbt) {
//        CompoundTag fluidTag = new CompoundTag();
//        fluidTag.putString("Fluidname", tag.getString("Fluidname"));
//        fluidTag.putInt("Amount", tag.getInt("Amount"));
//        fluid.writeToNBT(fluidTag);
//        createTag(tag, false);
//    }
//
//    private CompoundTag createTag(CompoundTag nbt, boolean useDefault){
//        nbt.putString("FLUID_NBT_KEY", this.getRawFluid().getFluidType().toString());
//        nbt.putString("Fluidname", this.getRawFluid().getFluidType().toString());
//        nbt.putInt("Amount", useDefault? DEFAULT_WATER_AMOUNT : this.fluid.getAmount());
//        nbt.putString("id", Squirtgun.MOD_ID + ".custom_fluid_storage");
//        return nbt;
//    }
//
//    public FluidStack getFluidStack() {
//        return this.fluid;
//    }
}
