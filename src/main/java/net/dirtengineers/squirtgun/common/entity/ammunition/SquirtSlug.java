package net.dirtengineers.squirtgun.common.entity.ammunition;

import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import static net.dirtengineers.squirtgun.common.registry.EntityRegistry.ENTITIES;
import static net.dirtengineers.squirtgun.common.registry.EntityRegistry.SQUIRT_SLUG;

public class SquirtSlug extends Arrow {

    /*
        Investigate shoot() from the super class
     */

    private ResourceLocation ammoType;

    public SquirtSlug(EntityType<? extends Arrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel); }

    public void setAmmoType(ResourceLocation pAmmoType){
        this.ammoType = pAmmoType;
    }

    public ResourceLocation getAmmoType(){
        return this.ammoType;
    }

    @Override
    @NotNull
    public ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void tickDespawn() {
        if (this.inGroundTime > 60){
            this.discard();
        }
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
