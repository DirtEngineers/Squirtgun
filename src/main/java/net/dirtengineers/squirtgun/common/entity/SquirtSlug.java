package net.dirtengineers.squirtgun.common.entity;

import com.google.common.collect.Sets;
import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.registry.EntityRegistration;
import net.dirtengineers.squirtgun.registry.SoundEventRegistration;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

import static net.dirtengineers.squirtgun.registry.ItemRegistration.CHEMICAL_FLUIDS;


public class SquirtSlug extends AbstractArrow implements IEntityAdditionalSpawnData {

    private double baseDamage = 0.25D;
    private int life;
    //TODO: consider removing fluid
    private Fluid fluid;
    private Chemical chemical;
    private Potion potion;
    private final int maxGroundTime = 10;
    private static final int NO_EFFECT_COLOR = -1;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR =
            SynchedEntityData.defineId(SquirtSlug.class, EntityDataSerializers.INT);
    private final Set<MobEffectInstance> effects = Sets.newHashSet();

    public SquirtSlug(LivingEntity pShooter, Level pLevel, Chemical pChemical) {
        super(EntityRegistration.SQUIRT_SLUG.get(), pShooter, pLevel);
        this.potion = null;
        this.chemical = pChemical;
        this.fluid = CHEMICAL_FLUIDS.get(chemical);
        setEffects();
    }

    public SquirtSlug(LivingEntity pShooter, Level pLevel, String pPotionKey) {
        super(EntityRegistration.SQUIRT_SLUG.get(), pShooter, pLevel);
        this.chemical = null;
        this.fluid = null;
        this.potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(pPotionKey));
        setEffects();
    }

    public SquirtSlug(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void setEffects() {
        this.effects.clear();
        if (this.chemical != null) {
            for (MobEffectInstance effect : this.chemical.getEffects()) {
                this.effects.add(new MobEffectInstance(effect));
            }
        }
        if (this.potion != null) {
            for (MobEffectInstance effect : this.potion.getEffects()) {
                this.effects.add(new MobEffectInstance(effect));
            }
        }
        this.updateColor();
    }

    public void tick() {
        super.tick();

        if (this.level.isClientSide)
            this.makeParticle(this.inGround ? 1 : 2);
        else
            if (this.inGround && this.inGroundTime != 0 && !this.effects.isEmpty()) {
                this.level.broadcastEntityEvent(this, (byte) 0);
                this.effects.clear();
                this.entityData.set(ID_EFFECT_COLOR, NO_EFFECT_COLOR);
            }
    }

    protected void doPostHurtEffects(LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
        Entity entity = this.getEffectSource();
        if(chemical != null) {
            pLiving.removeAllEffects();
        } else {
            for(MobEffectInstance mobeffectinstance : this.effects) {
                pLiving.addEffect(
                        new MobEffectInstance(mobeffectinstance.getEffect(),
                                Math.max(mobeffectinstance.getDuration() / 8, 1),
                                mobeffectinstance.getAmplifier(),
                                mobeffectinstance.isAmbient(),
                                mobeffectinstance.isVisible()),
                        entity);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 0) {
            makeParticle(0);
        } else {
            super.handleEntityEvent(pId);
        }
    }

    private void makeParticle(int pParticleAmount) {
        int i = this.getColor();
        if (i != -1 && pParticleAmount > 0) {
            double RED = (double)(i >> 16 & 255) / 255.0D;
            double GREEN = (double)(i >> 8 & 255) / 255.0D;
            double BLUE = (double)(i & 255) / 255.0D;

            for(int j = 0; j < pParticleAmount; ++j) {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), RED, GREEN, BLUE);
            }
        }
    }

    @Override
    public void playerTouch(Player pEntity) {}

    @Override
    public ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void setBaseDamage(double pBaseDamage) {
        this.baseDamage = pBaseDamage;
    }

    @Override
    public double getBaseDamage() {
        return this.baseDamage;
    }

    @Override
    public boolean tryPickup(Player pPlayer) {
        return false;
    }

    @Override
    public void tickDespawn() {
        ++this.life;
        if (this.life >= maxGroundTime) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        onHit();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        onHit();
        this.life = 0;
    }

    protected void onHit() {
        this.setSoundEvent(SoundEventRegistration.SQUIRT_SLUG_HIT.get());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEventRegistration.SQUIRT_SLUG_HIT.get();
    }

    private void updateColor() {
        int pValue;
        pValue = this.fluid != null ? IClientFluidTypeExtensions.of(this.fluid).getTintColor() : -1;
        if (pValue == -1) {
            pValue = this.chemical != null ? this.chemical.getColor() : -1;
        }
        if(pValue == -1) {
            pValue = this.potion != null ? this.potion.getEffects().get(0).getEffect().getColor() : -1;
        }
        this.entityData.set(ID_EFFECT_COLOR, pValue);
    }

    public int getColor() {
        return this.entityData.get(ID_EFFECT_COLOR);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_EFFECT_COLOR, -1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.fluid != null) {
            pCompound.putString("Fluid", fluid.getFluidType().toString());
        }
        if (!this.effects.isEmpty()) {
            ListTag listtag = new ListTag();
            for(MobEffectInstance mobeffectinstance : this.effects) {
                listtag.add(mobeffectinstance.save(new CompoundTag()));
            }
            pCompound.put("CustomEffects", listtag);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    //TODO: READ IT IN!
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey((Item) this.chemical)));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.chemical = (Chemical) ForgeRegistries.ITEMS.getValue(additionalData.readResourceLocation());
    }
}
