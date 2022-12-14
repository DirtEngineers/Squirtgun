package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.network.AlchemyPacket;
import net.dirtengineers.squirtgun.client.screens.SquirtgunReloadScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class SendReloadPhialsListS2PPacket implements AlchemyPacket {

    private final LinkedList<ItemStack> packetPhials;

    private final ItemStack swapPhialStack;

    public SendReloadPhialsListS2PPacket(LinkedList<ItemStack> pPhials, ItemStack pSwapPhial) {
        this.packetPhials = pPhials;
        this.swapPhialStack = pSwapPhial;
    }

    public SendReloadPhialsListS2PPacket(FriendlyByteBuf pBuffer) {
        int size = pBuffer.readInt();
        this.packetPhials = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            this.packetPhials.add(pBuffer.readItem());
        }
        this.swapPhialStack = pBuffer.readItem();
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(packetPhials.size());
        for (ItemStack phial : packetPhials) {
            pBuffer.writeItemStack(phial, false);
        }
        pBuffer.writeItem(swapPhialStack);
    }

    @Override
    public void handle(NetworkEvent.@NotNull Context pContext) {
        SquirtgunReloadScreen.phials = this.packetPhials;
        SquirtgunReloadScreen.phialSwapStack = this.swapPhialStack;
        SquirtgunReloadScreen.phialsListIsDirty = true;
    }
}
