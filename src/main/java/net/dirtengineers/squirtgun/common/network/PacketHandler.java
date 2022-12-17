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
        registerMessage(SetRecipeC2SPacket.class, SetRecipeC2SPacket::new);
        registerMessage(GetReloadPhialsListC2SPacket.class, GetReloadPhialsListC2SPacket::new);
        registerMessage(SendReloadPhialsListS2PPacket.class, SendReloadPhialsListS2PPacket::new);
        registerMessage(GunReloadC2SPacket.class, GunReloadC2SPacket::new);

        return this;
    }

    @Override
    protected @NotNull SimpleChannel getChannel() {
        return simpleChannel;
    }
}
