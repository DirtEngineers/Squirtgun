package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.registry.ItemRegistry;
import net.dirtengineers.squirtgun.common.util.Common;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.function.Predicate;

public class SquirtgunItem extends BowItem {

    private SquirtMagazine magazine;

    public SquirtgunItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return Common.SQUIRT_SLUG_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        displayAmmunitionAmount(pLevel);

        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean hasAmmo = !getProjectileItem(itemstack, pPlayer).isEmpty();

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, hasAmmo);
        if (ret != null) return ret;

        if (!pPlayer.getAbilities().instabuild && !hasAmmo) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    private ItemStack getProjectileItem(ItemStack pShootable, Player pPlayer) {
        if(pPlayer.getAbilities().instabuild) magazine = (SquirtMagazine) ItemRegistry.SQUIRTMAGAZINE.get();

        ItemStack itemstack = magazine.hasAmmunition() || pPlayer.getAbilities().instabuild ?
                new ItemStack(ItemRegistry.SQUIRTSLUGITEM.get()) :
                ItemStack.EMPTY;

        return ForgeHooks.getProjectile(pPlayer, pShootable, itemstack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
            ItemStack itemstack = getProjectileItem(pStack, player);

            int i = this.getUseDuration(pStack) - pTimeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof SquirtSlugItem && ((SquirtSlugItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));
            if (!pLevel.isClientSide) {
                SquirtSlugItem slugItem = (SquirtSlugItem)(itemstack.getItem() instanceof SquirtSlugItem ? itemstack.getItem() : ItemRegistry.SQUIRTSLUGITEM.get());
                SquirtSlug slug  =  slugItem.createSlug(pLevel, pEntityLiving);
                slug = magazine.fillSlug(slug);
                slug.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);

                pLevel.addFreshEntity(slug);
            }

            pLevel.playSound(
                    (Player)null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ARROW_SHOOT,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) +  0.5F
            );

            if (!flag1 && !player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    private void displayAmmunitionAmount(Level pLevel){
        if(pLevel == Minecraft.getInstance().level){

        }
//        Format will be Shots Available/Shots Max
//        int magCapacity = (int) Math.floor(magazine.getFluidCapacity()/SquirtSlug.shotSize);
//        int magFluidLevel = (int) Math.floor(magazine.getFluidLevel()/SquirtSlug.shotSize);

    }
}