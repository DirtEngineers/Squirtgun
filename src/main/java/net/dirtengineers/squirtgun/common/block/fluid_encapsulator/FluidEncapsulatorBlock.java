package net.dirtengineers.squirtgun.common.block.fluid_encapsulator;

import com.smashingmods.alchemylib.api.block.AbstractProcessingBlock;
import net.dirtengineers.squirtgun.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidEncapsulatorBlock extends AbstractProcessingBlock {
    public static final VoxelShape base = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    public static final VoxelShape rest = Block.box(2.0, 1.0, 2.0, 14.0, 16.0, 14.0);
    public static final VoxelShape SHAPE;

    public FluidEncapsulatorBlock() {
        super(FluidEncapsulatorBlockEntity::new);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        //TODO: Address atomizerEnergyPerTick
        pTooltip.add(MutableComponent.create(new TranslatableContents("tooltip.squirtgun.energy_requirement", Config.Common.encapsulatorEnergyPerTick.get())));
    }

    //TODO: Figure this out
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
//        if (!pLevel.isClientSide()) {
//            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
//            boolean interactionSuccessful = true;
//            if (blockEntity instanceof FluidEncapsulatorBlockEntity) {
//                interactionSuccessful = ((FluidEncapsulatorBlockEntity)blockEntity).onBlockActivated(pLevel, pPos, pPlayer, pHand);
//            }
//            if (!interactionSuccessful) {
//                NetworkHooks.openScreen((ServerPlayer)pPlayer, (FluidEncapsulatorBlockEntity)blockEntity, pPos);
//            }
//            return InteractionResult.CONSUME;
//        } else {
//            return InteractionResult.SUCCESS;
//        }
        return InteractionResult.CONSUME;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (level, pBlockPos, pBlockState, pBlockEntity) -> {
            if (pBlockEntity instanceof FluidEncapsulatorBlockEntity blockEntity) {
                blockEntity.tick();
            }

        } : null;
    }

    static {
        SHAPE = Shapes.or(base, rest);
    }
}
