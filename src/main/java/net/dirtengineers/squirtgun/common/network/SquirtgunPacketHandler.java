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
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Squirtgun.MOD_ID, "main"), () -> {
        return PROTOCOL_VERSION;
    }, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public SquirtgunPacketHandler() {
    }

    public static void register() {
        INSTANCE.messageBuilder(GunReloadInventoryC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(GunReloadInventoryC2SPacket::new)
                .encoder(GunReloadInventoryC2SPacket::encode)
                .consumerNetworkThread(GunReloadInventoryC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG pMessage, ServerPlayer pPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> pPlayer), pMessage);
    }
}
