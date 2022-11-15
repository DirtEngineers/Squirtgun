package net.dirtengineers.squirtgun.client.buttons;

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
    private int color;
    private int borderWidth = 0;
    private final Predicate<Boolean> onPress;
    private final ResourceLocation texture;

    public ImageButton(int xIn, int yIn, int pWidth, int pHeight, Component msg, ResourceLocation texture, int pColor, Predicate<Boolean> onPress) {
        super(xIn, yIn, pWidth, pHeight, msg);
        this.onPress = onPress;
        this.texture = texture;
        this.color = pColor;
    }

    public void addBorder(int pWidth, int pColor){
        this.borderWidth = pWidth;
        this.color = pColor;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        //TODO: Address this color.  Set a background?
        fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, Color.TRANSLUCENT);//0XFFFFFFFF);

//        Triple<Integer, Integer, Integer> colorRBG = Util.getRGBfromInt(color);
//        RenderSystem.setShaderColor((float) colorRBG.a, (float) colorRBG.b, (float) colorRBG.c, 0xFF);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.setFGColor(color);
//                (float) Integer.getInteger(colorString.substring(0, 2))
//                , (float) Integer.getInteger(colorString.substring(2, 4))
//                , (float) Integer.getInteger(colorString.substring(4, 6))
//                , colorString.length() > 6 ? (float) Integer.getInteger(colorString.substring(6, 8)) : (float) 0XFF);
        RenderSystem.setShaderTexture(0, texture);
        blit(stack, this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
    }

    public List<FormattedCharSequence> getTooltip() {
        return Language.getInstance().getVisualOrder(List.of(this.getMessage()));
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if(active) {
            this.onPress.test(true);
        }
    }

    public void setActive(boolean enabled) {
        active = enabled;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }
}
