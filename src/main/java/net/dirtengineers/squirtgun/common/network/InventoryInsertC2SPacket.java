package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.network.AlchemyPacket;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class InventoryInsertC2SPacket implements AlchemyPacket {
    int inventorySlot;
    ItemStack insertStack;

    public InventoryInsertC2SPacket(int pSlot, ItemStack pStack) {
        this.inventorySlot = pSlot;
        this.insertStack = pStack;
    }

    public InventoryInsertC2SPacket(FriendlyByteBuf pBuffer) {
        this.inventorySlot = pBuffer.readInt();
        this.insertStack = pBuffer.readItem();
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.inventorySlot);
        pBuffer.writeItem(this.insertStack);
    }

    @Override
    public void handle(NetworkEvent.Context pContext) {
        Player player = pContext.getSender();
        Objects.requireNonNull(player);
        if(inventorySlot == Constants.OFF_HAND_INDEX){
            player.getInventory().offhand.add(insertStack);
        } else if(inventorySlot == Constants.DROP_ITEM_INDEX){
            player.drop(insertStack, false);
        } else {
            player.getInventory().add(inventorySlot, insertStack);
        }
    }

//    public static void handle(InventoryInsertC2SPacket pPacket, Supplier<NetworkEvent.Context> pContext) {
//        pContext.get().enqueueWork(() -> {
//            Player player = pContext.get().getSender();
//            Objects.requireNonNull(player);
//            if(pPacket.inventorySlot == Constants.OFF_HAND_INDEX){
//                player.getInventory().offhand.add(pPacket.insertStack);
//            } else if(pPacket.inventorySlot == Constants.DROP_ITEM_INDEX){
//                player.drop(pPacket.insertStack, false);
//            } else {
//                player.getInventory().add(pPacket.inventorySlot, pPacket.insertStack);
//            }
//        });
//        pContext.get().setPacketHandled(true);
//    }
}
