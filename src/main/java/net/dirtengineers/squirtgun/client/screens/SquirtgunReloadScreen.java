package net.dirtengineers.squirtgun.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.buttons.PhialReloadScreenButton;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.EmptyPhialItem;
import net.dirtengineers.squirtgun.common.item.Squirtgun.SquirtgunItem;
import net.dirtengineers.squirtgun.common.network.InventoryInsertC2SPacket;
import net.dirtengineers.squirtgun.common.network.InventoryRemoveC2SPacket;
import net.dirtengineers.squirtgun.common.network.SquirtgunPacketHandler;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class SquirtgunReloadScreen extends Screen {
    int buttonHeight = 16;
    int buttonWidth = 16;
    int xShift = (int) Math.floor(buttonWidth * 1.5);
    int yShift = xShift;
    int centerX;
    int centerY;
    int destinationSlot = Constants.DROP_ITEM_INDEX;
    int offhandLocationIndex = -1;
    int inventoryWarningYpos;
    final int buttonColumns = 5;
    boolean showInventoryWarning = false;
    PhialReloadScreenButton gunButton;
    private final List<ItemStack> phials = new ArrayList<>();
    private ItemStack phialSwapStack = ItemStack.EMPTY;
    private SquirtgunItem actualGun;
    private final Player player = Minecraft.getInstance().player;

    public SquirtgunReloadScreen() {
        super(MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)));
    }

    @Override
    protected void init() {
        centerX = width / 2;
        centerY = height / 2;
        actualGun = (SquirtgunItem) SquirtgunItem.getGun(Minecraft.getInstance().player).getItem();
        if(player != null) {
            offhandLocationIndex = player.getInventory().items.size() + 1;
        }
        makeGunButton();
        populatePhialsList();
        UpdateLayout();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        String swapPhialName = MutableComponent.create(new TranslatableContents(phialSwapStack.getItem().getDescriptionId())).getString();
        drawCenteredString(
                pPoseStack
                , Minecraft.getInstance().font
                , MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)).withStyle(ChatFormatting.BOLD)
                , centerX
                , 12
                , 0xFFFFFF);
        drawCenteredString(
                pPoseStack
                , Minecraft.getInstance().font
                , MutableComponent.create(Component.literal(Component.translatable(Constants.reloadScreenCurrentAmmunition).getString() + swapPhialName).getContents())
                        .getString()
                , centerX
                , centerY - (yShift * 3)
                , 0xFFFFFF);
        if (showInventoryWarning) {
            drawCenteredString(
                    pPoseStack
                    , Minecraft.getInstance().font
                    , MutableComponent.create(Component.literal(Component.translatable(Constants.reloadScreenInventoryWarning).getString() + swapPhialName).getContents())
                            .withStyle(ChatFormatting.BOLD).getString()
                    , centerX
                    , inventoryWarningYpos
                    , 0xFFFFFF);
        }

        this.children().forEach(e -> {
            if (e instanceof PhialReloadScreenButton btn) {
                if (btn.isMouseOver(pMouseX, pMouseY)) {
                    renderTooltip(pPoseStack, btn.getTooltip(), pMouseX, pMouseY);
                }
            }
        });
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        //TODO: WHAT IF PLAYER CLOSES WITHOUT SELECTING A PHIAL?
        if (player != null) {
            if (((ChemicalPhial) phialSwapStack.getItem()).getChemical() != actualGun.getChemical()) {
                ItemStack removeStack = phialSwapStack.copy();// phial to remove from inventory
                ItemStack insertStack = actualGun.loadNewPhial(phialSwapStack);
                if(insertStack.getItem() instanceof EmptyPhialItem) {
                    SquirtgunPacketHandler.sendToServer(new InventoryInsertC2SPacket(destinationSlot, insertStack));
                    destinationSlot = removeStack.getCount() == offhandLocationIndex ? Constants.OFF_HAND_INDEX : removeStack.getCount() ;
                    SquirtgunPacketHandler.sendToServer(new InventoryRemoveC2SPacket(destinationSlot, removeStack));
                }
            }
        }
        super.onClose();
    }

    private void UpdateLayout() {
        clearWidgets();
        addRenderableWidget(gunButton);
        makeSwapStackButton();
        fillPhialTable();
    }

    private void populatePhialsList() {
        if (player != null) {
            phials.clear();
            for(int pSlot = 0; pSlot < player.getInventory().items.size(); pSlot++) {
                if(player.getInventory().items.get(pSlot).getItem() instanceof ChemicalPhial) {
                    ItemStack tempStack = player.getInventory().items.get(pSlot).copy();
                    tempStack.setCount(pSlot);
                    addToPhials(tempStack);
                }
            }
            if (player.getInventory().offhand.get(0).getItem() instanceof ChemicalPhial) {
                ItemStack tempStack = player.getInventory().offhand.get(0).copy();
                tempStack.setCount(offhandLocationIndex);
                addToPhials(tempStack);
            }
            Optional<ChemicalPhial> phial = ItemRegistration.PHIALS
                    .entrySet()
                    .stream()
                    .filter(entry -> actualGun.getChemical().equals(entry.getValue()))
                    .findFirst()
                    .map(Map.Entry::getKey);

            phialSwapStack = phial.map(
                            chemicalPhial -> new ItemStack(chemicalPhial, 1))
                    .orElseGet(() -> new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId()), 1));
            removeFromPhials(phialSwapStack);

            if (!phials.isEmpty()) {
                phials.sort(Comparator.comparing(s -> s.getDescriptionId().toLowerCase()));
            }
        }
    }

    private void addToPhials(ItemStack pStack) {
        if (!phialsContainsStack(pStack)) {
            phials.add(pStack);
        }
    }

    private void removeFromPhials(ItemStack pStack){
        ItemStack forRemoval = ItemStack.EMPTY;
        for(ItemStack phialStack : phials) {
            if(phialStack.getItem().getDescriptionId().equals(pStack.getItem().getDescriptionId())) {
                forRemoval = phialStack;
            }
        }
        if(forRemoval != ItemStack.EMPTY) {
            phials.remove(forRemoval);
        }
    }

    private boolean phialsContainsStack(ItemStack pStack) {
        for(ItemStack phialStack : phials) {
            if(phialStack.getItem().getDescriptionId().equals(pStack.getItem().getDescriptionId())) {
                return true;
            }
        }
        return false;
    }

    private void fillPhialTable() {
        int index = 0;
        int xPos = calculateLeftOffset(buttonColumns);
        int yPos = centerY;

        for (ItemStack phial : phials) {
            PhialReloadScreenButton btn = makePhialButton(phial, xPos + (index * xShift), yPos);
            addRenderableWidget(btn);

            // Spaces the buttons
            index++;
            if (index % buttonColumns == 0) {
                index = 0;
                yPos += yShift;
            }
        }
        inventoryWarningYpos = yPos + yShift;
    }

    private PhialReloadScreenButton makePhialButton(ItemStack pPhialStack, int pX, int pY) {
        PhialReloadScreenButton btn = new PhialReloadScreenButton(
                pX
                , pY
                , buttonWidth
                , buttonHeight
                , MutableComponent.create(new TranslatableContents(pPhialStack.getItem().getDescriptionId())).withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                , null
                , pButton -> this.swapPhials((PhialReloadScreenButton)pButton));
        btn.setTargetStack(pPhialStack);
        return btn;
    }

    private void makeSwapStackButton() {
        PhialReloadScreenButton btn = makePhialButton(
                phialSwapStack
                , centerX + (buttonWidth)
                , centerY - (yShift * 2));
        btn.active = false;
        addRenderableWidget(btn);
    }

    private void makeGunButton() {
        if (gunButton == null) {
            gunButton = new PhialReloadScreenButton(
                    centerX - (buttonWidth * 2)
                    , centerY - (yShift * 2)
                    , buttonWidth
                    , buttonHeight
                    , MutableComponent.create(new TranslatableContents(actualGun.getDescriptionId())).withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                    , new ResourceLocation(Squirtgun.MOD_ID, "textures/item/squirtgun.png")
                    , pButton -> this.swapPhials((PhialReloadScreenButton)pButton));
            gunButton.setTargetStack(new ItemStack(actualGun));
            gunButton.active = false;
        }
    }

    private int calculateLeftOffset(int pColumns) {
        int columns = Math.min(phials.size(), pColumns);
        int leftShift = (int) Math.floor((double) columns / 2) * (xShift);
        if (columns % 2 == 1) {
            leftShift += Math.floor((double) xShift / 2);
        }
        return centerX - leftShift;
    }

    private void swapPhials(PhialReloadScreenButton pButton) {
        if (pButton.getTargetStack() != null && player != null) {
            if (pButton.getTargetStack().getItem() instanceof ChemicalPhial) {
                ItemStack tempStack = phialSwapStack.copy();
                phialSwapStack = pButton.getTargetStack().copy();
                removeFromPhials(pButton.getTargetStack());

                if (tempStack.getItem() instanceof ChemicalPhial) {
                    addToPhials(tempStack);
                }
                if (!phials.isEmpty()) {
                    phials.sort(Comparator.comparing(s -> s.getDescriptionId().toLowerCase()));
                }
                setInventorySlotForPlacement();
                showInventoryWarning = destinationSlot == Constants.DROP_ITEM_INDEX;
                UpdateLayout();
            }
        }
    }

    private void setInventorySlotForPlacement() {
       destinationSlot = Constants.DROP_ITEM_INDEX;
        ItemStack stack;
        for (int slotNumber = 0; slotNumber < Objects.requireNonNull(player).getInventory().items.size(); ++slotNumber) {
            stack = player.getInventory().items.get(slotNumber);
            if (stack.getCount() < stack.getMaxStackSize()
                    && stack.getItem() instanceof EmptyPhialItem) {
                destinationSlot = slotNumber;
                break;
            }
        }
        // no appropriate stacks in inventory
        // Any empties NOT in the hotbar?
        if (destinationSlot == Constants.DROP_ITEM_INDEX) {
            for (int slotNumber = 9; slotNumber < player.getInventory().items.size(); ++slotNumber) {
                if (player.getInventory().items.get(slotNumber).isEmpty()) {
                    destinationSlot = slotNumber;
                    break;
                }
            }
        }
        // Hotbar, then?
        if (destinationSlot == Constants.DROP_ITEM_INDEX) {
            for (int slotNumber = 0; slotNumber < 8; ++slotNumber) {
                if (player.getInventory().items.get(slotNumber).isEmpty()) {
                    destinationSlot = slotNumber;
                    break;
                }
            }
        }
        //Off hand perhaps?
        if (destinationSlot == Constants.DROP_ITEM_INDEX) {
            destinationSlot = player.getInventory().offhand.get(0).isEmpty() ? Constants.OFF_HAND_INDEX : Constants.DROP_ITEM_INDEX;
        }
    }
}
