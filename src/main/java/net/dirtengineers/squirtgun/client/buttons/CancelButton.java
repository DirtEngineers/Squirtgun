package net.dirtengineers.squirtgun.client.buttons;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class CancelButton extends Button {

    private final ResourceLocation IMAGE_LOCATION = new ResourceLocation("textures/gui/container/beacon.png");

    public CancelButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.getYImage(this.isHoveredOrFocused()) - 1;
        this.blit(pPoseStack, this.x, this.y, 0, 66 + i * 20, 10, 20);
        this.blit(pPoseStack, this.x + 10, this.y, 191, 66 + i * 20, 10, 20);
        RenderSystem.setShaderTexture(0, IMAGE_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(pPoseStack, this.x + 3, this.y + 3, 114, 223, 13, 13);
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

    public List<FormattedCharSequence> getTooltip() {
            return Language.getInstance().getVisualOrder(List.of(this.getMessage()));
    }

    public void setActive(boolean pActive){
        active = pActive;
    }
}
