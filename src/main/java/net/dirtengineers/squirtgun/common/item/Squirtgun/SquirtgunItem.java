package net.dirtengineers.squirtgun.common.item.Squirtgun;

import com.mojang.blaze3d.platform.InputConstants;
import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.client.Keybinds;
import net.dirtengineers.squirtgun.client.screens.ModScreens;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.ChemicalPhial;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.dirtengineers.squirtgun.common.registry.SoundEventRegistration;
import net.dirtengineers.squirtgun.util.TextUtility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.CHEMICAL_FLUIDS;
import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.ammunitionChemicals;


public class SquirtgunItem extends BowItem {
    int shotsAvailable;
    int maxShots;
    int baseMaxShots = 10;
    Chemical chemical;
    private boolean statusChanged;

    public SquirtgunItem(Properties pProperties) {
        super(pProperties);
        maxShots = baseMaxShots;
        statusChanged = true;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected) {
        //ForgeRegistries.FLUIDS.getValue(new ResourceLocation("chemlib:hydrochloric_acid_fluid"))
        if (chemical == null && statusChanged)
            loadFromNBT(pStack);
        if (this.statusChanged)
            setTag(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(
                Component.literal(
                                TextUtility.buildAmmoStatus(
                                        chemical == null ?
                                                MutableComponent.create(new TranslatableContents(Constants.emptyFluidNameKey)).getString()
                                                : getAmmoStatus()
                                        , MutableComponent.create(new TranslatableContents(this.getDescriptionId())).getString()))
                        .withStyle(Constants.HOVER_TEXT_STYLE));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        // Only perform the shift action
        if (pPlayer.isShiftKeyDown()) {
            if (!pLevel.isClientSide) {
                GunProperties.setCanFire(itemstack, true);
            }

            if (pLevel.isClientSide) {
                if (Keybinds.shiftClickGuiBinding.getKey() == InputConstants.UNKNOWN) {
                    ModScreens.openGunSettingsScreen();
                    return InteractionResultHolder.pass(itemstack);
                }
            }

            // INTENTIONALLY LEFT IN. I DON'T HAVE THE TIME TO FIX THIS ISSUE ATM
            // @todo: migrate keybinding setting onto gadget so I can set a tag on the item
            return InteractionResultHolder.pass(itemstack);
        }

        boolean hasAmmo = hasAmmunition(pPlayer);

        net.minecraftforge.common.ForgeHooks.getProjectile(pPlayer, itemstack, hasAmmo ? new ItemStack(ItemRegistration.SQUIRTSLUGITEM.get()) : ItemStack.EMPTY);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, hasAmmo);
        if (ret != null) return ret;

        if (!pPlayer.getAbilities().instabuild && !hasAmmo) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            pLevel.playSound(
                    pPlayer,
                    pPlayer.getX(),
                    pPlayer.getY(),
                    pPlayer.getZ(),
                    SoundEventRegistration.GUN_USE.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
            );
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {

            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;

            boolean hasAmmo = hasAmmunition(player);
            net.minecraftforge.common.ForgeHooks.getProjectile(pEntityLiving, pStack, hasAmmo ? new ItemStack(ItemRegistration.SQUIRTSLUGITEM.get()) : ItemStack.EMPTY);

            int i = getUseDuration(pStack) - pTimeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, hasAmmo || flag);
            if (i < 0) return;

//            Infinity slugs?
//            boolean bInfinityAmmo = bInstabuild || (itemstack.getItem() instanceof SquirtSlugItem && ((SquirtSlugItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));

            if (hasAmmo) {
                if (!pLevel.isClientSide) {
                    SquirtSlug slug = makeSlugToFire(pLevel, player);
                    if (slug != null) {
                        if (slug.hasEffects()) {
                            slug.setBaseDamage(0D);
                        }
                        slug.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
                        pLevel.addFreshEntity(slug);
                    }
                }
                pLevel.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEventRegistration.GUN_FIRE.get(),
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

    private SquirtSlug makeSlugToFire(Level pLevel, LivingEntity pEntityLiving) {
        if ((chemical == null) || !hasAmmunition((Player) pEntityLiving)) {
            return null;
        }
        consumeAmmunition((Player) pEntityLiving);
        return new SquirtSlug(pEntityLiving, pLevel, CHEMICAL_FLUIDS.get(this.chemical), chemical);
    }

    private void consumeAmmunition(Player pPlayer) {
        if (!pPlayer.getAbilities().instabuild) {
            shotsAvailable--;
            statusChanged = true;
        }
    }

    private boolean hasAmmunition(Player pPlayer) {
//        if (pPlayer.getAbilities().instabuild && chemical == null) {
//            this.loadNewPhial(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("item.squirtgun.hydrochloric_acid_phial")), 1));
//            chemical = (Chemical) ForgeRegistries.ITEMS.getValue(new ResourceLocation("chemlib:hydrochloric_acid"));
//            shotsAvailable = maxShots;
//            statusChanged = true;
//        }
        return shotsAvailable > 0 || pPlayer.getAbilities().instabuild;
    }

    public Chemical getChemical() {
        return chemical;
    }

    public String getAmmoStatus() {
        return (chemical == null) ? "---" : String.format("%s/%s", shotsAvailable, maxShots);
    }

    public ItemStack loadNewPhial(ItemStack pStack) {
        if (pStack.isEmpty() || !(pStack.getItem() instanceof ChemicalPhial)) {
            return pStack;
        }
        if(((ChemicalPhial) pStack.getItem()).getChemical() == chemical) {
            return pStack;
        }
        if (isChemicalValid(((BasePhial) pStack.getItem()).getChemical())) {
            this.chemical = ((BasePhial) pStack.getItem()).getChemical();
            shotsAvailable = Math.min(((BasePhial) pStack.getItem()).getShotsAvailable(), maxShots);
            statusChanged = true;
            return new ItemStack(ForgeRegistries.ITEMS.getValue(ItemRegistration.PHIAL.getId()), 1);
        } else {
            return pStack;
        }
    }

    private boolean isChemicalValid(Chemical chemical) {
        return ammunitionChemicals.contains(chemical) || chemical == null;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    private void loadFromNBT(ItemStack pStack) {
        CompoundTag stackTag = pStack.getOrCreateTag();
        shotsAvailable = (stackTag.contains(Constants.SHOTS_AVAILABLE_TAG)
                && !Objects.requireNonNull(stackTag.get(Constants.SHOTS_AVAILABLE_TAG)).getAsString().equals(""))
                ? stackTag.getInt(Constants.SHOTS_AVAILABLE_TAG)
                : 0;
        maxShots = (stackTag.contains(Constants.MAX_SHOTS_TAG)
                && !Objects.requireNonNull(stackTag.get(Constants.MAX_SHOTS_TAG)).getAsString().equals(""))
                ? stackTag.getInt(Constants.MAX_SHOTS_TAG)
                : ((SquirtgunItem) pStack.getItem()).baseMaxShots;
        chemical = (stackTag.contains(Constants.CHEMICAL_TAG)
                && !Objects.requireNonNull(stackTag.get(Constants.CHEMICAL_TAG)).getAsString().equals(""))
                ? (Chemical) ForgeRegistries.ITEMS.getValue(new ResourceLocation(stackTag.getString(Constants.CHEMICAL_TAG)))
                : null;
    }

    private void setTag(ItemStack pStack) {
        CompoundTag tag = pStack.getOrCreateTag();
        tag.putInt(Constants.SHOTS_AVAILABLE_TAG, shotsAvailable);
        tag.putInt(Constants.MAX_SHOTS_TAG, maxShots);
        tag.putString(Constants.CHEMICAL_TAG, chemical != null ? String.format("%s:%s", chemical.getClass().getModule().getName(), chemical.asItem()) : "");
        pStack.setTag(tag);
        statusChanged = false;
        //pStack.setTag(new CompoundTag());
    }

    public static ItemStack getGun(Player player) {
        ItemStack heldItem = ItemStack.EMPTY;
        if (player != null) {
            heldItem = player.getMainHandItem();
            if (!(heldItem.getItem() instanceof SquirtgunItem)) {
                heldItem = player.getOffhandItem();
                if (!(heldItem.getItem() instanceof SquirtgunItem)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return heldItem;
    }
}