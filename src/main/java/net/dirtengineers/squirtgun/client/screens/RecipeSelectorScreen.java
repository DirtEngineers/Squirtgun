package net.dirtengineers.squirtgun.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.alchemylib.api.blockentity.container.AbstractProcessingScreen;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractFluidBlockEntity;
import com.smashingmods.alchemylib.api.recipe.AbstractProcessingRecipe;
import com.smashingmods.alchemylib.api.recipe.ProcessingRecipe;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.utility.RecipeDisplayUtil;
import net.dirtengineers.squirtgun.client.utility.TextUtility;
import net.dirtengineers.squirtgun.common.block.EncapsulatorBlockEntity;
import net.dirtengineers.squirtgun.common.network.SetRecipeC2SPacket;
import net.dirtengineers.squirtgun.common.recipe.AbstractPhialRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeSelectorScreen<P extends AbstractProcessingScreen<?>, B extends AbstractFluidBlockEntity, R extends AbstractProcessingRecipe> extends Screen {
    private int leftPos;
    private int topPos;
    private int recipeBoxLeftPos;
    private int recipeBoxTopPos;
    private final P parentScreen;
    private final B blockEntity;
    private final LinkedList<R> recipes;
    private final LinkedList<AbstractProcessingRecipe> displayedRecipes = new LinkedList<>();
    private final EditBox searchBox;
    private float scrollOffset;
    private boolean scrolling;
    private int startIndex;
    private final ResourceLocation bgLocation = new ResourceLocation(Squirtgun.MOD_ID, "textures/gui/recipe_select_gui.png");

    public RecipeSelectorScreen(AbstractProcessingScreen<?> pParentScreen, AbstractFluidBlockEntity pBlockEntity, LinkedList<AbstractPhialRecipe> pRecipes) {
        super(MutableComponent.create(new LiteralContents("")));
        this.parentScreen = (P) pParentScreen;
        this.blockEntity = (B) pBlockEntity;
        this.recipes = (LinkedList<R>) pRecipes;
        this.searchBox = new EditBox(Minecraft.getInstance().font, 0, 0, 92, 12, MutableComponent.create(new LiteralContents("")));
        if (!((EncapsulatorBlockEntity)this.blockEntity).getSearchText().isEmpty()) {
            this.searchBox.setValue(((EncapsulatorBlockEntity)this.blockEntity).getSearchText());
            this.searchRecipeList(((EncapsulatorBlockEntity)this.blockEntity).getSearchText());
        }
    }

    protected void init() {
        this.leftPos = (this.width - 184) / 2;
        this.recipeBoxLeftPos = this.leftPos + 58;
        this.recipeBoxTopPos = this.topPos + 26;
        super.init();
    }

    public void tick() {
        if (this.searchBox.getValue().isEmpty()) {
        ((EncapsulatorBlockEntity)this.blockEntity).setSearchText("");
            this.resetDisplayedRecipes();
            this.searchBox.setSuggestion(MutableComponent.create(new TranslatableContents(Constants.guiSearch)).getString());
        } else {
            if (this.displayedRecipes.size() < 30) {
                this.mouseScrolled(0.0, 0.0, 0.0);
                this.scrollOffset = 0.0F;
            }

            ((EncapsulatorBlockEntity)this.blockEntity).setSearchText(this.searchBox.getValue());
            this.searchRecipeList(this.searchBox.getValue());
            if (this.displayedRecipes.size() <= 30) {
                this.startIndex = 0;
                this.scrollOffset = 0.0F;
            }

            this.searchBox.setSuggestion("");
        }
        super.tick();
    }

    public void onClose() {
        ((EncapsulatorBlockEntity)this.blockEntity).setRecipeSelectorOpen(false);
        super.onClose();
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBg(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderRecipeBox(pPoseStack, pMouseX, pMouseY);
        this.renderWidget(this.searchBox, this.leftPos + 58, this.topPos + 11);
        this.renderParentTooltips(pPoseStack, pMouseX, pMouseY);
    }

    private void renderBg(PoseStack pPoseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, bgLocation);
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, 184, 162);
    }

    private void renderRecipeBox(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, bgLocation);
        int lastDisplayedIndex = this.startIndex + 30;
        this.renderScrollbar(pPoseStack);
        this.renderRecipeButtons(pPoseStack, pMouseX, pMouseY, lastDisplayedIndex);
        this.renderRecipeButtonItems(pPoseStack, pMouseX, pMouseY, lastDisplayedIndex);
        this.renderCurrentRecipe(pPoseStack, pMouseX, pMouseY);
    }

    private void renderScrollbar(PoseStack pPoseStack) {
        int scrollPosition = (int)(93.0F * this.scrollOffset);
        this.blit(pPoseStack, this.leftPos + 154, this.topPos + 28 + scrollPosition, 18 + (this.isScrollBarActive() ? 0 : 12), 162, 12, 15);
    }

    private void renderRecipeButtons(PoseStack pPoseStack, int pMouseX, int pMouseY, int pLastDisplayedIndex) {
        for(int index = this.startIndex; index < pLastDisplayedIndex && index < this.getDisplayedRecipes().size(); ++index) {
            int firstDisplayedIndex = index - this.startIndex;
            int xStart = this.recipeBoxLeftPos + firstDisplayedIndex % 5 * 18;
            int yStart = this.recipeBoxTopPos + firstDisplayedIndex / 5 * 18 + 2;
            int vOffset = 162;
            int currentRecipeIndex = this.getDisplayedRecipes().indexOf(this.blockEntity.getRecipe());
            if (index == currentRecipeIndex) {
                vOffset += 18;
            } else if (pMouseX >= xStart && pMouseX < xStart + 18 && pMouseY >= yStart && pMouseY < yStart + 18) {
                vOffset += 36;
            }

            this.blit(pPoseStack, xStart, yStart, 0, vOffset, 18, 18);
        }
    }

    private void renderRecipeButtonItems(PoseStack pPoseStack, int pMouseX, int pMouseY, int pLastDisplayedIndex) {
        LinkedList<AbstractProcessingRecipe> displayedRecipes = this.getDisplayedRecipes();

        for(int index = this.startIndex; index >= 0 && index < pLastDisplayedIndex && index < displayedRecipes.size(); ++index) {
            int firstDisplayedIndex = index - this.startIndex;
            ItemStack target = RecipeDisplayUtil.getTarget(this.getDisplayedRecipes().get(index));
            int xStart = this.recipeBoxLeftPos + firstDisplayedIndex % 5 * 18 + 1;
            int yStart = this.recipeBoxTopPos + firstDisplayedIndex / 5 * 18 + 3;
            this.renderFloatingItem(target, xStart, yStart);
            if (pMouseX >= xStart - 1 && pMouseX <= xStart + 16 && pMouseY >= yStart - 1 && pMouseY <= yStart + 16) {
                List<Component> components = TextUtility.getRecipeItemTooltipComponent(target, MutableComponent.create(new TranslatableContents(Constants.guiSelectRecipe)));
                this.renderTooltip(pPoseStack, components, Optional.empty(), pMouseX, pMouseY);
            }
        }
    }

    private void renderFloatingItem(ItemStack pItemStack, int pX, int pY) {
        RenderSystem.applyModelViewMatrix();
        this.setBlitOffset(2000);
        this.itemRenderer.blitOffset = 2000.0F;
        this.itemRenderer.renderAndDecorateItem(pItemStack, pX, pY);
        this.itemRenderer.renderGuiItemDecorations(this.font, pItemStack, pX, pY, null);
        this.setBlitOffset(0);
        this.itemRenderer.blitOffset = 0.0F;
    }

    private void renderCurrentRecipe(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        ProcessingRecipe recipe = this.blockEntity.getRecipe();
        if (recipe != null) {
            this.recipeLooper((pIndex, pInputSize, pX, pY) -> {
                if (pIndex < pInputSize) {
                    ItemStack itemStack = RecipeDisplayUtil.getRecipeInputByIndex(recipe, pIndex);
                    this.renderSlot(pPoseStack, pX, pY);
                    this.renderFloatingItem(itemStack, pX + 1, pY + 1);
                }
            });

            this.recipeLooper((pIndex, pInputSize, pX, pY) -> {
                if (pIndex < pInputSize) {
                    ItemStack itemStack = RecipeDisplayUtil.getRecipeInputByIndex(recipe, pIndex);
                    if (pMouseX >= pX - 1 && pMouseX < pX + 17 && pMouseY >= pY - 1 && pMouseY < pY + 17 && !itemStack.isEmpty()) {
                        List<Component> components = TextUtility.getRecipeItemTooltipComponent(itemStack, MutableComponent.create(new TranslatableContents(Constants.recipeRequiredInput)));
                        this.renderTooltip(pPoseStack, components, Optional.empty(), pMouseX, pMouseY);
                    }
                }
            });
            ItemStack target = RecipeDisplayUtil.getTarget(recipe);
            this.renderFloatingItem(target, this.leftPos + 21, this.topPos + 30);
            if (pMouseX >= this.leftPos + 17 && pMouseX < this.leftPos + 41 && pMouseY >= this.topPos + 27 && pMouseY <= this.topPos + 50) {
                List<Component> components = TextUtility.getRecipeItemTooltipComponent(target, MutableComponent.create(new TranslatableContents(Constants.currentSelectedRecipe)));
                this.renderTooltip(pPoseStack, components, Optional.empty(), pMouseX, pMouseY);
            }
        } else {
            this.recipeLooper((pIndex, pInputSize, pX, pY) -> this.renderSlot(pPoseStack, pX, pY));
        }
    }

    private void recipeLooper(LoopConsumer pConsumer) {
        int inputSize = RecipeDisplayUtil.getInputSize(this.blockEntity);
        int totalRows;
        int totalCols;

        switch (inputSize) {
            case 1 -> {
                totalRows = 1;
                totalCols = 1;
            }
            case 2 -> {
                totalRows = 1;
                totalCols = 2;
            }
            case 3, 4 -> {
                totalRows = 2;
                totalCols = 2;
            }
            default -> {
                totalRows = 0;
                totalCols = 0;
            }
        }

        int xOrigin = totalCols == 1 ? this.leftPos + 20 : this.leftPos + 11;
        int yOrigin = this.topPos + 59;

        for(int row = 0; row < totalRows; ++row) {
            for(int col = 0; col < totalCols; ++col) {
                int index = col + row * 2;
                int x = xOrigin + col * 18;
                int y = yOrigin + row * 18;
                pConsumer.accept(index, inputSize, x, y);
            }
        }
    }

    private void renderSlot(PoseStack pPoseStack, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, bgLocation);
        this.blit(pPoseStack, pX, pY, 0, 216, 18, 18);
    }

    public <W extends GuiEventListener & Widget & NarratableEntry> void renderWidget(W pWidget, int pX, int pY) {
        if (!this.renderables.contains(pWidget)) {
            if (pWidget instanceof AbstractWidget widget) {
                widget.x = pX;
                widget.y = pY;
            }
            this.addRenderableWidget(pWidget);
        }
    }

    public void renderParentTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY) {

        for (Widget renderable : this.parentScreen.renderables) {
            if (renderable instanceof AbstractWidget widget) {
                int xStart = widget.x;
                int xEnd = xStart + widget.getWidth();
                int yStart = widget.y;
                int yEnd = yStart + widget.getHeight();
                if (pMouseX > xStart && pMouseX < xEnd && pMouseY > yStart && pMouseY < yEnd) {
                    this.renderTooltip(pPoseStack, widget.getMessage(), pMouseX, pMouseY);
                }
            }
        }
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 69 && this.searchBox.isFocused()) {
            return false;
        } else {
            if (pKeyCode == 258 && !this.searchBox.isFocused()) {
                this.searchBox.setFocus(true);
                this.searchBox.setEditable(true);
                this.searchBox.active = true;
            } else if (pKeyCode == 256 && this.searchBox.isFocused()) {
                this.searchBox.setFocus(false);
                this.searchBox.setEditable(false);
                this.searchBox.active = false;
                return false;
            }
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        Objects.requireNonNull(Minecraft.getInstance().player);
        Objects.requireNonNull(Minecraft.getInstance().gameMode);
        int searchBoxMinX = this.leftPos + 56;
        int searchBoxMaxX = searchBoxMinX + 72;
        int searchBoxMinY = this.topPos + 12;
        int searchBoxMaxY = searchBoxMinY + 12;
        if (pMouseX >= (double)searchBoxMinX && pMouseX < (double)searchBoxMaxX && pMouseY >= (double)searchBoxMinY && pMouseY < (double)searchBoxMaxY) {
            this.searchBox.setFocus(true);
            this.searchBox.setEditable(true);
            this.searchBox.active = true;
        } else if (this.searchBox.isFocused() || this.searchBox.isActive()) {
            this.searchBox.setFocus(false);
            this.searchBox.active = false;
        }

        this.scrolling = false;
        int lastDisplayedIndex = this.startIndex + 30;

        int yEnd;
        for(int index = this.startIndex; index < lastDisplayedIndex; ++index) {
            int currentIndex = index - this.startIndex;
            double boxX = pMouseX - (double)(this.recipeBoxLeftPos + currentIndex % 5 * 18);
            double boxY = pMouseY - (double)(this.recipeBoxTopPos + currentIndex / 5 * 18);
            if (boxX > 0.0 && boxX <= 19.0 && boxY > 0.0 && boxY <= 19.0 && !this.blockEntity.isRecipeLocked() && this.isValidRecipeIndex(index)) {
                AbstractProcessingRecipe recipe = this.getDisplayedRecipes().get(index);
                Squirtgun.PACKET_HANDLER.sendToServer(new SetRecipeC2SPacket(this.blockEntity.getBlockPos(), recipe.getId(), recipe.getGroup()));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                return true;
            }

            yEnd = this.leftPos + 154;
            int scrollMinY = this.topPos + 28;
            int scrollMaxX = yEnd + 12;
            int scrollMaxY = scrollMinY + 108;
            if (pMouseX >= (double)yEnd && pMouseX < (double)scrollMaxX && pMouseY >= (double)scrollMinY && pMouseY < (double)scrollMaxY) {
                this.scrolling = true;
            }
        }

        for (Widget renderable : this.parentScreen.renderables) {
            if (renderable instanceof AbstractWidget widget) {
                int xStart = widget.x;
                int xEnd = xStart + widget.getWidth();
                int yStart = widget.y;
                yEnd = yStart + widget.getHeight();
                if (pMouseX > (double) xStart && pMouseX < (double) xEnd && pMouseY > (double) yStart && pMouseY < (double) yEnd) {
                    return this.parentScreen.mouseClicked(pMouseX, pMouseY, pButton);
                }
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int scrollbarTopPos = this.topPos + 28;
            int scrollbarBottomPos = scrollbarTopPos + 108;
            this.scrollOffset = ((float)pMouseY - (float)scrollbarTopPos - 7.5F) / ((float)(scrollbarBottomPos - scrollbarTopPos) - 15.0F);
            this.scrollOffset = Mth.clamp(this.scrollOffset, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffset * (float)this.getOffscreenRows()) + 0.5) * 5;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pMouseX >= (double)this.leftPos && pMouseX < (double)(this.leftPos + 184) && pMouseY >= (double)this.topPos && pMouseY < (double)(this.topPos + 162) && this.isScrollBarActive()) {
            this.scrollOffset = Mth.clamp(this.scrollOffset - (float)pDelta / (float)this.getOffscreenRows(), 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffset * (float)this.getOffscreenRows()) + 0.5) * 5;
        }

        return true;
    }

    private int getOffscreenRows() {
        return (this.displayedRecipes.size() + 6 - 1) / 6 - 3;
    }

    public void setTopPos(int topPos) {
        this.topPos = topPos;
    }

    public LinkedList<AbstractProcessingRecipe> getDisplayedRecipes() {
        return this.displayedRecipes;
    }

    public boolean isPauseScreen() {
        return false;
    }

    private boolean isScrollBarActive() {
        return this.displayedRecipes.size() > 30;
    }

    private boolean isValidRecipeIndex(int pSlot) {
        return pSlot >= 0 && pSlot < this.getDisplayedRecipes().size();
    }

    public void resetDisplayedRecipes() {
        this.displayedRecipes.clear();
        this.displayedRecipes.addAll(this.recipes);
        this.displayedRecipes.sort((r1, r2) -> r1.getId().compareNamespaced(r2.getId()));
    }

    private void searchRecipeList(String pKeyword) {
        this.getDisplayedRecipes().clear();
        LinkedList<AbstractProcessingRecipe> recipes = this.recipes.stream().filter((recipe) -> {
            Pair<ResourceLocation, String> searchablePair = RecipeDisplayUtil.getSearchablePair(recipe);
            ResourceLocation registryName = searchablePair.getLeft();
            String description = searchablePair.getRight();
            String keyword = pKeyword.toLowerCase();
            if (keyword.charAt(0) != '@') {
                return description.toLowerCase().contains(keyword);
            } else if (keyword.contains(" ")) {
                if (keyword.split(" ").length <= 1) {
                    return registryName.getNamespace().contains(keyword.substring(1, keyword.length() - 1));
                } else {
                    String[] splitKeyword = keyword.split(" ");
                    return registryName.getNamespace().contains(splitKeyword[0].substring(1)) && registryName.getPath().contains(splitKeyword[1]);
                }
            } else {
                return registryName.getNamespace().contains(keyword.substring(1));
            }
        }).collect(Collectors.toCollection(LinkedList::new));
        this.getDisplayedRecipes().addAll(recipes);
    }

    @FunctionalInterface
    interface LoopConsumer {
        void accept(int var1, int var2, int var3, int var4);
    }
}
