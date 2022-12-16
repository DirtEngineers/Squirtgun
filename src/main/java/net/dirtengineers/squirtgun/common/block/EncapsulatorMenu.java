package net.dirtengineers.squirtgun.common.block;

import com.smashingmods.alchemylib.api.blockentity.container.AbstractProcessingMenu;
import com.smashingmods.alchemylib.api.storage.ProcessingSlotHandler;
import net.dirtengineers.squirtgun.common.recipe.AbstractPhialRecipe;
import net.dirtengineers.squirtgun.registry.BlockRegistration;
import net.dirtengineers.squirtgun.registry.MenuRegistration;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import java.util.LinkedList;
import java.util.Objects;

public class EncapsulatorMenu extends AbstractProcessingMenu {
    private final Level level;
    private final LinkedList<AbstractPhialRecipe> displayedRecipes;
    public EncapsulatorMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pBuffer) {
        this(pContainerId, pInventory, Objects.requireNonNull(pInventory.player.level.getBlockEntity(pBuffer.readBlockPos())));
    }

    public EncapsulatorMenu(int pContainerId, Inventory pInventory, BlockEntity pBlockEntity) {
        super(MenuRegistration.ENCAPSULATOR_MENU.get(), pContainerId, pInventory, pBlockEntity, 2, 2);
        this.displayedRecipes = new LinkedList<>();
        this.level = pInventory.player.getLevel();
        EncapsulatorBlockEntity blockEntity = (EncapsulatorBlockEntity) pBlockEntity;
        ProcessingSlotHandler inputHandler = blockEntity.getInputHandler();
        ProcessingSlotHandler outputHandler = blockEntity.getOutputHandler();
        this.setupRecipeList();
        addSlot(new SlotItemHandler(inputHandler, 0, 84, 18));
        addSlot(new SlotItemHandler(inputHandler, 1, 84, 44));
        addSlot(new SlotItemHandler(outputHandler, 0, 156, 18));
        addSlot(new SlotItemHandler(outputHandler, 1, 156, 44));
    }

    public boolean stillValid(Player pPlayer) {
        return stillValid(
                ContainerLevelAccess.create(Objects.requireNonNull(this.getBlockEntity().getLevel()), this.getBlockEntity().getBlockPos())
                , pPlayer,
                BlockRegistration.ENCAPSULATOR_BLOCK.get());
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        return !this.getBlockEntity().isRecipeLocked() && this.isValidRecipeIndex(pId);
    }

    private boolean isValidRecipeIndex(int pSlot) {
        return pSlot >= 0 && pSlot < this.displayedRecipes.size();
    }

    private void setupRecipeList() {
        if (this.displayedRecipes.isEmpty()) {
            this.resetDisplayedRecipes();
        }
    }

    public void resetDisplayedRecipes() {
        this.displayedRecipes.clear();
        this.displayedRecipes.addAll(RecipeRegistration.getPhialRecipes(this.level));
    }
}
