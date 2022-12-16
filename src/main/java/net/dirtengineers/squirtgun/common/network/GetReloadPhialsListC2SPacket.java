package net.dirtengineers.squirtgun.common.network;

import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.dirtengineers.squirtgun.client.capabilities.squirtgun.IAmmunitionCapability;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.dirtengineers.squirtgun.util.ReloadScreenHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Supplier;

public class GetReloadPhialsListC2SPacket extends ReloadScreenHelper {

    private static final LinkedList<ItemStack> phials = new LinkedList<>();

    public GetReloadPhialsListC2SPacket() {}

    public GetReloadPhialsListC2SPacket(FriendlyByteBuf pFriendlyByteBuf) {}

    public void encode(FriendlyByteBuf pBuffer) {}

    public static void handle(Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            ServerPlayer player = pContext.get().getSender();
            Objects.requireNonNull(player);

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof ChemicalPhial) {
                    ItemStack tempStack = player.getInventory().getItem(i).copy();
                    tempStack.setCount(i);
                    phials.add(tempStack);
                }
            }

            if (player.getInventory().offhand.get(0).getItem() instanceof ChemicalPhial) {
                ItemStack tempStack = player.getInventory().offhand.get(0).copy();
                tempStack.setCount(ReloadScreenHelper.offhandLocationIndex);
                addToPhials(tempStack);
            }

            //Get from gun
            Optional<BasePhial> phialOptional = Optional.of((BasePhial) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId())));
            IAmmunitionCapability ammunitionHandler = player.getItemInHand(player.getUsedItemHand()).getCapability(SquirtgunCapabilities.SQUIRTGUN_AMMO).orElse(null);
            if (ammunitionHandler.getChemical() != null) {
                phialOptional = ItemRegistration.PHIALS
                        .entrySet()
                        .stream()
                        .filter(entry -> ammunitionHandler.getChemical().equals(entry.getValue()))
                        .findFirst()
                        .map(Map.Entry::getKey);
            }
            phialSwapStack = phialOptional.map(
                            chemicalPhial -> new ItemStack(chemicalPhial, 1))
                    .orElseGet(() -> new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId()), 1));

            phials.sort(new ReloadScreenHelper.ItemStackComparator());
            SquirtgunPacketHandler.sendToPlayer(new SendReloadPhialsListS2PPacket(phials), player);
        });
        pContext.get().setPacketHandled(true);
    }
}
