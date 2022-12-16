package net.dirtengineers.squirtgun.client.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.alchemylib.api.blockentity.container.AbstractProcessingScreen;
import com.smashingmods.alchemylib.api.blockentity.container.button.AbstractAlchemyButton;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.network.ToggleLockButtonC2SPacket;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import javax.annotation.Nonnull;

public class LockButton extends AbstractAlchemyButton {
    public LockButton(AbstractProcessingScreen<?> pParent) {
        super(pParent, (pButton) -> {
            boolean toggleLock = !pParent.getBlockEntity().isRecipeLocked();
            pParent.getBlockEntity().setRecipeLocked(toggleLock);
            pParent.getBlockEntity().setChanged();
            Squirtgun.PACKET_HANDLER.sendToServer(new ToggleLockButtonC2SPacket(pParent.getBlockEntity().getBlockPos(), toggleLock));
        });
    }

    public void renderButton(@Nonnull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.blit(pPoseStack, this.x, this.y, 25 + (this.blockEntity.isRecipeLocked() ? 0 : 1) * 20, 0, this.width, this.height);
        this.renderButtonTooltip(pPoseStack, pMouseX, pMouseY);
    }

    public MutableComponent getMessage() {
        return this.blockEntity.isRecipeLocked()
                ? MutableComponent.create(new TranslatableContents("alchemylib.container.unlock_recipe"))
                : MutableComponent.create(new TranslatableContents("alchemylib.container.lock_recipe"));
    }
}
