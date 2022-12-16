package net.dirtengineers.squirtgun.common.network;

import net.dirtengineers.squirtgun.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class InventoryRemoveC2SPacket {
    int inventorySlot;
    ItemStack removeStack;


    public InventoryRemoveC2SPacket(int pSlot, ItemStack pStack) {
        this.inventorySlot = pSlot;
        this.removeStack = pStack;
    }

    public InventoryRemoveC2SPacket(FriendlyByteBuf pBuffer) {
        this.inventorySlot = pBuffer.readInt();
        this.removeStack = pBuffer.readItem();
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.inventorySlot);
        pBuffer.writeItem(this.removeStack);
    }

    public static void handle(InventoryRemoveC2SPacket pPacket, Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            Player player = pContext.get().getSender();
            Objects.requireNonNull(player);
            if(pPacket.inventorySlot == Constants.OFF_HAND_INDEX){
                player.getInventory().offhand.get(0).setCount(0);
//                player.getInventory().offhand.remove(0);
            } else {
                player.getInventory().removeItem(pPacket.inventorySlot, 1);
            }
        });
        pContext.get().setPacketHandled(true);
    }
}
