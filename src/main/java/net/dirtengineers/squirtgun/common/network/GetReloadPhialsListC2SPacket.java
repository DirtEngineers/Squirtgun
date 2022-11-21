package net.dirtengineers.squirtgun.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GetReloadPhialsListC2SPacket {

    public GetReloadPhialsListC2SPacket() {}

    public GetReloadPhialsListC2SPacket(FriendlyByteBuf pBuffer) {}

    public void encode(FriendlyByteBuf pBuffer) {}

    public static void handle(Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            new SendReloadPhialsListS2PPacket().handle(pContext);
        });
        pContext.get().setPacketHandled(true);
    }
}
