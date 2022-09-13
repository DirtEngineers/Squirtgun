package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static net.dirtengineers.squirtgun.common.registry.ItemRegistry.SQUIRTGUNITEMS;
import static net.dirtengineers.squirtgun.common.util.Common.SQUIRT_MAGAZINE_ONLY;

public class SquirtgunItem extends BowItem {

    private SquirtCartridge magazine;

    public SquirtgunItem(Properties pProperties) { super(pProperties); }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return SQUIRT_MAGAZINE_ONLY;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * onItemUse ??.
     */
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
//        CHECK AMMO AVAILABILITY and display
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     *
     * !! Modify code from BowItem
     */
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
//        super.releaseUsing(pStack, pLevel, pEntityLiving, pTimeLeft);
        if (pEntityLiving instanceof Player player) {
            boolean flag = player.getAbilities().instabuild;

//          Get a slug based upon the fluid in the cartridge
            SquirtSlug squirtSlug = getSlugItem(pLevel, pEntityLiving);
        }
    }

//    Currently hardcoded  Later, get it from the magazine
    private @NotNull SquirtSlug getSlugItem(Level pLevel, LivingEntity pShooter) {
        SquirtSlugItem squirtSlugItem = (SquirtSlugItem) SQUIRTGUNITEMS.getEntries().stream().map(RegistryObject::get)
                .map((item) -> (SquirtSlugItem) item);
        return squirtSlugItem.createSquirtSlug(pLevel, new ItemStack(squirtSlugItem), pShooter);
    }


    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }


    //As in the BowItem
    public int getDefaultProjectileRange() { return 15; }

//    Rework enchantment values
    public int getEnchantmentValue() {
        return 1;
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }

}