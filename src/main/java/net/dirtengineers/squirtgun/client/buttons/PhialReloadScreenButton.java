package net.dirtengineers.squirtgun.client.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.alchemylib.api.blockentity.container.FakeItemRenderer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class PhialReloadScreenButton extends Button {
    ItemStack targetStack;
//    private final ResourceLocation texture;

    public PhialReloadScreenButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation alternateTexture, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
//        this.texture = alternateTexture != null ? alternateTexture : Constants.phialReloadScreenButtonTexture;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if(this.getTargetStack() != null) {
            FakeItemRenderer.renderFakeItem(this.getTargetStack(), this.x, this.y);
        }
//        Color activeColor = this.active ? Color.GREEN : Color.RED;
//        fill(pPoseStack, this.x, this.y, this.x + this.width, this.y + this.height, Color.TRANSLUCENT);
//
//
//
//        RenderSystem.setShaderColor(1, 1, 1, 1);
//        RenderSystem.setShaderTexture(0, texture);
//        blit(pPoseStack, this.x, this.y, 0, 0, 16, 16, 16, 16);
//        fill(pPoseStack, this.x, this.y, this.x + this.width, this.y + this.height, ((this.active ? 0x68000000 : 0x9B000000)) + activeColor.getRGB());
//        RenderSystem.setShaderTexture(0, texture);
//        blit(pPoseStack, this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
//        this.packedFGColor = 0X00_FF00;
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