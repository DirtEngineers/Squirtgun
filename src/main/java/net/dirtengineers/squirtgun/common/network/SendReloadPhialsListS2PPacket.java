package net.dirtengineers.squirtgun.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendReloadPhialsListS2PPacket {

    public SendReloadPhialsListS2PPacket() {}

    public SendReloadPhialsListS2PPacket(FriendlyByteBuf pBuffer) {}

    public void encode(FriendlyByteBuf pBuffer) {
//        pBuffer.writeItemStack(ItemStack.EMPTY, false);
    }

    public void handle(Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
//            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
//                    () -> () -> SquirtgunReloadScreen.updateTest(true));
        });

        pContext.get().setPacketHandled(true);
    }
}
