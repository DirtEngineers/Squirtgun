package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.network.AbstractPacketHandler;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler extends AbstractPacketHandler {
    private final SimpleChannel simpleChannel;

    public PacketHandler() {
        this.simpleChannel = createChannel(new ResourceLocation(Squirtgun.MOD_ID, "main"), "1.0.0");
    }

    @Override
    public PacketHandler register() {
        registerMessage(GunCapsUpdateC2SPacket.class, GunCapsUpdateC2SPacket::new);
        registerMessage(InventoryInsertC2SPacket.class, InventoryInsertC2SPacket::new);
        registerMessage(InventoryRemoveC2SPacket.class, InventoryRemoveC2SPacket::new);
        registerMessage(SetRecipeC2SPacket.class, SetRecipeC2SPacket::new);
        registerMessage(ToggleLockButtonC2SPacket.class, ToggleLockButtonC2SPacket::new);
        return this;
    }

    @Override
    protected SimpleChannel getChannel() {
        return simpleChannel;
    }

    //registerMessage(SetRecipePacket.class, SetRecipePacket::new);


//    private static int PACKET_ID = 0;
//    private static final String PROTOCOL_VERSION = "1.0";
//    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
//            new ResourceLocation(Squirtgun.MOD_ID, "main")
//            , () -> PROTOCOL_VERSION
//            , PROTOCOL_VERSION::equals
//            , PROTOCOL_VERSION::equals);
//
//    public PacketHandler() {
//    }
//
//    public static void register() {
//        INSTANCE.messageBuilder(InventoryInsertC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
//                .decoder(InventoryInsertC2SPacket::new)
//                .encoder(InventoryInsertC2SPacket::encode)
//                .consumerNetworkThread(InventoryInsertC2SPacket::handle)
//                .add();
//        INSTANCE.messageBuilder(InventoryRemoveC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
//                .decoder(InventoryRemoveC2SPacket::new)
//                .encoder(InventoryRemoveC2SPacket::encode)
//                .consumerNetworkThread(InventoryRemoveC2SPacket::handle)
//                .add();
//        INSTANCE.messageBuilder(GunCapsUpdateC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
//                .decoder(GunCapsUpdateC2SPacket::new)
//                .encoder(GunCapsUpdateC2SPacket::encode)
//                .consumerNetworkThread(GunCapsUpdateC2SPacket::handle)
//                .add();
//        INSTANCE.messageBuilder(SetRecipeC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
//                .decoder(SetRecipeC2SPacket::new)
//                .encoder(SetRecipeC2SPacket::encode)
//                .consumerNetworkThread(SetRecipeC2SPacket::handle)
//                .add();
//        INSTANCE.messageBuilder(ToggleLockButtonC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
//                .decoder(ToggleLockButtonC2SPacket::new)
//                .encoder(ToggleLockButtonC2SPacket::encode)
//                .consumerNetworkThread(ToggleLockButtonC2SPacket::handle)
//                .add();
//
//
//
//        INSTANCE.messageBuilder(GetReloadPhialsListC2SPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
//                .decoder(GetReloadPhialsListC2SPacket::new)
//                .encoder(GetReloadPhialsListC2SPacket::encode)
//                .consumerNetworkThread((pPacket, pContext) -> {
//                    GetReloadPhialsListC2SPacket.handle(pContext);
//                })
//                .add();
//        INSTANCE.messageBuilder(SendReloadPhialsListS2PPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
//                .decoder(SendReloadPhialsListS2PPacket::new)
//                .encoder(SendReloadPhialsListS2PPacket::encode)
//                .consumerNetworkThread(SendReloadPhialsListS2PPacket::handle)
//                .add();
//    }
//
//    public static <MSG> void sendToServer(MSG message) {
//        INSTANCE.sendToServer(message);
//    }
//
//    public static <MSG> void sendToPlayer(MSG pMessage, ServerPlayer pPlayer) {
//        INSTANCE.send(PacketDistributor.PLAYER.with(() -> pPlayer), pMessage);
//    }
}
