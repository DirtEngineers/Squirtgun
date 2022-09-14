package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.dirtengineers.squirtgun.common.registry.EntityRegistry.SQUIRT_SLUG;

public class SquirtgunItem extends BowItem {

    private SquirtMagazine magazine;

    public SquirtgunItem(Properties pProperties) { super(pProperties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean flag = true; // temp hardcode!(magazine == null);

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, flag);
        if (ret != null) return ret;

        if (!pPlayer.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
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
        super.releaseUsing(pStack, pLevel, pEntityLiving, pTimeLeft);


    }

//    Currently hardcoded  Later, get it from the magazine
    private SquirtSlug getSlugItem(Level pLevel) {

        return (SquirtSlug)RegistryObject.create(SQUIRT_SLUG.getId(),ForgeRegistries.ENTITY_TYPES).getHolder().get().value().create(pLevel);

    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}