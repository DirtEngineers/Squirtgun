package net.dirtengineers.squirtgun.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.client.buttons.CancelButton;
import net.dirtengineers.squirtgun.client.buttons.PhialReloadScreenButton;
import net.dirtengineers.squirtgun.client.capabilities.SquirtgunCapabilities;
import net.dirtengineers.squirtgun.client.capabilities.squirtgun.IAmmunitionCapability;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.EmptyPhialItem;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.dirtengineers.squirtgun.common.network.InventoryInsertC2SPacket;
import net.dirtengineers.squirtgun.common.network.InventoryRemoveC2SPacket;
import net.dirtengineers.squirtgun.common.network.SquirtgunPacketHandler;
import net.dirtengineers.squirtgun.registry.ItemRegistration;
import net.dirtengineers.squirtgun.registry.SoundEventRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class SquirtgunReloadScreen extends Screen {
    int buttonSize = 16;
    int positionShift = (int) Math.floor(buttonSize + 3);
    int centerX;
    final int maxButtonColumns = 9;
    int buttonRows;
    int bgHeight;
    int bgWidth;
    private int bgLeft;
    int bgTop;
    private final int titleOffsetY = 8;
    private final int topRowOffsetY = 30;
    private final int phialTableOffsetY = 70;
    private int phialTableBottom;
    int destinationSlot = Constants.DROP_ITEM_INDEX;
    int offhandLocationIndex = -1;
    CancelButton cancelButton;
    private final LinkedList<ItemStack> phials = new LinkedList<>();
    private ItemStack phialSwapStack = ItemStack.EMPTY;
    private static final Player player = Minecraft.getInstance().player;
    boolean phialSelected;

    public SquirtgunReloadScreen() {
        super(MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)));
    }

    @Override
    protected void init() {
        if (player != null) {
            offhandLocationIndex = player.getInventory().items.size() + 1;
        }
        centerX = width / 2;
        populatePhialsList();
        calculateBGSizeAndPosition();
        UpdateLayout();
        phialSelected = false;
    }

    private void calculateBGSizeAndPosition() {
        bgHeight = phialTableOffsetY;
        int rows = phials.size() / maxButtonColumns;
        if(phials.size() % maxButtonColumns != 0) {
            rows++;
        }
        bgHeight += (positionShift * (rows +1)) + 8;

        bgTop = this.height / 2 - (bgHeight / 2);
        bgWidth = maxButtonColumns * (positionShift + 1) + (8 + 3) * 2;//bgWidth = ((maxButtonColumns + 1) * (positionShift)) + 8;
    }

    private void UpdateLayout() {
        clearWidgets();
        makeSwapStackButton();
        fillPhialTable();
        if(phialSelected) {
            makeCancelbutton();
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        drawCenteredString(
                pPoseStack
                , Minecraft.getInstance().font
                , MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)).setStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT)
                        .withColor(ChatFormatting.WHITE)).withStyle(ChatFormatting.BOLD)
                , centerX
                , bgTop + titleOffsetY
                , 0xFFFFFF);
