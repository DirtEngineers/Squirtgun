package net.dirtengineers.squirtgun.common.block.fluid_encapsulator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.alchemistry.api.blockentity.AbstractFluidBlockEntity;
import com.smashingmods.alchemistry.api.container.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class FluidEncapsulatorScreen extends AbstractAlchemistryScreen<FluidEncapsulatorMenu> {
    protected final List<DisplayData> displayData = new ArrayList();

    public FluidEncapsulatorScreen(FluidEncapsulatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.displayData.add(new ProgressDisplayData(pMenu.getBlockEntity(), 92, 39, 60, 9, Direction2D.RIGHT));
        this.displayData.add(new EnergyDisplayData(pMenu.getBlockEntity(), 26, 21, 16, 46));
        this.displayData.add(new FluidDisplayData((AbstractFluidBlockEntity)pMenu.getBlockEntity(), 134, 21, 16, 46));
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
        RenderSystem.setShaderTexture(0, new ResourceLocation("alchemistry", "textures/gui/liquifier_gui.png"));
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        Component title = MutableComponent.create(new TranslatableContents("alchemistry.container.liquifier"));
        drawString(pPoseStack, this.font, title, this.imageWidth / 2 - this.font.width(title) / 2, -10, -1);
    }
}
