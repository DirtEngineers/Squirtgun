package net.dirtengineers.squirtgun.common.network;

import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.dirtengineers.squirtgun.client.capabilities.squirtgun.IAmmunitionCapability;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GunCapsUpdateC2SPacket {

    ItemStack insertStack;

    public GunCapsUpdateC2SPacket(ItemStack pPhial) {
        this.insertStack = pPhial;
    }

    public GunCapsUpdateC2SPacket(FriendlyByteBuf pBuffer) {
        this.insertStack = pBuffer.readItem();
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeItem(this.insertStack);
    }

    public static void handle(GunCapsUpdateC2SPacket pPacket, Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            if (pPacket.insertStack.getItem() instanceof ChemicalPhial chemicalPhial) {
                Player player = pContext.get().getSender();
                if(player != null) {
                    IAmmunitionCapability ammunitionHandler = SquirtgunItem.getPlayerGun(player).getCapability(SquirtgunCapabilities.SQUIRTGUN_AMMO, null).orElse(null);
                    ammunitionHandler.setChemical(chemicalPhial.getChemical());
                    ammunitionHandler.setShotsAvailable(chemicalPhial.getShotsAvailable());
                }
            }
        });
        pContext.get().setPacketHandled(true);
    }
}
