package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.network.AlchemyPacket;
import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.dirtengineers.squirtgun.client.capabilities.squirtgun.IAmmunitionCapability;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class GunCapsUpdateC2SPacket implements AlchemyPacket {

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

    @Override
    public void handle(NetworkEvent.Context pContext) {
        if (insertStack.getItem() instanceof BasePhial basePhial) {
            Player player = pContext.getSender();
            if(player != null) {
                IAmmunitionCapability ammunitionHandler = SquirtgunItem.getPlayerGun(player).getCapability(SquirtgunCapabilities.SQUIRTGUN_AMMO, null).orElse(null);
                if (basePhial.getChemical() != null) {
                    ammunitionHandler.setChemical(basePhial.getChemical());
                } else {
                    ammunitionHandler.setPotionKey(basePhial.getPotionLocation());
                }
                ammunitionHandler.setShotsAvailable(basePhial.getShotsAvailable());
            }
        }
    }
}
