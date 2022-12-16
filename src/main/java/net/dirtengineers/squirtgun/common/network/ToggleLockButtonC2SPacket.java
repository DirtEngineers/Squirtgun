package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractProcessingBlockEntity;
import com.smashingmods.alchemylib.api.network.AlchemyPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class ToggleLockButtonC2SPacket implements AlchemyPacket {
    private final BlockPos blockPos;
    private final boolean locked;

    public ToggleLockButtonC2SPacket(BlockPos pBlockPos, boolean pLock) {
        this.blockPos = pBlockPos;
        this.locked = pLock;
    }

    public ToggleLockButtonC2SPacket(FriendlyByteBuf pBuffer) {
        this.blockPos = pBuffer.readBlockPos();
        this.locked = pBuffer.readBoolean();
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeBlockPos(blockPos);
        pBuffer.writeBoolean(locked);
    }

    @Override
    public void handle(NetworkEvent.Context pContext) {
        Player player = pContext.getSender();
        if (player != null) {
            AbstractProcessingBlockEntity blockEntity = (AbstractProcessingBlockEntity)player.level.getBlockEntity(this.blockPos);
            if (blockEntity != null) {
                blockEntity.setRecipeLocked(this.locked);
                blockEntity.setChanged();
            }
        }
    }

//    public static void handle(ToggleLockButtonC2SPacket pPacket, Supplier<NetworkEvent.Context> pContext) {
//        pContext.get().enqueueWork(() -> {
//            Player player = pContext.get().getSender();
//            if (player != null) {
//                AbstractProcessingBlockEntity blockEntity = (AbstractProcessingBlockEntity) player.level.getBlockEntity(pPacket.blockPos);
//                if (blockEntity != null) {
//                    blockEntity.setRecipeLocked(pPacket.locked);
//                    blockEntity.setChanged();
//                }
//            }
//        });
//        pContext.get().setPacketHandled(true);
//    }
}
