package net.dirtengineers.squirtgun.common.block.fluid_encapsulator;

import com.smashingmods.alchemistry.api.block.AbstractAlchemistryBlock;
import com.smashingmods.alchemistry.common.block.liquifier.LiquifierBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

public class FluidEncapsulatorBlock extends AbstractAlchemistryBlock {
    public static final VoxelShape base = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    public static final VoxelShape rest = Block.box(2.0, 1.0, 2.0, 14.0, 16.0, 14.0);
    public static final VoxelShape SHAPE;

    public FluidEncapsulatorBlock() {
        super(FluidEncapsulatorBlockEntity::new);
    }

    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return SHAPE;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            boolean interactionSuccessful = true;
            if (blockEntity instanceof LiquifierBlockEntity) {
                interactionSuccessful = ((LiquifierBlockEntity)blockEntity).onBlockActivated(pLevel, pPos, pPlayer, pHand);
            }

            if (!interactionSuccessful) {
                NetworkHooks.openScreen((ServerPlayer)pPlayer, (LiquifierBlockEntity)blockEntity, pPos);
            }

            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (level, pBlockPos, pBlockState, pBlockEntity) -> {
            if (pBlockEntity instanceof LiquifierBlockEntity blockEntity) {
                blockEntity.tick();
            }

        } : null;
    }

    static {
        SHAPE = Shapes.or(base, rest);
    }
}
