package net.dirtengineers.squirtgun.client.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class PhialReloadScreenButton extends Button {
    //TODO: Change from BasePhial to ItemStack
    ItemStack targetStack;
    private final ResourceLocation texture;

    public PhialReloadScreenButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation alternateTexture, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
        this.texture = alternateTexture != null ? alternateTexture : Constants.phialReloadScreenButtonTexture;
    }

    public PhialReloadScreenButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation alternateTexture, OnPress pOnPress, OnTooltip pOnTooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pOnTooltip);
        this.texture = alternateTexture != null ? alternateTexture : Constants.phialReloadScreenButtonTexture;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
//        fill(pPoseStack, this.x, this.y, this.x + this.width, this.y + this.height, Color.TRANSLUCENT);//0XFFFFFFFF);

//        RenderSystem.setShaderTexture(0, texture);
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//        this.setFGColor(0x00FF00);
//        blit(pPoseStack, this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        blit(pPoseStack, this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
    }

    @Override
    public void renderToolTip(PoseStack pPoseStack, int pMouseX, int pMouseY){
        super.renderToolTip(pPoseStack, pMouseX, pMouseY);
    }

    public ItemStack getTargetStack() {
        return targetStack;
    }

    public void setTargetStack(ItemStack targetStack) {
        this.targetStack = targetStack;
    }

    public List<FormattedCharSequence> getTooltip() {
        return Language.getInstance().getVisualOrder(List.of(this.getMessage()));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPress() {
        this.onPress.onPress(this);
    }

//    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
//        super.onTooltip.onTooltip(this, pPoseStack, pMouseX, pMouseY);
//    }
    @OnlyIn(Dist.CLIENT)
    class SubmitButtonTooltip implements Button.OnTooltip {
        public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
            renderToolTip(pPoseStack, pMouseX, pMouseY);
//            if (ChatReportScreen.this.cannotBuildReason != null) {
//                Component component = ChatReportScreen.this.cannotBuildReason.message();
//                ChatReportScreen.this.renderTooltip(p_240156_, ChatReportScreen.this.font.split(component, Math.max(ChatReportScreen.this.width / 2 - 43, 170)), p_240157_, p_240158_);
//            }
        }
    }
}