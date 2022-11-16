package net.dirtengineers.squirtgun.common.network;

import net.dirtengineers.squirtgun.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class GunReloadInventoryC2SPacket {

    int inventorySlot;
    ItemStack incomingStack;

    public GunReloadInventoryC2SPacket(int pSlot, ItemStack pStack) {
        this.inventorySlot = pSlot;
        this.incomingStack = pStack;
    }

    public GunReloadInventoryC2SPacket(FriendlyByteBuf pBuffer) {
        this.inventorySlot = pBuffer.readInt();
        this.incomingStack = pBuffer.readItem();
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.inventorySlot);
        pBuffer.writeItem(this.incomingStack);
    }

    public static void handle(GunReloadInventoryC2SPacket pPacket, Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            Player player = pContext.get().getSender();
            Objects.requireNonNull(player);
            if(pPacket.inventorySlot == Constants.OFF_HAND_INDEX){
                player.getInventory().offhand.add(pPacket.incomingStack);
            } else if(pPacket.inventorySlot == Constants.DROP_ITEM_INDEX){
                player.drop(pPacket.incomingStack, false);
            } else {
                player.getInventory().add(pPacket.inventorySlot, pPacket.incomingStack);
            }
        });
        pContext.get().setPacketHandled(true);
    }
}
