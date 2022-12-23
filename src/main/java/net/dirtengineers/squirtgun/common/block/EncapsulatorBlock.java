package net.dirtengineers.squirtgun.common.block;

import net.dirtengineers.squirtgun.Config;
import net.dirtengineers.squirtgun.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EncapsulatorBlock extends AbstractMachineBlock {

    public static final VoxelShape SHAPE = Shapes.block();

    public EncapsulatorBlock() {
        super(EncapsulatorBlockEntity::new);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(new TranslatableComponent(Constants.encapsulatorEnergyRequirementTooltipKey, Config.Common.encapsulatorEnergyPerTick.get()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            boolean interactionSuccessful = true;
            if (blockEntity instanceof EncapsulatorBlockEntity) {
                interactionSuccessful = ((EncapsulatorBlockEntity)blockEntity).onBlockActivated(pLevel, pPos, pPlayer, pHand);
            }
            if (!interactionSuccessful) {
                NetworkHooks.openGui((ServerPlayer)pPlayer, (EncapsulatorBlockEntity)blockEntity, pPos);
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
       return !pLevel.isClientSide() ? (level, pBlockPos, pBlockState, pBlockEntity) -> {
            if (pBlockEntity instanceof EncapsulatorBlockEntity blockEntity) {
                blockEntity.tick();
            }

        } : null;
    }
}
