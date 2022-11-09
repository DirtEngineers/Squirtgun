package net.dirtengineers.squirtgun.common.item.Squirtgun;

import com.mojang.blaze3d.platform.InputConstants;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.client.Keybinds;
import net.dirtengineers.squirtgun.client.TextUtility;
import net.dirtengineers.squirtgun.client.screens.ModScreens;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class SquirtgunItem extends BowItem {

//TODO:Change on loading new phial or firing slug
    private boolean statusChanged;
    private ChemicalPhial phial = null;

    public SquirtgunItem(Properties pProperties){
        super(pProperties);
        statusChanged = true;
    }

    private void PHIALLOADINGTEST(Level pLevel, Player pPlayer) {
//        if(phial == null){
            phial = (ChemicalPhial) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("squirtgun:hydrochloric_acid_phial"))).asItem();
            phial.loadFluid(new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("chemlib:hydrochloric_acid_fluid"))), 1000));
//            phial.loadFluid(new FluidStack(phial.getOptionalFluid().orElse(Fluids.EMPTY), 1000));
            statusChanged = true;
//        }
        if(pPlayer.getAbilities().instabuild && phial.getShotsAvailable() <= 0){
            phial.loadFluid(new FluidStack(phial.getOptionalFluid().orElse(Fluids.EMPTY), 1000));
            statusChanged = true;
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected) {
        if (!pLevel.isClientSide) {
            if(this.phial == null && statusChanged)
                loadFromNBT(pStack);
            if(this.statusChanged)
                setTag(pStack);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(
                pStack,
                pLevel,
                TextUtility.setAmmoHoverText(
                        this.phial != null ?
                                this.phial.getOptionalFluid() :
                                Optional.ofNullable(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(Constants.EMPTY_FLUID_NAME))),
                        this.getAmmoStatus(),
                        this,
                        pTooltipComponents),
                pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
//        PHIALLOADINGTEST(pLevel, pPlayer);

         ItemStack itemstack = pPlayer.getItemInHand(pHand);
        // Only perform the shift action
        if (pPlayer.isShiftKeyDown()) {
            if (!pLevel.isClientSide) {
                GunProperties.setCanFire(itemstack, true);
            }

            if (pLevel.isClientSide) {
                if (Keybinds.shiftClickGuiBinding.getKey() == InputConstants.UNKNOWN) {
                    ModScreens.openGunSettingsScreen(itemstack);
                    return InteractionResultHolder.pass(itemstack);
                }
            }

            // INTENTIONALLY LEFT IN. I DON'T HAVE THE TIME TO FIX THIS ISSUE ATM
            // @todo: migrate keybinding setting onto gadget so I can set a tag on the item
            return InteractionResultHolder.pass(itemstack);
        }

        boolean hasAmmo = hasAmmunition(pPlayer);

        net.minecraftforge.common.ForgeHooks.getProjectile(pPlayer, itemstack, hasAmmo ? new ItemStack(phial.getOrCreateGenericSlugItem()) : ItemStack.EMPTY);
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

            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;

            boolean hasAmmo = hasAmmunition(player);
            net.minecraftforge.common.ForgeHooks.getProjectile(pEntityLiving, pStack, hasAmmo ? new ItemStack(phial.getOrCreateGenericSlugItem()) : ItemStack.EMPTY);

            int i = getUseDuration(pStack) - pTimeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, hasAmmo || flag);
            if (i < 0) return;

//            Infinity slugs?
//            boolean bInfinityAmmo = bInstabuild || (itemstack.getItem() instanceof SquirtSlugItem && ((SquirtSlugItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));

            if (hasAmmo) {
                if (!pLevel.isClientSide) {
                    SquirtSlug slug = phial.makeSlugToFire(pLevel, player);
                    statusChanged = !((Player) pEntityLiving).getAbilities().instabuild;
                    if (slug.hasEffects()) slug.setBaseDamage(0D);
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
                        1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                );
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
        if (!pLevel.isClientSide) {
            GunProperties.setCanFire(pStack, true);
        }
    }

    private boolean hasAmmunition(Player pPlayer) {
        boolean result;
        if (phial == null) {
            if (pPlayer.getAbilities().instabuild) {
                ChemicalPhial newMag =
                        (ChemicalPhial)
                                Objects.requireNonNull(ForgeRegistries
                                        .ITEMS
                                        .getValue(new ResourceLocation("squirtgun:nitric_acid_phial")))
                                        .asItem();
                newMag.loadFluid(new FluidStack(newMag.getOptionalFluid().orElse(Fluids.EMPTY), 1000));
                phial = newMag;
                statusChanged = true;
                result = phial.hasAmmunition(pPlayer);
            } else
                result = false;
        } else
            result = phial.hasAmmunition(pPlayer);
        return result;
    }

    public Optional<Fluid> getFluid(){
        return (phial != null) ? phial.getOptionalFluid() : Optional.of(FluidStack.EMPTY.getFluid());
    }

    public String getAmmoStatus(){
        return (phial == null) ? new TranslatableContents("No Phial").getKey() : phial.getAmmoStatus();
    }

    public ChemicalPhial loadNewPhial(ChemicalPhial pPhial){
        ChemicalPhial outMag = phial;
        phial = pPhial;
        statusChanged = true;
        return outMag;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    private void loadFromNBT(ItemStack pStack) {
        CompoundTag stackTag = pStack.getOrCreateTag();
        if (Objects.requireNonNull(stackTag).contains(Constants.PHIAL_TYPE_TAG) && stackTag.contains(Constants.PHIAL_SHOTS_AVAILABLE_TAG)) {
            phial = (ChemicalPhial)
                    Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(
                                    new ResourceLocation(Squirtgun.MOD_ID + ":" + stackTag.getString(Constants.PHIAL_TYPE_TAG))
                            )
                    ).asItem();
            phial.loadFluid(new FluidStack(phial.getOptionalFluid().orElse(Fluids.EMPTY),
                    stackTag.getInt(Constants.PHIAL_SHOTS_AVAILABLE_TAG) * SquirtSlug.shotSize));
            pStack.setTag(stackTag);
            statusChanged = false;
        }
//        pStack.setTag(new CompoundTag());
    }

    private void setTag(ItemStack pStack) {
        CompoundTag tag = pStack.getOrCreateTag();
        if(phial != null) {
            tag.putString(Constants.PHIAL_TYPE_TAG, phial.toString());
            tag.putInt(Constants.PHIAL_SHOTS_AVAILABLE_TAG, phial.getShotsAvailable());
        }
        pStack.setTag(tag);
        statusChanged = false;
    }

    public static ItemStack getGun(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof SquirtgunItem)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof SquirtgunItem)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }
}