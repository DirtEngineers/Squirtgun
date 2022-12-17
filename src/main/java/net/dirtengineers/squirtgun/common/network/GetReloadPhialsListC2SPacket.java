package net.dirtengineers.squirtgun.common.network;

import com.electronwill.nightconfig.core.AbstractConfig;
import com.smashingmods.alchemylib.api.network.AlchemyPacket;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.dirtengineers.squirtgun.client.capabilities.squirtgun.IAmmunitionCapability;
import net.dirtengineers.squirtgun.client.screens.SquirtgunReloadScreen;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.EmptyPhialItem;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;

import static net.dirtengineers.squirtgun.client.screens.SquirtgunReloadScreen.*;

/**
 * This class represents a packet that is sent from the client to the server to request a list of chemical phials
 * that are available for reloading in the player's inventory.
 *
 * @author X_Niter
 */
public class GetReloadPhialsListC2SPacket implements AlchemyPacket {

    private static final LinkedList<ItemStack> packetPhials = new LinkedList<>();

    ItemStack insertStack;

    /**
     * Constructs a new GetReloadPhialsListC2SPacket instance.
     */
    public GetReloadPhialsListC2SPacket(ItemStack pItemStack) {
        insertStack = pItemStack;
    }

    /**
     * Constructs a new GetReloadPhialsListC2SPacket instance from the given FriendlyByteBuf.
     *
     * @param pFriendlyByteBuf the FriendlyByteBuf to read data from
     */
    public GetReloadPhialsListC2SPacket(FriendlyByteBuf pFriendlyByteBuf) {
        this.insertStack = pFriendlyByteBuf.readItem();
    }

    /**
     * Encodes this packet's data into the given FriendlyByteBuf.
     *
     * @param pBuffer the FriendlyByteBuf to write data to
     */
    public void encode(@NotNull FriendlyByteBuf pBuffer) {
        pBuffer.writeItem(this.insertStack);
    }

    /**
     * Handles this packet's logic.
     *
     * @param pContext the context in which the packet is being handled
     */
    @Override
    public void handle(NetworkEvent.@NotNull Context pContext) {
        IAmmunitionCapability fIAmmunitionCapability = insertStack.getCapability(SquirtgunCapabilities.SQUIRTGUN_AMMO).orElse(null);

        if (!packetPhials.isEmpty()) {
            packetPhials.clear();
        }

        ServerPlayer player = pContext.getSender();
        if (player == null) {
            return;
        }

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof BasePhial) {
                ItemStack tempStack = player.getInventory().getItem(i).copy();
                tempStack.setCount(i);
                addToPhials(tempStack);
            }
        }

        if (player.getInventory().offhand.get(0).getItem() instanceof BasePhial) {
            ItemStack tempStack = player.getInventory().offhand.get(0).copy();
            tempStack.setCount(SquirtgunReloadScreen.offhandLocationIndex);
            addToPhials(tempStack);
        }

        BasePhial basePhial = null;
        if (fIAmmunitionCapability.getChemical() != null) {
            for (BasePhial phial : ItemRegistration.CHEMICAL_PHIALS) {
                if (fIAmmunitionCapability.getChemical().equals(phial.getChemical())) {
                    basePhial = phial;

                }
            }
        }
        if (!Objects.equals(fIAmmunitionCapability.getPotionKey(), "")) {
            for (BasePhial phial : ItemRegistration.POTION_PHIALS) {
                if (fIAmmunitionCapability.getPotionKey().equals(phial.getPotionLocation())) {
                    basePhial = phial;

                }
            }
        }

        phialSwapStack = basePhial != null
                ? new ItemStack(basePhial, 1)
                : new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId()), 1);

        packetPhials.sort(new ItemStackComparator());
        Squirtgun.PACKET_HANDLER.sendToPlayer(new SendReloadPhialsListS2PPacket(packetPhials), player);

    }

    private static void addToPhials(ItemStack pStack) {
        if ((phialsGetStackIndex(pStack) < 0) && !(pStack.getItem() instanceof EmptyPhialItem)) {
            packetPhials.add(pStack);
        }
    }

    private static int phialsGetStackIndex(ItemStack pStack) {
        for (ItemStack phial : packetPhials) {
            if (ForgeRegistries.ITEMS.getResourceKey(phial.getItem()).equals(ForgeRegistries.ITEMS.getResourceKey(pStack.getItem()))) {
                return packetPhials.indexOf(phial);
            }
        }
        return -1;
    }
}
