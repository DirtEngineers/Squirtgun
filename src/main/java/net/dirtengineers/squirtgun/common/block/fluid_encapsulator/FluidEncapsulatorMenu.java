package net.dirtengineers.squirtgun.common.block.fluid_encapsulator;

import com.smashingmods.alchemistry.api.blockentity.handler.CustomItemStackHandler;
import com.smashingmods.alchemistry.api.container.AbstractAlchemistryMenu;
import com.smashingmods.alchemistry.common.block.liquifier.LiquifierBlockEntity;
import com.smashingmods.alchemistry.registry.BlockRegistry;
import com.smashingmods.alchemistry.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class FluidEncapsulatorMenu extends AbstractAlchemistryMenu {
    public FluidEncapsulatorMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pBuffer) {
        this(pContainerId, pInventory, Objects.requireNonNull(pInventory.player.level.getBlockEntity(pBuffer.readBlockPos())));
    }

    protected FluidEncapsulatorMenu(int pContainerId, Inventory pInventory, BlockEntity pBlockEntity) {
        super(MenuRegistry.LIQUIFIER_MENU.get(), pContainerId, pInventory, pBlockEntity, 1, 0);
        LiquifierBlockEntity blockEntity = (LiquifierBlockEntity)pBlockEntity;
        CustomItemStackHandler inputHandler = blockEntity.getItemHandler();
        this.addSlots(SlotItemHandler::new, inputHandler, 1, 1, 0, inputHandler.getSlots(), 62, 35);
    }

    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(Objects.requireNonNull(this.getBlockEntity().getLevel()), this.getBlockEntity().getBlockPos()), pPlayer, (Block) BlockRegistry.LIQUIFIER.get());
    }
}
