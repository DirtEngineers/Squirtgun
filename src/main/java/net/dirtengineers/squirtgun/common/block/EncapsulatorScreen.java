package net.dirtengineers.squirtgun.common.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.alchemylib.api.blockentity.container.Direction2D;
import com.smashingmods.alchemylib.api.blockentity.container.data.*;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractFluidBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.container.AbstractProcessingScreen;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class EncapsulatorScreen extends AbstractProcessingScreen<EncapsulatorMenu> {
    protected final List<AbstractDisplayData> displayData = new ArrayList<>();

    public EncapsulatorScreen(EncapsulatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.displayData.add(new ProgressDisplayData(pMenu.getBlockEntity(), 114, 35, 60, 9, Direction2D.RIGHT));
        this.displayData.add(new EnergyDisplayData(pMenu.getBlockEntity(), 12, 12, 16, 54));
        this.displayData.add(new FluidDisplayData((AbstractFluidBlockEntity)pMenu.getBlockEntity(), 48, 12, 16, 54));
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderDisplayData(this.displayData, pPoseStack, this.leftPos, this.topPos);
        this.renderDisplayTooltip(this.displayData, pPoseStack, this.leftPos, this.topPos, pMouseX, pMouseY);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new ResourceLocation(Squirtgun.MOD_ID, Constants.encapsulatorMenuScreenTexture));
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        Component title = MutableComponent.create(new TranslatableContents(Constants.encapsulatorMenuScreenTitle));
        drawString(pPoseStack, this.font, title, this.imageWidth / 2 - this.font.width(title) / 2, -10, -1);
    }
}
