package net.dirtengineers.squirtgun.client.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.alchemylib.api.blockentity.container.FakeItemRenderer;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class PhialReloadScreenButton extends Button {
    ItemStack targetStack;
    boolean displayLoadedMessage = false;
    private ResourceLocation texture;

    public PhialReloadScreenButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }
    public PhialReloadScreenButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation alternateTexture, OnPress pOnPress) {
        this(pX, pY,pWidth, pHeight, pMessage, pOnPress);
        this.texture = alternateTexture;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(pPoseStack, this.x - 2, this.y - 2, 0, 46 + i * 20, getWidth() / 2 + 2, 20);
        this.blit(pPoseStack, this.x + this.width / 2, this.y - 2, 200 - getWidth() / 2 - 1, 46 + i * 20, getWidth() / 2 + 2, 20);
        if(this.getTargetStack() != null) {
            FakeItemRenderer.renderFakeItem(this.getTargetStack(), this.x, this.y);
        }
    }

    protected void renderBg(PoseStack pPoseStack, Minecraft pMinecraft, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.getYImage(this.isHoveredOrFocused());
        this.blit(pPoseStack, this.x - 2, this.y - 2, 0, 46 + i * 20, getWidth() / 2 + 2, 18);
        this.blit(pPoseStack, this.x + this.width / 2, this.y - 2, 200 - getWidth() / 2 - 1, 46 + i * 20, getWidth() / 2 + 2, 18);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public ItemStack getTargetStack() {
        return targetStack;
    }

    public void setTargetStack(ItemStack targetStack) {
        this.targetStack = targetStack;
    }

    public List<FormattedCharSequence> getTooltip() {
        if(displayLoadedMessage) {
            return Language
                    .getInstance()
                    .getVisualOrder(
                            List.of(this.getMessage()
                                    , MutableComponent.create(new TranslatableContents(Constants.gunGuiPhialIsLoaded)).withStyle(Constants.LOADED_PHIAL_TEXT_STYLE)));
        }
        else {
            return Language.getInstance().getVisualOrder(List.of(this.getMessage()));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return this.visible
                && pMouseX >= (double)this.x
                && pMouseY >= (double)this.y
                && pMouseX < (double)(this.x + this.width)
                && pMouseY < (double)(this.y + this.height);
    }

    public void setDisplayLoadedMessage(boolean pSet) {
        displayLoadedMessage = pSet;
    }
}