package net.dirtengineers.squirtgun.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SquirtgunReloadScreen extends Screen {
    private final List<ChemicalPhial> phials = new ArrayList<>();
    private boolean checkInventory = true;
    public SquirtgunReloadScreen(ItemStack gun) {
        super(MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick){
        renderBackground(pPoseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        drawCenteredString(
                pPoseStack,
                Minecraft.getInstance().font,
                MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)).withStyle(ChatFormatting.BOLD),
                width / 2,
                12,
                0xFFFFFF);
        net.minecraftforge.client.gui.widget.ExtendedButton button = new ExtendedButton(200,50, 75, 20, Component.literal("BLAH!"), null);
        button.visible = true;
        button.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        getPlayerInventoryPhials();

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private void getPlayerInventoryPhials(){
        if(checkInventory) {
            Player player = Minecraft.getInstance().player;
            if (player != null && phials.isEmpty()) {
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.getItem() instanceof ChemicalPhial) {
                        if (!phials.contains((ChemicalPhial) stack.getItem())) {
                            phials.add((ChemicalPhial) stack.copy().getItem());
                        }
                    }
                }
                if (player.getInventory().offhand.get(0).getItem() instanceof ChemicalPhial) {
                    if (!phials.contains((ChemicalPhial) player.getInventory().offhand.get(0).getItem())) {
                        phials.add((ChemicalPhial) player.getInventory().offhand.get(0).getItem());
                    }
                }
            }
            checkInventory = false;
        }
    }
}