//        drawCenteredString(
//                pPoseStack
//                , Minecraft.getInstance().font
//                , Component.literal(MutableComponent.create(new TranslatableContents(phialSwapStack.getItem().getDescriptionId())).getString() + " Loaded")
//                        .setStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT)
//                        .withColor(ChatFormatting.WHITE).withItalic(true))
//                , centerX
//                , bgTop + topRowOffsetY + positionShift
//                , 0xFFFFFF);

        this.children().forEach(e -> {
            if (e instanceof PhialReloadScreenButton btn) {
                if (btn.isMouseOver(pMouseX, pMouseY)) {
                    renderTooltip(pPoseStack, btn.getTooltip(), pMouseX, pMouseY);
                }
            }
            if (e instanceof CancelButton btn) {
                if (btn.isMouseOver(pMouseX, pMouseY)) {
                    renderTooltip(pPoseStack, btn.getTooltip(), pMouseX, pMouseY);
                }
            }
        });
    }

    @Override
    public void renderBackground(PoseStack pPoseStack, int pVOffset) {
        super.renderBackground(pPoseStack, pVOffset);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int targetImageWidth = 248;
        int targetImageHeight = 166;
        int cornerOffset = 5;
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/demo_background.png"));
        this.blit(pPoseStack, bgLeft, bgTop, 0, 0, bgWidth, bgHeight);
        this.blit(pPoseStack, centerX + bgWidth / 2 - 5, bgTop, targetImageWidth - cornerOffset, 0, cornerOffset, bgHeight);
        this.blit(pPoseStack, bgLeft, (height / 2) + (bgHeight / 2), 0, targetImageHeight - cornerOffset, bgWidth, cornerOffset);
        this.blit(pPoseStack, centerX + bgWidth / 2 - 5, bgTop + bgHeight - 1, targetImageWidth - cornerOffset, targetImageHeight - cornerOffset, cornerOffset, cornerOffset);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (player != null && phialSelected) {
            ItemStack removeStack = phialSwapStack.copy();// phial to remove from inventory
            ItemStack insertStack = SquirtgunItem.loadNewPhial(player, phialSwapStack);
            if (insertStack.getItem() instanceof EmptyPhialItem) {
                setInventorySlotForPlacement();
                SquirtgunPacketHandler.sendToServer(new InventoryInsertC2SPacket(destinationSlot, insertStack));
                destinationSlot = removeStack.getCount() == offhandLocationIndex ? Constants.OFF_HAND_INDEX : removeStack.getCount();
                SquirtgunPacketHandler.sendToServer(new InventoryRemoveC2SPacket(destinationSlot, removeStack));
                player.playSound(SoundEventRegistration.RELOAD_SCREEN_CLOSE.get());
            }
        }
        super.onClose();
    }

    private void populatePhialsList() {
        if (player != null) {
            phials.clear();
            for (int pSlot = 0; pSlot < player.getInventory().items.size(); pSlot++) {
                if (player.getInventory().items.get(pSlot).getItem() instanceof ChemicalPhial) {
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

            phials.sort(new ItemStackComparator());
        }
    }

    private void fillPhialTable() {
        int buttonsInRow = 0;
        buttonRows = 0;
        int xPos = centerX - calculateLeftOffset(maxButtonColumns);
        int yPos = bgTop + phialTableOffsetY;

        for (ItemStack phial : phials) {
            addRenderableWidget(makePhialButton(
                    phial
                    , false
                    , xPos + (buttonsInRow * (positionShift))
                    , yPos));
            // Spaces the buttons
            buttonsInRow++;
            if (buttonsInRow % maxButtonColumns == 0) {
                buttonsInRow = 0;
                buttonRows++;
                yPos += positionShift + 1;
            }
        }
        bgLeft = xPos - positionShift;
        phialTableBottom = yPos + positionShift;
    }

    private void makeSwapStackButton() {
        PhialReloadScreenButton btn = makePhialButton(
                phialSwapStack
                , true
                , centerX - positionShift / 2
                , bgTop + topRowOffsetY);
        addRenderableWidget(btn);
    }

    private int calculateLeftOffset(int pColumns) {
        int buttonColumns = Math.min(phials.size(), pColumns);
        return (int) (positionShift * (buttonColumns * 0.5));
    }

    private PhialReloadScreenButton makePhialButton(ItemStack pPhialStack, boolean isSwapStack, int pX, int pY) {
        boolean isSameAsSwapStack = ItemStackComparator.getItemPath(pPhialStack).compareToIgnoreCase(ItemStackComparator.getItemPath(phialSwapStack)) == 0;
        PhialReloadScreenButton btn = new PhialReloadScreenButton(
                pX
                , pY
                , buttonSize
                , buttonSize
                , MutableComponent.create(new TranslatableContents(pPhialStack.getItem().getDescriptionId())).withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                , pButton -> this.swapPhials((PhialReloadScreenButton) pButton));
        btn.setTargetStack(pPhialStack);
        btn.active = !isSwapStack;
        btn.setDisplayLoadedMessage(isSameAsSwapStack && !isSwapStack);
        return btn;
    }

    private void makeCancelbutton() {
        cancelButton = new CancelButton(
                centerX - (positionShift / 2) - 2
                , phialTableBottom
                , 18
                , 18
                , MutableComponent.create(new TranslatableContents(Constants.gunGuiCancelButtonMessage)).withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                , b -> super.onClose());
        cancelButton.setActive(phialSelected);
        addRenderableWidget(cancelButton);
    }

    private void swapPhials(PhialReloadScreenButton pButton) {
        if (pButton.getTargetStack() != null && player != null) {
            if (pButton.getTargetStack().getItem() instanceof ChemicalPhial) {
                phialSwapStack = pButton.getTargetStack().copy();
                phialSelected = true;
                UpdateLayout();
                if (player.level.isClientSide) {
                    player.playSound(SoundEventRegistration.PHIAL_SWAP.get());
                }
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

    private void addToPhials(ItemStack pStack) {
        if (phialsGetStackIndex(pStack) < 0) {
            phials.add(pStack);
        }
    }

    private int phialsGetStackIndex(ItemStack pStack) {
        for (ItemStack phial : phials) {
            if (ForgeRegistries.ITEMS.getResourceKey(phial.getItem()).equals(ForgeRegistries.ITEMS.getResourceKey(pStack.getItem()))) {
                return phials.indexOf(phial);
            }
        }
        return -1;
    }

    static class ItemStackComparator implements Comparator<ItemStack> {

        @Override
        public int compare(ItemStack itemStack1, ItemStack itemStack2) {
            return Integer.compare(getItemPath(itemStack1).compareToIgnoreCase(getItemPath(itemStack2)), 0);
        }

        public static String getItemPath(ItemStack pStack) {
            ResourceKey<Item> key = ForgeRegistries.ITEMS.getResourceKey(pStack.getItem()).orElse(null);
            return key != null ? key.location().getPath() : "";
        }
    }
}