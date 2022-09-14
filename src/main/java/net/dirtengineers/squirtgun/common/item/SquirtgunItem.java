package net.dirtengineers.squirtgun.common.item;

import com.mojang.blaze3d.platform.GlStateManager;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import static net.dirtengineers.squirtgun.common.item.SquirtMagazine.EMPTY;

public class SquirtgunItem extends BowItem {

    private final SquirtMagazine magazine = EMPTY;

    public SquirtgunItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        InteractionResultHolder<ItemStack> result;
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean pHasAmmo = true; // temp hardcodehasAmmunition();

        displayAmmunitionAmount();

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, pHasAmmo);
        if (ret != null) {
            result = ret;
        } else if (!pPlayer.getAbilities().instabuild && !pHasAmmo) {
            result = InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            result = InteractionResultHolder.consume(itemstack);
        }

        return result;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * onItemUse ??.
     **/
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
////        CHECK AMMO AVAILABILITY and display
//            pPlayer.startUsingItem(pHand);
//            return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
//    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     *
     * !! Modify code from BowItem
     */
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {

        if (pEntityLiving instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
            boolean hasMagazine = magazine != EMPTY;

            if(hasMagazine) {
                // Fire the projectile, damn it!
            }
        }
    }


    /**
     * returns the action that specifies what animation to play when the items is being used
     */
//    public UseAnim getUseAnimation(ItemStack pStack) {
//        return UseAnim.BOW;
//    }
//
    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    private boolean hasAmmunition(){
        if(magazine == EMPTY)
            return false;
        else return magazine.getFluidLevel() >= SquirtSlug.shotSize;
    }

    private void displayAmmunitionAmount(){
//        Format will be Shots Available/Shots Max
        int magCapacity = (int) Math.floor(magazine.getFluidCapacity()/SquirtSlug.shotSize);
        int magFluidLevel = (int) Math.floor(magazine.getFluidLevel()/SquirtSlug.shotSize);

    }
}