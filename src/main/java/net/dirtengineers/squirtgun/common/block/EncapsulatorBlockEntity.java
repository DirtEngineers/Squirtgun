package net.dirtengineers.squirtgun.common.block;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractFluidBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.FluidBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.SearchableBlockEntity;
import com.smashingmods.alchemylib.api.item.IngredientStack;
import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import com.smashingmods.alchemylib.api.storage.EnergyStorageHandler;
import com.smashingmods.alchemylib.api.storage.FluidStorageHandler;
import com.smashingmods.alchemylib.api.storage.ProcessingSlotHandler;
import com.smashingmods.chemlib.registry.FluidRegistry;
import net.dirtengineers.squirtgun.Config;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.PotionPhial;
import net.dirtengineers.squirtgun.common.network.SetRecipeC2SPacket;
import net.dirtengineers.squirtgun.common.recipe.AbstractPhialRecipe;
import net.dirtengineers.squirtgun.common.recipe.ChemicalPhialRecipe;
import net.dirtengineers.squirtgun.common.recipe.PotionPhialRecipe;
import net.dirtengineers.squirtgun.registry.BlockEntityRegistration;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EncapsulatorBlockEntity extends AbstractFluidBlockEntity implements FluidBlockEntity, SearchableBlockEntity {
    private static final int PHIAL_OUTPUT_SLOT = 0;
    private static final int EMPTY_CONTAINER_OUTPUT_SLOT = 1;
    private final int maxProgress;
    List<AbstractPhialRecipe> recipes;
    private AbstractPhialRecipe currentRecipe;
    private ResourceLocation recipeId;
    private boolean recipeSelectorOpen = false;
    private String searchText = "";
    private boolean recipeLocked = false;

    public EncapsulatorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Squirtgun.MOD_ID, BlockEntityRegistration.FLUID_ENCAPSULATOR_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        this.setEnergyPerTick(Config.Common.encapsulatorEnergyPerTick.get());
        getFluidStorage().setValidator((fluidStack) -> ItemRegistration.CHEMICAL_FLUIDS.containsValue(fluidStack.getFluid()));
        this.maxProgress = Config.Common.encapsulatorTicksPerOperation.get();
    }

    public void onLoad() {
        if (this.level != null && !this.level.isClientSide()) {
            RecipeRegistration.getPhialRecipe((recipe) -> recipe.getId().equals(this.recipeId), this.level).ifPresent(this::setRecipe);
        }
        super.onLoad();
    }

    private void testing() {
//        getEnergyHandler().setEnergy(getEnergyHandler().getMaxEnergyStored());
//        getFluidStorage().setFluid(
//                new FluidStack(
//                        Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("chemlib:nitric_acid_fluid")))
//                        , getFluidStorage().getCapacity()));
    }
    public void tick() {
        super.tick();
    }

    public void updateRecipe() {
        if (this.level != null && !this.level.isClientSide() && !this.getInputHandler().isEmpty() && !this.isRecipeLocked()) {
            RecipeRegistration.getPhialRecipe(this::test, this.level).ifPresent((recipe) -> {
                if (this.currentRecipe == null || !this.currentRecipe.equals(recipe)) {
                    this.setProgress(0);
                    this.setRecipe(recipe.copy());
                }
            });
        }
    }

    private boolean test(AbstractPhialRecipe recipe) {
        List<ItemStack> inputStacks = new LinkedList<>();
        for(ItemStack stack : getInputHandler().getStacks()) {
            inputStacks.add(stack.copy());
        }
        if(recipe instanceof ChemicalPhialRecipe) {
            inputStacks.add(new ItemStack(FluidRegistry.getBuckets().filter(
                    BucketItem -> BucketItem.getFluid().getFluidType()
                            == getFluidStorage().getFluidStack().getFluid().getFluidType()).findFirst().orElse((BucketItem) Items.BUCKET), 1));
        }
        return recipe.matchInputs(inputStacks);
    }

    @Override
    public boolean canProcessRecipe() {
        if (currentRecipe == null || getInputHandler().getStackInSlot(0).isEmpty()) {
            setProgress(0);
            return false;
        } else {
            AbstractPhialRecipe tempRecipe = currentRecipe.copy();
            ItemStack primaryOutput = getOutputHandler().getStackInSlot(PHIAL_OUTPUT_SLOT).copy();
            ItemStack secondaryOutput = getOutputHandler().getStackInSlot(EMPTY_CONTAINER_OUTPUT_SLOT).copy();
            return getEnergyHandler().getEnergyStored() >= getEnergyPerTick()
                    && canUsePrimaryOutput(tempRecipe, primaryOutput)
                    && canUseSecondaryOutput(secondaryOutput)
                    && test(tempRecipe);
        }
    }

    private boolean canUsePrimaryOutput(AbstractPhialRecipe tempRecipe, ItemStack pPrimaryOutput) {
        return tempRecipe.getResultItem().getCount() + pPrimaryOutput.getCount() <= tempRecipe.getResultItem().getMaxStackSize()
                && (ItemStack.isSameItemSameTags(pPrimaryOutput, tempRecipe.getResultItem()) || pPrimaryOutput.isEmpty());
    }

    private boolean canUseSecondaryOutput(ItemStack pSecondaryOutput) {
        ItemStack outStack = ItemStack.EMPTY;
        int bucketInputSlotIndex = getBucketInputSlotIndex();
        if(currentRecipe instanceof ChemicalPhialRecipe) {
            if(bucketInputSlotIndex == -1) {
                return true;
            }
            outStack = new ItemStack(Items.BUCKET);
        }
        if(currentRecipe instanceof PotionPhialRecipe) {
            outStack = new ItemStack(Items.GLASS_BOTTLE);
        }
        return (ItemStack.isSameItemSameTags(pSecondaryOutput, outStack) || pSecondaryOutput.isEmpty());
    }

    @Override
    public void processRecipe() {
        if (this.getProgress() < maxProgress) {
//            if(this.getProgress() == 1) {
//                //TODO: Cancel sound event on demand
//                Objects.requireNonNull(this.getLevel()).playSound(
//                        null
//                        , this.getBlockPos()
//                        , SoundEventRegistration.ENCAPSULATOR_PROCESSING.get()
//                        , SoundSource.BLOCKS
//                        , 0.5F
//                        , 1.0F);
//            }
            incrementProgress();
        } else {
            int phialInputSlotIndex = getPhialInputSlotIndex();
            ItemStack outputStack = currentRecipe.getOutput().get(0).copy();
            if (currentRecipe instanceof ChemicalPhialRecipe) {
                int bucketInputSlotIndex = getBucketInputSlotIndex();
                if (bucketInputSlotIndex == -1) {
                    getFluidStorage().getFluidStack().shrink(((ChemicalPhial) outputStack.getItem()).getFluidCapacityInMb());
                } else {
                    getInputHandler().decrementSlot(bucketInputSlotIndex, 1);
                    getOutputHandler().insertItem(EMPTY_CONTAINER_OUTPUT_SLOT, new ItemStack(Items.BUCKET, 1), false);
                }
                getInputHandler().decrementSlot(phialInputSlotIndex, 1);
                getOutputHandler().insertItem(PHIAL_OUTPUT_SLOT, outputStack, false);
            }
            if (currentRecipe instanceof PotionPhialRecipe) {
                getInputHandler().decrementSlot(getPotionInputSlotIndex(), 1);
                getOutputHandler().insertItem(EMPTY_CONTAINER_OUTPUT_SLOT, new ItemStack(Items.GLASS_BOTTLE, 1), false);
                getInputHandler().decrementSlot(phialInputSlotIndex, 1);
                getOutputHandler().insertItem(PHIAL_OUTPUT_SLOT, outputStack, false);
            }
            setProgress(0);
            this.getEnergyHandler().extractEnergy(Config.Common.encapsulatorEnergyPerTick.get(), false);
            this.setChanged();
        }
    }

    private int getPhialInputSlotIndex() {
        for(int index = 0; index < getInputHandler().getSlots(); index++) {
            if(getInputHandler().getStackInSlot(index).getItem() instanceof BasePhial) {
                return index;
            }
        }
        return -1;
    }

    private int getBucketInputSlotIndex() {
        for(int index = 0; index < getInputHandler().getSlots(); index++) {
            if(getInputHandler().getStackInSlot(index).getItem() instanceof BucketItem) {
                return index;
            }
        }
        return -1;
    }

    private int getPotionInputSlotIndex() {
        for(int index = 0; index < getInputHandler().getSlots(); index++) {
            if(getInputHandler().getStackInSlot(index).hasTag() && Objects.requireNonNull(getInputHandler().getStackInSlot(index).getTag()).contains("Potion")) {
                return index;
            }
        }
        return -1;
    }

    public void setChanged() {
        updateRecipe();
        setCanProcess(canProcessRecipe());
        super.setChanged();
    }

    @Override
    public <R extends AbstractProcessingRecipe> void setRecipe(@Nullable R r) {
        this.currentRecipe = (AbstractPhialRecipe) r;
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
        return new ProcessingSlotHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setProgress(0);
                setChanged();
            }

            @Override
            public boolean isItemValid(int pSlot, @NotNull ItemStack pItemStack) {
                if (currentRecipe != null && isRecipeLocked()) {
                    boolean notContained = this.getStacks().stream().noneMatch(itemStack -> ItemStack.isSameItemSameTags(itemStack, pItemStack));
                    boolean inputRequired = getIngredientStacks(currentRecipe.getInput()).stream()
                            .map(IngredientStack::getIngredient)
                            .anyMatch(ingredient -> ingredient.test(pItemStack));
                    return notContained && inputRequired;
                }
                return super.isItemValid(pSlot, pItemStack);
            }
        };
    }

    @Override
    public ProcessingSlotHandler initializeOutputHandler() {
        return new ProcessingSlotHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return (slot == 0 && (stack.getItem() instanceof ChemicalPhial || stack.getItem() instanceof PotionPhial))
                        || (slot == 1 && stack.getItem() instanceof BucketItem);
            }
        };
    }

    private List<IngredientStack> getIngredientStacks(List<ItemStack> list) {
        List<IngredientStack> outStack = new LinkedList<>();
        for(ItemStack stack : list) {
            outStack.add(new IngredientStack(stack));
        }
        return outStack;
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

    @Override
    public void setRecipeSelectorOpen(boolean pOpen) {
        recipeSelectorOpen = pOpen;
    }

    @Override
    public boolean isRecipeSelectorOpen() {
        return recipeSelectorOpen;
    }

    @Override
    public String getSearchText() {
        return searchText;
    }

    @Override
    public void setSearchText(String pText) {
        searchText = pText;
    }

    @Override
    public boolean isRecipeLocked() {
        return recipeLocked;
    }

    @Override
    public void setRecipeLocked(boolean pRecipeLocked) {
        recipeLocked = pRecipeLocked;
    }

    protected void saveAdditional(CompoundTag pTag) {
        pTag.putString("searchText", searchText);
        pTag.put("input", getInputHandler().serializeNBT());
        pTag.put("output", getOutputHandler().serializeNBT());
        if (currentRecipe != null) {
            pTag.putString("recipeId", currentRecipe.getId().toString());
        }
        super.saveAdditional(pTag);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        getInputHandler().deserializeNBT(pTag.getCompound("input"));
        getOutputHandler().deserializeNBT(pTag.getCompound("output"));
        if(getOutputHandler().getSlots() < 2) {
            getOutputHandler().setSize(2);
        }
        setSearchText(pTag.getString("searchText"));
        recipeId = ResourceLocation.tryParse(pTag.getString("recipeId"));
        if (level != null && level.isClientSide()) {
            RecipeRegistration.getPhialRecipe((recipe) -> recipe.getId().equals(recipeId), level).ifPresent((recipe) -> {
                if (!recipe.equals(currentRecipe)) {
                    setRecipe(recipe);
                    Squirtgun.PACKET_HANDLER.sendToServer(new SetRecipeC2SPacket(getBlockPos(), recipe.getId(), recipe.getGroup()));
                }
            });
        }
    }
}
