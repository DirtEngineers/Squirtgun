package net.dirtengineers.squirtgun.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.client.buttons.ActionButton;
import net.dirtengineers.squirtgun.client.buttons.PhialReloadScreenButton;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.item.EmptyPhialItem;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.dirtengineers.squirtgun.common.network.GetReloadPhialsListC2SPacket;
import net.dirtengineers.squirtgun.common.network.InventoryInsertC2SPacket;
import net.dirtengineers.squirtgun.common.network.InventoryRemoveC2SPacket;
import net.dirtengineers.squirtgun.common.network.SquirtgunPacketHandler;
import net.dirtengineers.squirtgun.registry.SoundEventRegistration;
import net.dirtengineers.squirtgun.util.ReloadScreenHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

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
    private final int topRowOffsetY = 30;
    private final int phialTableOffsetY = topRowOffsetY + buttonSize * 2;
    private int phialTableBottom;
    int destinationSlot = Constants.DROP_ITEM_INDEX;
    ActionButton actionButton;
    private static final Player player = Minecraft.getInstance().player;
    boolean phialSelected;

    public SquirtgunReloadScreen() {
        super(MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)));

    }

    @Override
    protected void init() {
        super.init();

        if (player != null) {
            ReloadScreenHelper.offhandLocationIndex = player.getInventory().items.size() + 1;
        }
        centerX = width / 2;
        // Send GetReloadPhialsListC2SPacket to server to get the updated list of phials
        SquirtgunPacketHandler.sendToServer(new GetReloadPhialsListC2SPacket());
        calculateBGSizeAndPosition();
        UpdateLayout();
        phialSelected = false;
    }

    private void calculateBGSizeAndPosition() {
        bgHeight = phialTableOffsetY;
        int rows = ReloadScreenHelper.phials.size() / maxButtonColumns;
        if (ReloadScreenHelper.phials.size() % maxButtonColumns != 0) {
            rows++;
        }
        bgHeight += (positionShift * (rows + 1)) + 16;
        bgWidth = positionShift * maxButtonColumns + buttonSize + 6;
        bgTop = this.height / 2 - (bgHeight / 2);
        bgLeft = centerX - (int) (positionShift * (maxButtonColumns * 0.5)) - positionShift;
    }

    private void UpdateLayout() {
        clearWidgets();
        makeSwapStackButton();
        fillPhialTable();
        if(phialSelected) {
            makeCancelbutton();
            makeAgreebutton();
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        int titleOffsetY = 8;
        drawCenteredString(
                pPoseStack
                , Minecraft.getInstance().font
                , MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)).setStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT)
                        .withColor(ChatFormatting.WHITE)).withStyle(ChatFormatting.BOLD)
                , centerX
                , bgTop + titleOffsetY
                , 0xFFFFFF);

        this.children().forEach(e -> {
            if (e instanceof PhialReloadScreenButton btn) {
                if (btn.isMouseOver(pMouseX, pMouseY)) {
                    renderTooltip(pPoseStack, btn.getTooltip(), pMouseX, pMouseY);
                }
            }
            if (e instanceof ActionButton btn) {
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
        this.blit(pPoseStack, bgLeft, bgTop, 0, 0, bgWidth + 7, bgHeight);  // main BG
        this.blit(pPoseStack, centerX + bgWidth / 2, bgTop, targetImageWidth - cornerOffset, 0, cornerOffset, bgHeight);    //right edge
        this.blit(pPoseStack, bgLeft, (height / 2) + (bgHeight / 2), 0, targetImageHeight - cornerOffset, bgWidth + 7, cornerOffset);   //bottom edge
        this.blit(pPoseStack, centerX + bgWidth / 2, bgTop + bgHeight - 1, targetImageWidth - cornerOffset, targetImageHeight - cornerOffset, cornerOffset, cornerOffset);    // bottom-right corner
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (player != null && phialSelected) {
            ItemStack removeStack = ReloadScreenHelper.phialSwapStack.copy();// phial to remove from inventory
            ItemStack insertStack = SquirtgunItem.loadNewPhial(player, ReloadScreenHelper.phialSwapStack);
            if (insertStack.getItem() instanceof EmptyPhialItem) {
                setInventorySlotForPlacement();
                SquirtgunPacketHandler.sendToServer(new InventoryInsertC2SPacket(destinationSlot, insertStack));
                destinationSlot = removeStack.getCount() == ReloadScreenHelper.offhandLocationIndex ? Constants.OFF_HAND_INDEX : removeStack.getCount();
                SquirtgunPacketHandler.sendToServer(new InventoryRemoveC2SPacket(destinationSlot, removeStack));//PHIAL_SWAP
                player.playSound(SoundEventRegistration.PHIAL_SWAP.get());
            }
        }
        super.onClose();
    }

    private void fillPhialTable() {
        int buttonsInRow = 0;
        buttonRows = 0;
        int xPos = centerX - calculateLeftOffset();
        int yPos = bgTop + phialTableOffsetY;

        for (ItemStack phial : ReloadScreenHelper.phials) {
            addRenderableWidget(makePhialButton(
                    phial
                    , false
                    , xPos + (buttonsInRow * (positionShift))
                    , yPos));

            // Spaces the buttons
            buttonsInRow++;
            if (buttonsInRow == maxButtonColumns && (buttonRows + 1) * maxButtonColumns != ReloadScreenHelper.phials.size()) {
                buttonsInRow = 0;
                buttonRows++;
                yPos += positionShift + 1;
            }
        }
        phialTableBottom = yPos + positionShift + 8;
    }

    private void makeSwapStackButton() {
        PhialReloadScreenButton btn = makePhialButton(
                ReloadScreenHelper.phialSwapStack
                , true
                , centerX - positionShift / 2
                , bgTop + topRowOffsetY);
        addRenderableWidget(btn);
    }

    private int calculateLeftOffset() {
        int buttonColumns = Math.min(ReloadScreenHelper.phials.size(), maxButtonColumns);
        return (int) (positionShift * (buttonColumns * 0.5));
    }

    private PhialReloadScreenButton makePhialButton(ItemStack pPhialStack, boolean isSwapStack, int pX, int pY) {
        boolean isSameAsSwapStack = ReloadScreenHelper.ItemStackComparator.getItemPath(pPhialStack)
                .compareToIgnoreCase(ReloadScreenHelper.ItemStackComparator.getItemPath(ReloadScreenHelper.phialSwapStack)) == 0;
        PhialReloadScreenButton btn = new PhialReloadScreenButton(
                pX
                , pY
                , buttonSize
                , buttonSize
                , MutableComponent.create(new TranslatableContents(pPhialStack.getItem().getDescriptionId())).withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                , pButton -> this.swapPhials((PhialReloadScreenButton) pButton)
        );
        btn.setTargetStack(pPhialStack);
        btn.active = !isSwapStack;
        btn.setDisplayLoadedMessage(isSameAsSwapStack && !isSwapStack);
        return btn;
    }

    private void makeCancelbutton() {
        actionButton = new ActionButton(
                centerX + 4
                , phialTableBottom
                , 18
                , 18
                , MutableComponent.create(new TranslatableContents(Constants.gunGuiCancelButtonMessage)).withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                , b -> super.onClose()
                , ActionButton.ActionType.CANCEL);
        actionButton.setActive(phialSelected);
        addRenderableWidget(actionButton);
    }

    private void makeAgreebutton() {
        actionButton = new ActionButton(
                centerX - 24
                , phialTableBottom
                , 18
                , 18
                , MutableComponent.create(new TranslatableContents(Constants.gunGuiAgreeButtonMessage)).withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                , b -> this.onClose()
                , ActionButton.ActionType.AGREE);
        actionButton.setActive(phialSelected);
        addRenderableWidget(actionButton);
    }

    private void swapPhials(PhialReloadScreenButton pButton) {
        if (pButton.getTargetStack() != null && player != null) {
            if (pButton.getTargetStack().getItem() instanceof ChemicalPhial) {
                ReloadScreenHelper.phialSwapStack = pButton.getTargetStack().copy();
                phialSelected = true;
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
        // Any empties NOT in the hot bar?
        if (destinationSlot == Constants.DROP_ITEM_INDEX) {
            for (int slotNumber = 9; slotNumber < player.getInventory().items.size(); ++slotNumber) {
                if (player.getInventory().items.get(slotNumber).isEmpty()) {
                    destinationSlot = slotNumber;
                    break;
                }
            }
        }
        // Hot-bar, then?
        if (destinationSlot == Constants.DROP_ITEM_INDEX) {
            for (int slotNumber = 0; slotNumber < 8; ++slotNumber) {
                if (player.getInventory().items.get(slotNumber).isEmpty()) {
                    destinationSlot = slotNumber;
                    break;
                }
            }
        }
        //Offhand perhaps?
        if (destinationSlot == Constants.DROP_ITEM_INDEX) {
            destinationSlot = player.getInventory().offhand.get(0).isEmpty() ? Constants.OFF_HAND_INDEX : Constants.DROP_ITEM_INDEX;
        }
    }




}