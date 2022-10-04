package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.util.Common;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.SQUIRTGUN_TAB;
import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.SQUIRTMAGAZINE;
import static net.dirtengineers.squirtgun.common.util.Common.Ammunition;

public class SquirtgunItem extends BowItem {

    private int testFluidRotationIndex = 0;

    private final List<String> myFluids = new ArrayList<>();

    private SquirtMagazine magazine = (SquirtMagazine) SQUIRTMAGAZINE.get();

    public SquirtgunItem(){
        super(new Item.Properties().tab(SQUIRTGUN_TAB));
//        myFluids.add("chemlib:epinephrine");
        myFluids.add("chemlib:hydrochloric_acid");
//        myFluids.add("chemlib:sulfuric_acid");
//        myFluids.add("chemlib:nitric_acid");
        myFluids.add("chemlib:bromine");
    }

    private String getFriendlyName(){
        return I18n.get(this.getDescriptionId());
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, Common.setAmmoHoverText(this.magazine, getFriendlyName(), pTooltipComponents), pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {

        MAGAZINELOADINGTEST(pLevel);

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
                if(slug.hasEffects()) slug.setBaseDamage(0D);
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

    private SquirtMagazine loadNewSquirtMagazine(SquirtMagazine pMagazine) {
        SquirtMagazine outMagazine = new SquirtMagazine(this.magazine);
        this.magazine = new SquirtMagazine(pMagazine);
        return outMagazine;
    }

    public ItemStack unloadSquirtMagazine(){
        SquirtMagazine mag = new SquirtMagazine(this.magazine);
        this.magazine.emptyMagazine();
        return new ItemStack(mag, 1);
    }

    private void MAGAZINELOADINGTEST(Level pLevel) {
        if (pLevel.isClientSide) {
            while (!myFluids.contains(((Fluid) Ammunition.keySet().toArray()[testFluidRotationIndex]).getFluidType().toString()))
                testFluidRotationIndex = indexRotation(testFluidRotationIndex);
            this.magazine.setFluid(new FluidStack((Fluid) Ammunition.keySet().toArray()[testFluidRotationIndex], 750));
            testFluidRotationIndex = indexRotation(testFluidRotationIndex);
        }
    }

    private int indexRotation(int index) {
        index++;
        if (index >= Common.Ammunition.size()) index = 0;
        return index;
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
//        Format will be Shots Available/Shots Max
//        int magCapacity = (int) Math.floor(magazine.getFluidCapacity()/SquirtSlug.shotSize);
//        int magFluidLevel = (int) Math.floor(magazine.getFluidLevel()/SquirtSlug.shotSize);

    }
}