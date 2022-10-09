package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.util.AmmunitionContainer;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class SquirtMagazineItem extends Item {

    private final AmmunitionContainer storage;

    public SquirtMagazineItem(Properties properties) {
        super(properties);
        storage = new AmmunitionContainer(Common.SQUIRT_AMMUNITION_ONLY);
    }

//    public SquirtMagazineItem(int pCapacity) {
//        super(new Item.Properties().tab(SQUIRTGUN_TAB));
//        storage = new AmmunitionContainer(Common.fluidTankBaseCapacity, Common.SQUIRT_AMMUNITION_ONLY);
//        this.container = new FluidTank(pCapacity, Common.SQUIRT_AMMUNITION_ONLY);
//    }

//    public SquirtMagazineItem(SquirtMagazineItem pMag) {
//        super(new Item.Properties().tab(ItemRegistration.SQUIRTGUN_TAB));
//        this.container.setCapacity(pMag.getFluidCapacity());
//        this.container.fill(pMag.container.getFluid(), EXECUTE);
//    }

    public FluidStack unload() {
        return this.drain();
    }

    public MagazineReturnValue load(FluidStack pFluidStack) {
        FluidStack outStack = this.drain();
        return new MagazineReturnValue(outStack, this.setFluid(pFluidStack));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, Text.setAmmoHoverText(this.storage, Common.getFriendlyItemName(this), pTooltipComponents), pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    TANK UTILITIES

    private FluidStack drain() {
        return this.storage.drain(this.storage.getCapacity(), EXECUTE);
    }

    public int setFluid(FluidStack pFluidStack) {
        this.drain();
        return this.storage.fill(pFluidStack, EXECUTE);
    }

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



