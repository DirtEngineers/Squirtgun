package net.dirtengineers.squirtgun.client.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.client.utility.TextUtility;
import net.dirtengineers.squirtgun.util.FakeItemRenderer_OLD;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhialReloadScreenButton extends Button {
    ItemStack targetStack;
    boolean displayLoadedMessage = false;

    public PhialReloadScreenButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int yOffset = 46 + this.getYImage(this.isHoveredOrFocused()) * 20;
        this.blit(pPoseStack, this.x - 2, this.y - 2, 0, yOffset, getWidth() / 2 + 2, 20);
        this.blit(pPoseStack, this.x + this.width / 2, this.y - 2, 200 - getWidth() / 2 - 1, yOffset, getWidth() / 2 + 2, 20);
        if (this.getTargetStack() != null) {
            FakeItemRenderer_OLD.renderFakeItem(this.getTargetStack(), this.x, this.y, this.width);
        }
    }

    public ItemStack getTargetStack() {
        return targetStack;
    }

    public void setTargetStack(ItemStack targetStack) {
        this.targetStack = targetStack;
    }

    public List<Component> getTooltip() {
        List<Component> components = new ArrayList<>();
        if (!active || displayLoadedMessage) {
            components.add(MutableComponent.create(new TranslatableContents(Constants.gunGuiPhialLoaded)).withStyle(Constants.HOVER_TEXT_STYLE));
        }
        TextUtility.getTooltipComponents(this.targetStack, components, false);

        String namespace = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.targetStack.getItem())).getNamespace();
        components.add(MutableComponent.create(new LiteralContents(StringUtils.capitalize(namespace))).withStyle(Constants.MOD_ID_TEXT_STYLE));

        return components;
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