package net.dirtengineers.squirtgun.common.network;

import net.dirtengineers.squirtgun.util.ReloadScreenHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.LinkedList;
import java.util.function.Supplier;

public class SendReloadPhialsListS2PPacket extends ReloadScreenHelper {

    private final LinkedList<ItemStack> packetPhials;

    public SendReloadPhialsListS2PPacket(LinkedList<ItemStack> phials) {
        this.packetPhials = phials;
    }

    public SendReloadPhialsListS2PPacket(FriendlyByteBuf pBuffer) {
        int size = pBuffer.readInt();
        this.packetPhials = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            this.packetPhials.add(pBuffer.readItem());
        }
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(packetPhials.size());
        for (ItemStack phial : packetPhials) {
            pBuffer.writeItemStack(phial, false);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            // Update the list of phials for SquirtgunReloadScreen
            ReloadScreenHelper.phials = this.packetPhials;
        });

        pContext.get().setPacketHandled(true);
    }
}
