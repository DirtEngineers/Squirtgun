package net.dirtengineers.squirtgun.common.network;

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

            switch (pPacket.inventorySlot) {
                case -1 -> player.getInventory().offhand.add(pPacket.incomingStack);
                case Integer.MAX_VALUE -> player.drop(pPacket.incomingStack, false);
                default -> player.getInventory().add(pPacket.inventorySlot, pPacket.incomingStack);
            }
        });
        pContext.get().setPacketHandled(true);
    }
}
