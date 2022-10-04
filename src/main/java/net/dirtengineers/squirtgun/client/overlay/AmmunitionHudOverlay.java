package net.dirtengineers.squirtgun.client.overlay;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class AmmunitionHudOverlay {
    private static final ResourceLocation TEST_TEXTURE = new ResourceLocation(Squirtgun.MOD_ID,
            "textures/entity/projectile/squirt_slug.png");
    public static final IGuiOverlay HUD_AMMUNITION = ((gui, poseStack, partialTick, width, height) -> {

        int x = width / 2;
        int y = height;
        String testText = "RIGHT HERE!";
        Font font = gui.getFont();

        Player player = Minecraft.getInstance().player;
        assert player != null;
        if(player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SquirtgunItem){
            GuiComponent.drawString(poseStack, font, testText, x - font.width(testText) / 2, y, 0xFFFFFFFF);
        }
    });
}
