package net.dirtengineers.squirtgun.common.network;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractProcessingBlockEntity;
import com.smashingmods.alchemylib.api.network.AlchemyPacket;
import net.dirtengineers.squirtgun.registry.RecipeRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class SetRecipeC2SPacket implements AlchemyPacket {
    private final BlockPos blockPos;
    private final ResourceLocation recipeId;
    private final String group;

    public SetRecipeC2SPacket(BlockPos pBlockPos, ResourceLocation pRecipeId, String pGroup) {
        this.blockPos = pBlockPos;
        this.recipeId = pRecipeId;
        this.group = pGroup;
    }

    public SetRecipeC2SPacket(FriendlyByteBuf pBuffer) {
        this.blockPos = pBuffer.readBlockPos();
        this.recipeId = pBuffer.readResourceLocation();
        this.group = pBuffer.readUtf();
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeBlockPos(this.blockPos);
        pBuffer.writeResourceLocation(this.recipeId);
        pBuffer.writeUtf(this.group);
    }

    @Override
    public void handle(NetworkEvent.Context pContext) {
        Player player = pContext.getSender();
        Objects.requireNonNull(player);
        Objects.requireNonNull(player.getLevel());
        Level level = player.getLevel();
        BlockEntity blockEntity = player.getLevel().getBlockEntity(blockPos);
        RecipeRegistration.getRecipeByGroupAndId(group, recipeId, level).ifPresent((recipe) -> {
            if (blockEntity instanceof AbstractProcessingBlockEntity processingBlockEntity) {
                processingBlockEntity.setProgress(0);
                processingBlockEntity.setRecipe(recipe);
                processingBlockEntity.setChanged();
            }
        });
    }
}
