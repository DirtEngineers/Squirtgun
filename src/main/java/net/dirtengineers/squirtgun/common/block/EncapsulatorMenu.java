package net.dirtengineers.squirtgun.common.block;

import com.smashingmods.alchemylib.api.blockentity.container.AbstractProcessingMenu;
import com.smashingmods.alchemylib.api.storage.ProcessingSlotHandler;
import net.dirtengineers.squirtgun.common.registry.BlockRegistration;
import net.dirtengineers.squirtgun.common.registry.MenuRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class EncapsulatorMenu extends AbstractProcessingMenu {
    public EncapsulatorMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pBuffer) {
        this(pContainerId, pInventory, Objects.requireNonNull(pInventory.player.level.getBlockEntity(pBuffer.readBlockPos())));
    }

    public EncapsulatorMenu(int pContainerId, Inventory pInventory, BlockEntity pBlockEntity) {
        super(MenuRegistration.ENCAPSULATOR_MENU.get(), pContainerId, pInventory, pBlockEntity, 1, 1);
        EncapsulatorBlockEntity blockEntity = (EncapsulatorBlockEntity)pBlockEntity;
        ProcessingSlotHandler inputHandler = blockEntity.getInputHandler();
        ProcessingSlotHandler outputHandler = blockEntity.getOutputHandler();
        this.addSlots(SlotItemHandler::new, inputHandler, 1, 1, 0, inputHandler.getSlots(), 84, 31);
        this.addSlots(SlotItemHandler::new, outputHandler, 1, 1, 0, outputHandler.getSlots(), 156, 31);
    }

    public boolean stillValid(Player pPlayer) {
        return stillValid(
                ContainerLevelAccess.create(Objects.requireNonNull(this.getBlockEntity().getLevel()), this.getBlockEntity().getBlockPos())
                , pPlayer,
                BlockRegistration.ENCAPSULATOR.get());
    }
}
