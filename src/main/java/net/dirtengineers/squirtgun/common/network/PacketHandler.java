package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.network.AbstractPacketHandler;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

public class PacketHandler extends AbstractPacketHandler {
    private final SimpleChannel simpleChannel;

    public PacketHandler() {
        this.simpleChannel = createChannel(new ResourceLocation(Squirtgun.MOD_ID, "main"), "1.0.0");
    }

    @Override
    public @NotNull PacketHandler register() {
        registerMessage(GunCapsUpdateC2SPacket.class, GunCapsUpdateC2SPacket::new);
        registerMessage(InventoryInsertC2SPacket.class, InventoryInsertC2SPacket::new);
        registerMessage(InventoryRemoveC2SPacket.class, InventoryRemoveC2SPacket::new);
        registerMessage(SetRecipeC2SPacket.class, SetRecipeC2SPacket::new);
        registerMessage(GetReloadPhialsListC2SPacket.class, GetReloadPhialsListC2SPacket::new);
        registerMessage(SendReloadPhialsListS2PPacket.class, SendReloadPhialsListS2PPacket::new);

        return this;
    }

    @Override
    protected @NotNull SimpleChannel getChannel() {
        return simpleChannel;
    }
}
