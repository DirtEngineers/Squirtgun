package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.network.AlchemyPacket;
import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.dirtengineers.squirtgun.client.capabilities.squirtgun.IAmmunitionCapability;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.EmptyPhialItem;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

import static net.dirtengineers.squirtgun.Constants.DROP_ITEM_INDEX;
import static net.dirtengineers.squirtgun.Constants.OFF_HAND_INDEX;

public class GunReloadC2SPacket implements AlchemyPacket {

    ItemStack phialStack;

    public GunReloadC2SPacket(ItemStack pStack) {
        this.phialStack = pStack;
    }

    public GunReloadC2SPacket(FriendlyByteBuf pBuffer) {
        this.phialStack = pBuffer.readItem();
    }

    @Override
    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeItem(this.phialStack);
    }

    @Override
    public void handle(NetworkEvent.Context pContext) {
        Player pPlayer = pContext.getSender();
        if(pPlayer != null && phialStack.getItem() instanceof BasePhial basePhial) {

            IAmmunitionCapability ammunitionHandler = SquirtgunItem.getPlayerGun(pContext.getSender()).getCapability(SquirtgunCapabilities.SQUIRTGUN_AMMO).orElse(null);
            if (basePhial instanceof ChemicalPhial) {
                ammunitionHandler.setChemical(basePhial.getChemical());
            } else {
                ammunitionHandler.setPotionKey(basePhial.getPotionLocation());
            }
            ammunitionHandler.setShotsAvailable(basePhial.getShotsAvailable());

            int placementSlotIndex = setInventorySlotForPlacement(pPlayer);
            ItemStack emptyPhial = new ItemStack(ItemRegistration.PHIAL.get());
            switch (placementSlotIndex) {
                case OFF_HAND_INDEX -> pPlayer.getInventory().offhand.add(emptyPhial);
                case DROP_ITEM_INDEX -> pPlayer.drop(emptyPhial, false);
                default -> pPlayer.getInventory().add(placementSlotIndex, emptyPhial);
            }

            if (phialStack.getCount() > pPlayer.getInventory().items.size()) {
                pPlayer.getInventory().offhand.get(0).shrink(1);
            } else {
                pPlayer.getInventory().items.get(phialStack.getCount()).shrink(1);
            }
        }
    }

    private int setInventorySlotForPlacement(Player pPlayer) {
        int destinationSlot = DROP_ITEM_INDEX;
        //Partial stacks to place in?
        ItemStack stack;
        for (int slotNumber = 0; slotNumber < Objects.requireNonNull(pPlayer).getInventory().items.size(); ++slotNumber) {
            stack = pPlayer.getInventory().items.get(slotNumber);
            if (stack.getCount() < stack.getMaxStackSize() && stack.getItem() instanceof EmptyPhialItem) {
                destinationSlot = slotNumber;
                break;
            }
        }
        //Off hand?
        if (destinationSlot == DROP_ITEM_INDEX) {
            stack = pPlayer.getInventory().offhand.get(0);
            if (stack.getCount() < stack.getMaxStackSize() && stack.getItem() instanceof EmptyPhialItem) {
                destinationSlot = OFF_HAND_INDEX;
            }
        }

        // no appropriate stacks in inventory
        // Find an empty slot
        if (destinationSlot == DROP_ITEM_INDEX) {
            for (int slotNumber = 0; slotNumber < Objects.requireNonNull(pPlayer).getInventory().items.size(); ++slotNumber) {
                if (pPlayer.getInventory().items.get(slotNumber).isEmpty()) {
                    destinationSlot = slotNumber;
                    break;
                }
            }
        }
        return destinationSlot;
    }
}
