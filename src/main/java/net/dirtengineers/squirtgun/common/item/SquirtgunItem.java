package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.util.Common;
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
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.SQUIRTGUN_TAB;
import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.SQUIRTMAGAZINE;

public class SquirtgunItem extends BowItem {

    private final SquirtMagazine magazine = (SquirtMagazine) SQUIRTMAGAZINE.get();

    public SquirtgunItem(){
        super(new Item.Properties().tab(SQUIRTGUN_TAB));
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {

        MAGAZINELOADINGTEST();

        displayAmmunitionAmount(pLevel);

        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        boolean hasAmmo = !hasAmmunition(pPlayer);

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, hasAmmo);
        if (ret != null) return ret;

        if (!pPlayer.getAbilities().instabuild && !hasAmmo) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            boolean bInstabuild = player.getAbilities().instabuild;
            boolean bInfinityAmmo = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
            boolean flag = bInstabuild || bInfinityAmmo;

            boolean hasAmmo = hasAmmunition(player);

            int i = this.getUseDuration(pStack) - pTimeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, hasAmmo || flag);
            if (i < 0) return;

//            Infinity slugs?
//            boolean bInfinityAmmo = bInstabuild || (itemstack.getItem() instanceof SquirtSlugItem && ((SquirtSlugItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));

            if (!pLevel.isClientSide) {
                SquirtSlug slug = magazine.makeSlugToFire(pLevel, player);
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

            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    private boolean hasAmmunition(Player pPlayer){
        return magazine.hasAmmunition(pPlayer);
    }

    private ItemStack loadSquirtMagazine(ItemStack pStack){
        if(pStack.getItem() instanceof SquirtMagazine pMag) {
            this.magazine.emptyMagazine();
            FluidStack stack = pMag.emptyMagazine();
            this.magazine.setFluid(stack);
            pStack.shrink(1);
        }
        return pStack;
    }

    public ItemStack unloadSquirtMagazine(){
        SquirtMagazine mag = new SquirtMagazine(this.magazine);
        this.magazine.emptyMagazine();
        return new ItemStack(mag, 1);
    }

    private void MAGAZINELOADINGTEST(){
        this.magazine.setFluid(new FluidStack(Common.AmmunitionFluids.get(0), 750));
//        SquirtMagazine TESTMAG = (SquirtMagazine)SQUIRTMAGAZINE.get().asItem();
//        TESTMAG.setFluid(new FluidStack(Common.AmmunitionFluids.get(0), 750));
//        this.loadFromMagazine(new ItemStack(this.magazine));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    private void displayAmmunitionAmount(Level pLevel){
//        if(pLevel == Minecraft.getInstance().level){
//
//        }
//        Format will be Shots Available/Shots Max
//        int magCapacity = (int) Math.floor(magazine.getFluidCapacity()/SquirtSlug.shotSize);
//        int magFluidLevel = (int) Math.floor(magazine.getFluidLevel()/SquirtSlug.shotSize);

    }
}