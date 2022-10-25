package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.common.util.TextUtility;
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
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;


public class SquirtgunItem extends BowItem {
    private final String MAGAZINE_TYPE_TAG = Squirtgun.MOD_ID + ".magazine_type";
    private final String MAGAZINE_SHOTS_TAG = Squirtgun.MOD_ID + ".magazine_shots";

//    Change on loading new magazine or firing slug
    private boolean statusChanged;
    private BaseSquirtMagazine magazine = null;

    public SquirtgunItem(Properties pProperties){
        super(pProperties);
        statusChanged = true;
    }

    private void MAGAZINELOADINGTEST(Level pLevel, Player pPlayer) {
        if(this.magazine == null){
            magazine = (BaseSquirtMagazine) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("squirtgun:hydrochloric_acid_magazine"))).asItem();
            magazine.setFluid();
            magazine.loadFluid(new FluidStack(magazine.getFluid(), 1000));
            this.statusChanged = true;
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected) {
        if (!pLevel.isClientSide) {
            if(this.magazine == null && statusChanged)
                this.loadFromNBT(pStack);
            if(this.statusChanged)
                this.setTag(pStack);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(
                pStack,
                pLevel,
                TextUtility.setAmmoHoverText(
                        this.magazine != null ? this.magazine.getFluid() : ForgeRegistries.FLUIDS.getValue(new ResourceLocation(TextUtility.EMPTY_FLUID_NAME)),
                        this.getAmmoStatus(),
                        ItemRegistration.getFriendlyItemName(this),
                        pTooltipComponents),
                pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {

        MAGAZINELOADINGTEST(pLevel, pPlayer);

        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        boolean hasAmmo = this.hasAmmunition(pPlayer);

        net.minecraftforge.common.ForgeHooks.getProjectile(pPlayer, itemstack, hasAmmo ? new ItemStack(this.magazine.getGenericSlugItem()) : ItemStack.EMPTY);
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

            boolean hasAmmo = this.hasAmmunition(player);
            net.minecraftforge.common.ForgeHooks.getProjectile(pEntityLiving, pStack, hasAmmo ? new ItemStack(this.magazine.getGenericSlugItem()) : ItemStack.EMPTY);

            int i = this.getUseDuration(pStack) - pTimeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, hasAmmo || flag);
            if (i < 0) return;

//            Infinity slugs?
//            boolean bInfinityAmmo = bInstabuild || (itemstack.getItem() instanceof SquirtSlugItem && ((SquirtSlugItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));

            if(hasAmmo) {
            if (!pLevel.isClientSide) {
                SquirtSlug slug = this.magazine.makeSlugToFire(pLevel, player);
                this.statusChanged = true;
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
    }

    private boolean hasAmmunition(Player pPlayer) {
        boolean result;
        if (this.magazine == null)
            if (pPlayer.getAbilities().instabuild) {
                BaseSquirtMagazine newMag = (BaseSquirtMagazine) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("squirtgun:nitric_acid_magazine"))).asItem();
                newMag.setFluid();
                newMag.loadFluid(new FluidStack(newMag.getFluid(), 1000));
                this.magazine = newMag;
                statusChanged = true;
                result = this.magazine.hasAmmunition(pPlayer);
            } else
                result = false;
        else
            result = this.magazine.hasAmmunition(pPlayer);
        return result;
    }

    public Fluid getFluid(){
        return (this.magazine != null) ? this.magazine.getFluid() : FluidStack.EMPTY.getFluid();
    }

    public String getAmmoStatus(){
        return (this.magazine == null) ? new TranslatableContents("No Magazine").getKey() : this.magazine.getAmmoStatus();
    }

    public BaseSquirtMagazine loadNewMagazine(BaseSquirtMagazine pMagazine){
        BaseSquirtMagazine outMag = this.magazine;
        this.magazine = pMagazine;
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
        if (Objects.requireNonNull(stackTag).contains(MAGAZINE_TYPE_TAG) && stackTag.contains(MAGAZINE_SHOTS_TAG)) {
            this.magazine = (BaseSquirtMagazine)
                    Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(
                                    new ResourceLocation(Squirtgun.MOD_ID + ":" + stackTag.getString(MAGAZINE_TYPE_TAG))
                            )
                    ).asItem();
            this.magazine.setFluid();
            magazine.loadFluid(new FluidStack(
                    this.magazine.getFluid(),
                    stackTag.getInt(MAGAZINE_SHOTS_TAG) * SquirtSlug.shotSize));
            pStack.setTag(stackTag);
            statusChanged = false;
        }
//        pStack.setTag(new CompoundTag());
    }

    private void setTag(ItemStack pStack) {
        CompoundTag tag = pStack.getOrCreateTag();
        if(this.magazine != null) {
            tag.putString(MAGAZINE_TYPE_TAG, this.magazine.toString());
            tag.putInt(MAGAZINE_SHOTS_TAG, this.magazine.getShotsAvailable());
        }
        pStack.setTag(tag);
        this.statusChanged = false;
    }
}