package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.util.AmmunitionContainer;
import net.dirtengineers.squirtgun.common.util.Common;
import net.dirtengineers.squirtgun.common.util.Text;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.dirtengineers.squirtgun.common.util.Common.Ammunition;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class SquirtgunItem extends BowItem {
    private final boolean generalTest = true;
    private final boolean restrictedFluidTest = true;
    private final boolean fluidRotationTest = true;
    private int testFluidRotationIndex = 0;
    private final List<String> myFluids = new ArrayList<>();
    private final AmmunitionContainer storage;

    public SquirtgunItem(Properties pProperties){
        super(pProperties);
        myFluids.add("chemlib:epinephrine");
        myFluids.add("chemlib:hydrochloric_acid");
        myFluids.add("chemlib:sulfuric_acid");
        myFluids.add("chemlib:nitric_acid");
        myFluids.add("chemlib:bromine");
        storage = new AmmunitionContainer(Common.SQUIRT_AMMUNITION_ONLY);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, Text.setAmmoHoverText(this.storage, Common.getFriendlyItemName(this), pTooltipComponents), pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (fluidRotationTest || restrictedFluidTest || generalTest)
            MAGAZINELOADINGTEST(pLevel, pPlayer);

        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        boolean hasAmmo = !this.storage.hasAmmunition(pPlayer);

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

            boolean hasAmmo = this.storage.hasAmmunition(player);

            int i = this.getUseDuration(pStack) - pTimeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, hasAmmo || flag);
            if (i < 0) return;

//            Infinity slugs?
//            boolean bInfinityAmmo = bInstabuild || (itemstack.getItem() instanceof SquirtSlugItem && ((SquirtSlugItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));

            if (!pLevel.isClientSide) {
                SquirtSlug slug = this.makeSlugToFire(pLevel, player);
                if(slug.hasEffects()) slug.setBaseDamage(0D);
                slug.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
                pLevel.addFreshEntity(slug);
            }
            pLevel.playSound(
                    null,
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

    public SquirtSlug makeSlugToFire(Level pLevel, LivingEntity pEntityLiving) {
        SquirtSlug slug = new SquirtSlug(pEntityLiving, pLevel, this.storage.getFluid().getFluid());
        return this.fillSlug(pEntityLiving, slug);
    }

    public SquirtSlug fillSlug(LivingEntity pEntityLiving, SquirtSlug pSlug) {
        if (pEntityLiving instanceof Player player) {
            if (this.storage.hasAmmunition(player)) {
                pSlug.setAmmoType(this.storage.getFluid().getFluid());
                if (!player.getAbilities().instabuild)
                    this.storage.drainContainer(SquirtSlug.shotSize, EXECUTE);
            }
        }
        return pSlug;
    }

    public AmmunitionContainer getInnerStorage(){
        return this.storage;
    }

//    private SquirtMagazineItem loadNewSquirtMagazine(SquirtMagazineItem pMagazine) {
//        SquirtMagazineItem outMagazine = new SquirtMagazineItem(magazine);
//        magazine = new SquirtMagazineItem(pMagazine);
//        return outMagazine;
//    }
//
//    public ItemStack loadNewMagazine(ItemStack pItemStack) {
//        if (pItemStack.getItem() instanceof SquirtMagazineItem pMag) {
//            SquirtMagazineItem tempMag = new SquirtMagazineItem(magazine);
//            magazine = new SquirtMagazineItem(pMag);
//            pItemStack = new ItemStack(new SquirtMagazineItem(tempMag), 1);
//        }
//        return pItemStack;
//    }
//
//    public ItemStack unloadSquirtMagazine(){
//        SquirtMagazineItem mag = new SquirtMagazineItem(magazine);
//        magazine.unload();
//        return new ItemStack(mag, 1);
//    }

    private void MAGAZINELOADINGTEST(Level pLevel, Player pPlayer) {
//        if (pLevel.isClientSide) {
        if (restrictedFluidTest)
            while (!myFluids.contains(((Fluid) Ammunition.keySet().toArray()[testFluidRotationIndex]).getFluidType().toString()))
                testFluidRotationIndex = indexRotation(testFluidRotationIndex);
        if (fluidRotationTest) {
            this.storage.setFluid(new FluidStack((Fluid) Ammunition.keySet().toArray()[testFluidRotationIndex], 750));
            testFluidRotationIndex = indexRotation(testFluidRotationIndex);
//            }
//            for (ItemStack itemStack : pPlayer.getInventory().items) {
//                if(itemStack.getItem() instanceof SquirtMagazine magazine)
////                    magazine.drainContainer(magazine.getFluidLevel(), IFluidHandler.FluidAction.EXECUTE);
//                    if(magazine.getFluidLevel() == 0) {
//                        magazine.setFluid(new FluidStack((Fluid) Ammunition.keySet().toArray()[testFluidRotationIndex], 500));
//                        itemStack.setCount(1);
//                        break;
//                    }
//            }
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
}