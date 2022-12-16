package net.dirtengineers.squirtgun.common.network;

import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class SquirtgunPacketHandler {
    private static int PACKET_ID = 0;
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Squirtgun.MOD_ID, "main")
            , () -> PROTOCOL_VERSION
            , PROTOCOL_VERSION::equals
            , PROTOCOL_VERSION::equals);

    public SquirtgunPacketHandler() {
    }

    public static void register() {
        INSTANCE.messageBuilder(InventoryInsertC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(InventoryInsertC2SPacket::new)
                .encoder(InventoryInsertC2SPacket::encode)
                .consumerNetworkThread(InventoryInsertC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(InventoryRemoveC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(InventoryRemoveC2SPacket::new)
                .encoder(InventoryRemoveC2SPacket::encode)
                .consumerNetworkThread(InventoryRemoveC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(GunCapsUpdateC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(GunCapsUpdateC2SPacket::new)
                .encoder(GunCapsUpdateC2SPacket::encode)
                .consumerNetworkThread(GunCapsUpdateC2SPacket::handle)
                .add();



        INSTANCE.messageBuilder(GetReloadPhialsListC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(GetReloadPhialsListC2SPacket::new)
                .encoder(GetReloadPhialsListC2SPacket::encode)
                .consumerNetworkThread(GetReloadPhialsListC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(SendReloadPhialsListS2PPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SendReloadPhialsListS2PPacket::new)
                .encoder(SendReloadPhialsListS2PPacket::encode)
                .consumerNetworkThread(SendReloadPhialsListS2PPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG pMessage, ServerPlayer pPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> pPlayer), pMessage);
    }
}
