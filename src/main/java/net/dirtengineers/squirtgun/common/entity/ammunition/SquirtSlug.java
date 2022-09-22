package net.dirtengineers.squirtgun.common.entity.ammunition;

import net.dirtengineers.squirtgun.common.registry.EntityRegistration;
import net.dirtengineers.squirtgun.common.registry.SoundEventRegistration;
import net.dirtengineers.squirtgun.common.util.Common;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class SquirtSlug extends AbstractArrow {
    //    DynamicFluidContainerModel
    public static final int shotSize = 100;
    private int life;

    private Fluid ammoType;

    public SquirtSlug(Level pLevel) {
        this(EntityRegistration.SQUIRT_SLUG.get(), pLevel);
    }

    public SquirtSlug(LivingEntity pShooter, Level pLevel, Fluid pFluid) {
        super(EntityRegistration.SQUIRT_SLUG.get(), pShooter, pLevel);
        this.ammoType = pFluid;
    }

    public SquirtSlug(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SquirtSlug(EntityType<? extends AbstractArrow> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public SquirtSlug(EntityType<? extends AbstractArrow> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    public void setAmmoType(Fluid pAmmoType) {
        this.ammoType = pAmmoType;
    }

    public Fluid getAmmoType() {
        return this.ammoType;
    }


    @Override
    public void playerTouch(Player pEntity) {
    }

    @Override
    public ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean tryPickup(Player pPlayer) {
        return false;
    }

    @Override
    public void tickDespawn() {
        ++this.life;
        if (this.life >= 10) {
            this.discard();
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.setSoundEvent(SoundEventRegistration.SQUIRT_SLUG_HIT.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.life = 0;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEventRegistration.SQUIRT_SLUG_HIT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.ammoType != null) {
            pCompound.putString("Fluid", ammoType.getFluidType().toString());
        }

//        if (this.fixedColor) {
//            pCompound.putInt("Color", this.getColor());
//        }
//
//        if (!this.effects.isEmpty()) {
//            ListTag listtag = new ListTag();
//
//            for(MobEffectInstance mobeffectinstance : this.effects) {
//                listtag.add(mobeffectinstance.save(new CompoundTag()));
//            }
//
//            pCompound.put("CustomPotionEffects", listtag);
//        }
    }


    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Fluid"))
            this.ammoType = Common.getAmmunitionFluidByName(pCompound.getString("Fluid"));

//        for(MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(pCompound)) {
//            this.addEffect(mobeffectinstance);
//        }
//
//        if (pCompound.contains("Color", 99)) {
//            this.setFixedColor(pCompound.getInt("Color"));
//        } else {
//            this.updateColor();
//        }
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
