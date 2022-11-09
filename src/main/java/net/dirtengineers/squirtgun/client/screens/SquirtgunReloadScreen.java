package net.dirtengineers.squirtgun.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SquirtgunReloadScreen extends Screen {
    private final List<ChemicalPhial> phials = new ArrayList<>();
    private ItemStack gun;
    private boolean checkInventory = true;
    private HashMap<ChemicalPhial, ImageButton> phialButtons = new HashMap<>();

    public SquirtgunReloadScreen(ItemStack pGun) {
        super(MutableComponent.create(new TranslatableContents(Constants.gunFunctionality)));
        gun = pGun;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick){
        getPlayerInventoryPhials();
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
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.children().forEach(e -> {
                if(e instanceof ImageButton btn) {
                    if (btn.isMouseOver(pMouseX, pMouseY))
                        renderTooltip(pPoseStack, btn.getTooltip(), pMouseX, pMouseY);
                }
        });
//        net.minecraftforge.client.gui.widget.ExtendedButton button = new ExtendedButton(200,50, 75, 20, Component.literal("BLAH!"), null);
//
//        button.visible = true;
//        button.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void getPlayerInventoryPhials(){
        if(checkInventory) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                populatePhialsList(player);
                populatePhialButtons();
                checkInventory = false;
            }
        }
    }

    private void populatePhialsList(Player pPlayer){
        phials.clear();
        for (ItemStack stack : pPlayer.getInventory().items) {
            if (stack.getItem() instanceof ChemicalPhial) {
                if (!phials.contains((ChemicalPhial) stack.getItem())) {
                    phials.add((ChemicalPhial) stack.copy().getItem());
                }
            }
        }
        if (pPlayer.getInventory().offhand.get(0).getItem() instanceof ChemicalPhial) {
            if (!phials.contains((ChemicalPhial) pPlayer.getInventory().offhand.get(0).getItem())) {
                phials.add((ChemicalPhial) pPlayer.getInventory().offhand.get(0).getItem());
            }
        }
        if(!phials.isEmpty()){
            phials.sort(Comparator.comparing(s -> s.getDescriptionId().toLowerCase()));
        }
    }

    private void populatePhialButtons(){
        int baseX = width / 2, baseY = height / 2;
        int top = baseY - 60;
        int index = 0, x = baseX + 10, y = top + 20;
        for (ChemicalPhial phial : phials) {
            ImageButton btn = new ImageButton(
                    x + (index * 30)
                    , y
                    , phial.getDescription()
                    , new ResourceLocation(Squirtgun.MOD_ID, "textures/item/" + "squirt_phial_white.png")
                    , send -> this.selectPhial(phial, send));
            addRenderableWidget(btn);
            phialButtons.put(phial, btn);
//            ForgeRegistries.ITEMS.getEntries()
            // Spaces the upgrades
            index ++;
            if( index % 4 == 0 ) {
                index = 0;
                y += 35;
            }
        }
    }

    private boolean selectPhial(ChemicalPhial pPhial, boolean pUpdate){

        return true;
    }
//    private boolean toggleUpgrade(Upgrade upgrade, boolean update) {
//        // When the button is clicked we toggle
//        if( update ) {
//            this.updateButtons(upgrade);
//            PacketHandler.sendToServer(new PacketUpdateUpgrade(upgrade.getName()));
//        }
//
//        // When we're just init the gui, we check if it's on or off.
//        return upgrade.isEnabled();
//    }
}
