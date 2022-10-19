package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.util.Common;
import net.dirtengineers.squirtgun.common.util.Text;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SquirtMagazineItem extends Item {

    private int ammunitionAmount = 0;
    private  int Capacity = 10;
    private Fluid fluid;
    private int AmmunitionAvailable = 0;

    public SquirtMagazineItem(Properties properties) {
        super(properties);
    }

    public int loadFluid(FluidStack pFluidStack){
        this.fluid = pFluidStack.getFluid();
        return Math.max(pFluidStack.getAmount() - this.Capacity, 0);
    }

    /*
    Not right for now
     */
    public MagazineReturnValue load(FluidStack pFluidStack) {
        FluidStack outStack = this.drain();
        return new MagazineReturnValue(pFluidStack, this.loadFluid(pFluidStack));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, Text.setAmmoHoverText(this.fluid, this.getAmmoStatus(), Common.getFriendlyItemName(this), pTooltipComponents), pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public String getAmmoStatus(){ return this.AmmunitionAvailable + "/" +  this.Capacity; }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    TANK UTILITIES

    private FluidStack drain() {
        FluidStack stack = FluidStack.EMPTY;
        this.fluid = stack.getFluid();
        this.AmmunitionAvailable = 0;
        return stack;
    }

//    public int setFluid(FluidStack pFluidStack) {
//        this.drain();
//        return this.storage.fill(pFluidStack, EXECUTE);
//    }

    public static class MagazineReturnValue {
        FluidStack stack;
        int consumed;

        MagazineReturnValue(FluidStack pFluidStack, int amountConsumed) {
            stack = pFluidStack;
            consumed = amountConsumed;
        }

        public FluidStack getFluidStack() {
            return this.stack;
        }

        public int getAmountConsumed() {
            return this.consumed;
        }
    }
}



