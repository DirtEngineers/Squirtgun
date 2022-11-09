package net.dirtengineers.squirtgun.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

public class ImageButton extends AbstractWidget {
    private final Predicate<Boolean> onPress;
    private boolean enabled;
    private final ResourceLocation texture;

    public ImageButton(int xIn, int yIn, Component msg, ResourceLocation texture, Predicate<Boolean> onPress) {
        super(xIn, yIn, 21, 26, msg);
        this.onPress = onPress;
        this.texture = texture;
        this.enabled = true;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, Color.TRANSLUCENT);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
        blit(stack, this.x + 2, this.y + 5, 0, 0, 16, 16, 16, 16);
    }

    public List<FormattedCharSequence> getTooltip() {
        return Language.getInstance().getVisualOrder(List.of(this.getMessage()));
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        this.enabled = !this.enabled;
        this.onPress.test(true);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }
}
