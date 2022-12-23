package net.dirtengineers.squirtgun.common.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.alchemylib.api.blockentity.container.AbstractProcessingScreen;
import com.smashingmods.alchemylib.api.blockentity.container.Direction2D;
import com.smashingmods.alchemylib.api.blockentity.container.data.AbstractDisplayData;
import com.smashingmods.alchemylib.api.blockentity.container.data.EnergyDisplayData;
import com.smashingmods.alchemylib.api.blockentity.container.data.FluidDisplayData;
import com.smashingmods.alchemylib.api.blockentity.container.data.ProgressDisplayData;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractFluidBlockEntity;
import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import com.smashingmods.alchemylib.api.storage.ProcessingSlotHandler;
import com.smashingmods.alchemylib.client.button.LockButton;
import com.smashingmods.alchemylib.client.button.PauseButton;
import com.smashingmods.alchemylib.client.button.RecipeSelectorButton;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.screens.RecipeSelectorScreen;
import net.dirtengineers.squirtgun.client.utility.RecipeDisplayUtil;
import net.dirtengineers.squirtgun.client.utility.TextUtility;
import net.dirtengineers.squirtgun.common.recipe.AbstractPhialRecipe;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EncapsulatorScreen extends AbstractProcessingScreen<EncapsulatorMenu> {
    protected final List<AbstractDisplayData> displayData = new ArrayList<>();
    private final EncapsulatorBlockEntity blockEntity;
    private final LockButton lockButton = new LockButton(this);
    private final PauseButton pauseButton = new PauseButton(this);
    private final RecipeSelectorScreen<AbstractProcessingScreen<?>, AbstractFluidBlockEntity, AbstractPhialRecipe> recipeSelectorScreen;
    private final RecipeSelectorButton recipeSelector;

    public EncapsulatorScreen(EncapsulatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.displayData.add(new ProgressDisplayData(pMenu.getBlockEntity(), 114, 35, 60, 9, Direction2D.RIGHT));
        this.displayData.add(new EnergyDisplayData(pMenu.getBlockEntity(), 12, 12, 16, 54));
        this.displayData.add(new FluidDisplayData((AbstractFluidBlockEntity) pMenu.getBlockEntity(), 48, 12, 16, 54));
        this.blockEntity = (EncapsulatorBlockEntity) pMenu.getBlockEntity();
        this.recipeSelectorScreen = new RecipeSelectorScreen<>(
                this,
                blockEntity,
                RecipeRegistration.getAllPhialRecipes(pMenu.getLevel()));
        this.recipeSelector = new RecipeSelectorButton(this, recipeSelectorScreen);
    }

    @Override
    protected void init() {
        this.recipeSelectorScreen.setTopPos((height - imageHeight) / 2);
        this.widgets.add(lockButton);
        this.widgets.add(pauseButton);
        this.widgets.add(recipeSelector);
        super.init();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderDisplayData(displayData, pPoseStack, leftPos, topPos);
        this.renderCurrentRecipe(pPoseStack, pMouseX, pMouseY);
        this.renderDisplayTooltip(displayData, pPoseStack, leftPos, topPos, pMouseX, pMouseY);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    private void renderFloatingItem(ItemStack pItemStack, int pX, int pY) {
        RenderSystem.applyModelViewMatrix();
        this.itemRenderer.renderAndDecorateItem(pItemStack, pX, pY);
        this.itemRenderer.renderGuiItemDecorations(this.font, pItemStack, pX, pY);
        this.setBlitOffset(0);
        this.itemRenderer.blitOffset = 0.0F;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new ResourceLocation(Squirtgun.MOD_ID, Constants.encapsulatorMenuScreenTexture));
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        Component title = new TranslatableComponent(Constants.encapsulatorMenuScreenTitle);
        drawString(pPoseStack, this.font, title, this.imageWidth / 2 - this.font.width(title) / 2, -10, -1);
    }

    private void renderCurrentRecipe(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        AbstractPhialRecipe currentRecipe = menu.getBlockEntity().getRecipe();
        ProcessingSlotHandler handler = blockEntity.getInputHandler();

        if (currentRecipe != null) {
            int recipeLeftPos = leftPos + 120;
            int recipeTopPos = topPos + 12;
            ItemStack currentOutput = currentRecipe.getOutput().get(0);
            itemRenderer.renderAndDecorateItem(currentOutput, recipeLeftPos, recipeTopPos);

            if (pMouseX >= recipeLeftPos - 3 && pMouseX < recipeLeftPos + 11 && pMouseY >= recipeTopPos - 4 && pMouseY < recipeTopPos + 20) {
                renderTooltip(pPoseStack
                        , TextUtility.getRecipeItemTooltipComponent(currentOutput, new TranslatableComponent(Constants.currentSelectedRecipe))
                        , Optional.empty()
                        , pMouseX
                        , pMouseY);
            }

            int xOrigin = leftPos + 84;
            int yOrigin = topPos + 18;
            for (int index = 0; index < 2; index++) {
                int y = yOrigin + index * 26;
                if (index < currentRecipe.getInput().size()) {
                    ItemStack itemStack =
                            RecipeDisplayUtil.getRecipeInputByIndex(currentRecipe, index);

                    if (handler.getStackInSlot(index).isEmpty()) {

                        renderFloatingItem(itemStack, xOrigin, y);
                        itemRenderer.renderGuiItemDecorations(font, itemStack, xOrigin, y);

                        if (pMouseX >= xOrigin - 2 && pMouseX < xOrigin + 16 && pMouseY >= y - 1 && pMouseY < y + 17) {
                            List<Component> components = TextUtility.getRecipeItemTooltipComponent(itemStack, new TranslatableComponent(Constants.recipeRequiredInput));
                            this.renderTooltip(pPoseStack, components, Optional.empty(), pMouseX, pMouseY);
                        }
                    }
                }
            }
        }
    }
}
