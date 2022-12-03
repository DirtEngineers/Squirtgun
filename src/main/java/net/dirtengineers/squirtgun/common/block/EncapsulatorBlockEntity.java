package net.dirtengineers.squirtgun.common.block;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractFluidBlockEntity;
import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import com.smashingmods.alchemylib.api.storage.EnergyStorageHandler;
import com.smashingmods.alchemylib.api.storage.FluidStorageHandler;
import com.smashingmods.alchemylib.api.storage.ProcessingSlotHandler;
import net.dirtengineers.squirtgun.Config;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.EmptyPhialItem;
import net.dirtengineers.squirtgun.common.recipe.PhialRecipe;
import net.dirtengineers.squirtgun.registry.BlockEntityRegistration;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.dirtengineers.squirtgun.registry.SoundEventRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EncapsulatorBlockEntity extends AbstractFluidBlockEntity {
    private static final int FILLED_PHIAL_OUTPUT_SLOT = 0;
    private final int maxProgress;
    List<AbstractProcessingRecipe> recipes;
    private AbstractProcessingRecipe currentRecipe;

    public EncapsulatorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Squirtgun.MOD_ID, BlockEntityRegistration.FLUID_ENCAPSULATOR_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        getFluidStorage().setValidator((fluidStack) -> ItemRegistration.CHEMICAL_FLUIDS.containsValue(fluidStack.getFluid()));
        this.maxProgress = Config.Common.encapsulatorTicksPerOperation.get();
    }

    private void testing() {
//        getEnergyHandler().setEnergy(getEnergyHandler().getMaxEnergyStored());
//        getFluidStorage().setFluid(new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("chemlib:nitric_acid_fluid"))), getFluidStorage().getCapacity()));
    }

    @Override
    public void updateRecipe() {
        if (this.level != null && !this.level.isClientSide()) {
            for (Object recipe : RecipeRegistration.getRecipesByType(RecipeRegistration.PHIAL_CREATION_RECIPE_TYPE.get(), this.level)) {
                if (recipe != null && ((PhialRecipe) Objects.requireNonNull(recipe)).getFluidInput().isFluidEqual(this.getFluidStorage().getFluid())) {
                    if (this.currentRecipe == null || !this.currentRecipe.equals(recipe)) {
                        this.setProgress(0);
                        this.currentRecipe = (PhialRecipe) recipe;
                    }
                }
            }
        }
    }

    @Override
    public boolean canProcessRecipe() {
        if (currentRecipe == null || getInputHandler().getStackInSlot(0).isEmpty()) {
            this.setProgress(0);
            //TODO: Cancel sound event
            return false;
        } else {
            if (currentRecipe instanceof PhialRecipe) {
                return  getEnergyHandler().getEnergyStored() >= Config.Common.encapsulatorEnergyPerTick.get()
                        && getInputHandler().getStackInSlot(0).getItem() instanceof BasePhial phial
                        && getFluidStorage().getFluidStack().getFluid() == ((PhialRecipe) currentRecipe).getFluidInput().getFluid()
                        && getFluidStorage().getFluidStack().getAmount() >= phial.getFluidCapacityInMb()
                        && (phial instanceof EmptyPhialItem ?
                        getFluidStorage().getFluidStack().getAmount() >= ((PhialRecipe) currentRecipe).getFluidInput().getAmount()
                        : getFluidStorage().getFluidStack().getAmount() >= ((BasePhial) getInputHandler().getStackInSlot(0).getItem()).getFluidCapacityInMb())
                        && getOutputHandler().isItemValid(FILLED_PHIAL_OUTPUT_SLOT, new ItemStack(((PhialRecipe) currentRecipe).getOutput().getItem()));
            } else {
                return false;
            }
        }
    }

    @Override
    public void processRecipe() {
        if (this.getProgress() < maxProgress) {
            if(this.getProgress() == 1) {
                Objects.requireNonNull(this.getLevel()).playSound(
                        null
                        , this.getBlockPos()
                        , SoundEventRegistration.ENCAPSULATOR_PROCESSING.get()
                        , SoundSource.BLOCKS
                        , 0.5F
                        , 1.0F);
            }
            incrementProgress();
        } else {
            if (currentRecipe instanceof PhialRecipe) {
                setProgress(0);
                ItemStack outputStack = ((PhialRecipe) currentRecipe).getOutput().copy();
                getFluidStorage().getFluidStack().shrink(((ChemicalPhial) outputStack.getItem()).getFluidCapacityInMb());
                getInputHandler().decrementSlot(0, 1);
                getOutputHandler().insertItem(FILLED_PHIAL_OUTPUT_SLOT, outputStack, false);
            }
        }
        this.getEnergyHandler().extractEnergy(Config.Common.encapsulatorEnergyPerTick.get(), false);
        this.setChanged();
    }

    public void setChanged() {
        updateRecipe();
        super.setChanged();
    }

    @Override
    public <R extends AbstractProcessingRecipe> void setRecipe(@Nullable R r) {
        this.currentRecipe = r;
    }

    @Override
    public <R extends AbstractProcessingRecipe> LinkedList<R> getAllRecipes() {
        return (LinkedList<R>) this.recipes;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    @Override
    public AbstractProcessingRecipe getRecipe() {
        return currentRecipe;
    }

    ///////////////////////////////////////
    // ENERGY
    @Override
    public EnergyStorageHandler initializeEnergyStorage() {
        return new EnergyStorageHandler(Config.Common.encapsulatorEnergyCapacity.get()) {
            protected void onEnergyChanged() {
                EncapsulatorBlockEntity.this.setChanged();
            }
        };
    }

    ///////////////////////////////////////
    // ITEMS
    @Override
    public ProcessingSlotHandler initializeInputHandler() {
        return new ProcessingSlotHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                EncapsulatorBlockEntity.this.setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack pItemStack) {
                return pItemStack.getItem() instanceof BasePhial;
            }
        };
    }

    @Override
    public ProcessingSlotHandler initializeOutputHandler() {
        return new ProcessingSlotHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                EncapsulatorBlockEntity.this.setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack pItemStack) {
                return pItemStack.getItem().toString().equals(this.getStackInSlot(0).getItem().toString())
                        || this.getStackInSlot(0).isEmpty();
            }
        };
    }

    ///////////////////////////////////////
    // FLUIDS
    @Override
    public FluidStorageHandler initializeFluidStorage() {
        return new FluidStorageHandler(Config.Common.encapsulatorFluidCapacity.get(), FluidStack.EMPTY) {
            @Override
            protected void onContentsChanged() {
                EncapsulatorBlockEntity.this.setChanged();
            }
        };
    }

    public boolean onBlockActivated(Level pLevel, BlockPos pBlockPos, Player pPlayer, InteractionHand pHand) {
        testing();
        return super.onBlockActivated(pLevel, pBlockPos, pPlayer, pHand);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new EncapsulatorMenu(pContainerId, pInventory, this);
    }
}
