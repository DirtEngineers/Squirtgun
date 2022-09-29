package net.dirtengineers.squirtgun.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import static com.mojang.realmsclient.gui.RealmsWorldSlotButton.SLOT_FRAME_LOCATION;

public class AmmunitionHudOverlay {

    public static final IGuiOverlay HUD_AMMUNITION = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SLOT_FRAME_LOCATION);
        GuiComponent.blit(poseStack, x, y, 0.0F, 0.0F, 80, 80, 80, 80);
        GuiComponent.drawCenteredString(poseStack, gui.getFont(), "TEST!", x, y, 50000000);
//        RenderSystem.setShaderTexture(0, EMPTY_THIRST);
//        for(int i = 0; i < 10; i++) {
//            GuiComponent.blit(poseStack,x - 94 + (i * 9), y - 54,0,0,12,12,
//                    12,12);
//        }

//        RenderSystem.setShaderTexture(0, FILLED_THIRST);
//        for(int i = 0; i < 10; i++) {
//            if(ClientThirstData.getPlayerThirst() > i) {
//                GuiComponent.blit(poseStack,x - 94 + (i * 9),y - 54,0,0,12,12,
//                        12,12);
//            } else {
//                break;
//            }
//        }
    });
}
