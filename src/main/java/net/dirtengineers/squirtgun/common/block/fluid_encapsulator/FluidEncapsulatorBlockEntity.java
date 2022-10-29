package net.dirtengineers.squirtgun.common.block.fluid_encapsulator;

import com.smashingmods.alchemistry.Config;
import com.smashingmods.alchemistry.api.blockentity.AbstractFluidBlockEntity;
import com.smashingmods.alchemistry.api.blockentity.handler.CustomEnergyStorage;
import com.smashingmods.alchemistry.api.blockentity.handler.CustomFluidStorage;
import com.smashingmods.alchemistry.api.blockentity.handler.CustomItemStackHandler;
import com.smashingmods.alchemistry.common.recipe.liquifier.LiquifierRecipe;
import com.smashingmods.alchemistry.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FluidEncapsulatorBlockEntity extends AbstractFluidBlockEntity {
    private final int maxProgress;
    private LiquifierRecipe currentRecipe;

    public FluidEncapsulatorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super((BlockEntityType) BlockEntityRegistry.LIQUIFIER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        this.maxProgress = (Integer) Config.Common.liquifierTicksPerOperation.get();
    }

    public void updateRecipe() {
//        if (this.level != null && !this.level.isClientSide()) {
//            RecipeRegistry.getRecipesByType((RecipeType)RecipeRegistry.LIQUIFIER_TYPE.get(), this.level).stream().filter((recipe) -> {
//                return recipe.getInput().matches(this.getItemHandler().getStackInSlot(0));
//            }).findFirst().ifPresent((recipe) -> {
//                if (this.currentRecipe == null || !this.currentRecipe.equals(recipe)) {
//                    this.setProgress(0);
//                    this.currentRecipe = (LiquifierRecipe) recipe;
//                }
//
//            });
//        }

    }

    public boolean canProcessRecipe() {
        ItemStack input = this.getItemHandler().getStackInSlot(0);
        if (this.currentRecipe == null) {
            return false;
        } else {
            return this.getEnergyHandler().getEnergyStored() >= (Integer) Config.Common.liquifierEnergyPerTick.get()
                    && (this.getFluidStorage().getFluidStack().isFluidEqual(this.currentRecipe.getOutput()) || this.getFluidStorage().isEmpty())
                    && this.getFluidStorage().getFluidAmount() <= this.getFluidStorage().getFluidAmount() + this.currentRecipe.getOutput().getAmount()
                    && this.currentRecipe.getInput().matches(input)
                    && input.getCount() >= this.currentRecipe.getInput().getCount();
        }
    }

    public void processRecipe() {
        if (this.getProgress() < this.maxProgress) {
            this.incrementProgress();
        } else {
            this.setProgress(0);
            this.getItemHandler().decrementSlot(0, this.currentRecipe.getInput().getCount());
            this.getFluidStorage().fill(this.currentRecipe.getOutput().copy(), IFluidHandler.FluidAction.EXECUTE);
        }

        this.getEnergyHandler().extractEnergy((Integer) Config.Common.liquifierEnergyPerTick.get(), false);
        this.setChanged();
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public <T extends Recipe<Inventory>> void setRecipe(@Nullable T pRecipe) {
        this.currentRecipe = (LiquifierRecipe)pRecipe;
    }

    public Recipe<Inventory> getRecipe() {
        return this.currentRecipe;
    }

    public CustomEnergyStorage initializeEnergyStorage() {
        return new CustomEnergyStorage((Integer) Config.Common.liquifierEnergyCapacity.get()) {
            protected void onEnergyChanged() {
                super.onEnergyChanged();
                FluidEncapsulatorBlockEntity.this.setChanged();
            }
        };
    }

    public CustomFluidStorage initializeFluidStorage() {
        return new CustomFluidStorage((Integer) Config.Common.liquifierFluidCapacity.get(), FluidStack.EMPTY) {
            protected void onContentsChanged() {
                super.onContentsChanged();
                FluidEncapsulatorBlockEntity.this.setChanged();
            }
        };
    }

    public CustomItemStackHandler initializeItemHandler() {
        return new CustomItemStackHandler(1);
    }

    public boolean onBlockActivated(Level pLevel, BlockPos pBlockPos, Player pPlayer, InteractionHand pHand) {
        return FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pBlockPos, (Direction)null);
    }

    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("maxProgress", this.maxProgress);
        super.saveAdditional(pTag);
    }

    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new FluidEncapsulatorMenu(pContainerId, pInventory, this);
    }
}
