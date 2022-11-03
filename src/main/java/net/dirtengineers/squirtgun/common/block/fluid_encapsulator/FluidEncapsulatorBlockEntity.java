package net.dirtengineers.squirtgun.common.block.fluid_encapsulator;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractInventoryBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.FluidBlockEntity;
import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import com.smashingmods.alchemylib.api.storage.EnergyStorageHandler;
import com.smashingmods.alchemylib.api.storage.FluidStorageHandler;
import com.smashingmods.alchemylib.api.storage.ProcessingSlotHandler;
import net.dirtengineers.squirtgun.Config;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BasePhialItem;
import net.dirtengineers.squirtgun.common.item.EmptyPhialItem;
import net.dirtengineers.squirtgun.common.recipe.fluid_encapsulator.creation.PhialTypeCreationRecipe;
import net.dirtengineers.squirtgun.common.registry.BlockEntityRegistration;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

//TODO: liquifierTicksPerOperation
public class FluidEncapsulatorBlockEntity extends AbstractInventoryBlockEntity implements FluidBlockEntity {
    private static final int FILLED_PHIAL_OUTPUT_SLOT = 0;
    private final int maxProgress;
    List<AbstractProcessingRecipe> recipes;
    private AbstractProcessingRecipe currentRecipe;
    private final FluidStorageHandler fluidStorage = this.initializeFluidStorage();
    private final LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.of(() -> this.fluidStorage);

    public FluidEncapsulatorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Squirtgun.MOD_ID, BlockEntityRegistration.FLUID_ENCAPSULATOR_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        fluidStorage.setValidator((fluidStack) -> ItemRegistration.CHEMICAL_FLUIDS.containsValue(fluidStack.getFluid()));
        this.maxProgress = Config.Common.encapsulatorTicksPerOperation.get();
    }

    //TODO: FusionControllerBlockEntity is a possibility
    //TODO: Update recipe and canprocess recipe when contents or recipe change
    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void updateRecipe() {
//        if (this.level != null && !this.level.isClientSide()) {
//            RecipeRegistration.getRecipesByType(
//                            (RecipeType) RecipeRegistration.FLUID_ENCAPSULATOR_RECIPE_TYPES.get()
//                            , this.level)
//                    .stream()
//                    .filter((recipe) -> {
//                        return recipe.getInput().matches(this.getItemHandler().getStackInSlot(0));
//                    })
//                    .findFirst().ifPresent(
//                            (recipe) -> {
//                                if (this.currentRecipe == null || !this.currentRecipe.equals(recipe)) {
//                                    this.setProgress(0);
//                                    this.currentRecipe = (LiquifierRecipe) recipe;
//                                }
//
//                            });
//        }
    }

    //TODO: Stubbed out for later recipes
    @Override
    public boolean canProcessRecipe() {
        if (this.currentRecipe == null ||this.getEnergyHandler().getEnergyStored() >= Config.Common.encapsulatorEnergyPerTick.get()) {
            return false;
        } else {
            if (this.currentRecipe instanceof PhialTypeCreationRecipe) {
                return this.getInputHandler().getStackInSlot(0).getItem() instanceof EmptyPhialItem
                        && this.getInputHandler().getStackInSlot(0) != ItemStack.EMPTY
                        && this.fluidStorage.getFluidStack().getFluid() == ((PhialTypeCreationRecipe) this.currentRecipe).getFluidInput().getFluid()
                        && this.fluidStorage.getFluidStack().getAmount() >= ((PhialTypeCreationRecipe) this.currentRecipe).getFluidInput().getAmount();
            } else {
                return false;
            }
        }
    }

    //TODO: Stubbed out for other recipes
    @Override
    public void processRecipe() {
        if (this.getProgress() < this.maxProgress) {
            this.incrementProgress();
        } else {
            if (this.currentRecipe instanceof PhialTypeCreationRecipe) {
                createNewPhial();
            }
        }
        this.getEnergyHandler().extractEnergy(Config.Common.encapsulatorEnergyPerTick.get(), false);
        this.setChanged();
    }

    private void createNewPhial(){
        int fluidAmount = ((PhialTypeCreationRecipe) this.currentRecipe).getFluidInput().getAmount();
        this.setProgress(0);
        this.fluidStorage.setAmount(this.fluidStorage.getFluidAmount() - fluidAmount);
        this.getInputHandler().decrementSlot(0, 1);

        ItemStack outputStack = ((PhialTypeCreationRecipe) this.currentRecipe).getOutput().copy();
        outputStack.setCount(1);
        ((BasePhialItem) outputStack.getItem()).loadFluid(((PhialTypeCreationRecipe) this.currentRecipe).getFluidInput().copy());
        this.getOutputHandler().insertItem(FILLED_PHIAL_OUTPUT_SLOT, outputStack, false);
    }

    //TODO: OTHER STUFF?
    public void setChanged(){
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
        return this.currentRecipe;
    }

//    @Override
//    public boolean onBlockActivated(Level pLevel, BlockPos pBlockPos, Player pPlayer, InteractionHand pHand) {
//        return FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pBlockPos, null);
//    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        //TODO: check this
        pTag.putInt("maxProgress", this.maxProgress);
        super.saveAdditional(pTag);
    }

    //TODO: WRITE STUFF!
    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new FluidEncapsulatorMenu(pContainerId, pInventory, this);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> pCapability, @Nullable Direction pDirection) {
        return pCapability == ForgeCapabilities.FLUID_HANDLER ? this.lazyFluidHandler.cast() : super.getCapability(pCapability, pDirection);
    }

    @Override
    public void invalidateCaps() {
        this.lazyFluidHandler.invalidate();
        super.invalidateCaps();
    }

    ///////////////////////////////////////
    // ENERGY
    @Override
    public EnergyStorageHandler initializeEnergyStorage() {
        return new EnergyStorageHandler(Config.Common.encapsulatorEnergyCapacity.get()) {
            protected void onEnergyChanged() {
                super.onEnergyChanged();
                FluidEncapsulatorBlockEntity.this.setChanged();
            }
        };
    }

    ///////////////////////////////////////
    // ITEMS
    //TODO: CompactorBlockEntity example
    @Override
    public ProcessingSlotHandler initializeInputHandler() {
        return new ProcessingSlotHandler(1){
            @Override
            protected void onContentsChanged(int slot){
                super.onContentsChanged(slot);
                FluidEncapsulatorBlockEntity.this.setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack pItemStack) {
                return super.isItemValid(slot, pItemStack);
            }
        };
    }

    @Override
    public ProcessingSlotHandler initializeOutputHandler() {
        return new ProcessingSlotHandler(1);
    }
//    public CustomItemStackHandler initializeOutputHandler() {
//        return new CustomItemStackHandler(10) {
//            public boolean isItemValid(int pSlot, ItemStack pItemStack) {
//                return false;
//            }
//
//            protected void onContentsChanged(int slot) {
//                DissolverBlockEntity.this.setChanged();
//            }
//        };
//    }

    ///////////////////////////////////////
    // FLUIDS
    @Override
    public FluidStorageHandler initializeFluidStorage() {
        return new FluidStorageHandler(Config.Common.encapsulatorFluidCapacity.get(), FluidStack.EMPTY) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                FluidEncapsulatorBlockEntity.this.setChanged();
            }
        };
    }

    @Override
    public FluidStorageHandler getFluidStorage() {
        return this.fluidStorage;
    }

    //TODO: THIS
    @Override
    public ProcessingSlotHandler initializeSlotHandler() {
        return null;
    }

    //TODO: THIS
    @Override
    public ProcessingSlotHandler getSlotHandler() {
        return null;
    }
}
