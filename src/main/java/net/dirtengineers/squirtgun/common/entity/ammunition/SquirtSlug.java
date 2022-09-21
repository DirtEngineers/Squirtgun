package net.dirtengineers.squirtgun.common.entity.ammunition;

import net.dirtengineers.squirtgun.common.registry.EntityRegistration;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class SquirtSlug extends AbstractArrow {
//    DynamicFluidContainerModel
    /*
        Investigate shoot() from the super class
     */

    public static final int shotSize = 100;
    private int life;

    private Fluid ammoType;

    public SquirtSlug(Level pLevel){
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

    public void setAmmoType(Fluid pAmmoType){
        this.ammoType = pAmmoType;
    }

    public Fluid getAmmoType(){
        return this.ammoType;
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void playerTouch(Player pEntity) {}

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

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        this.life = 0;
        super.onHitEntity(pResult);
    }

//    Iterator var1 = Arrays.asList("solid", "liquid", "gas", "dust").iterator();
//
//        while(var1.hasNext()) {
//        String type = (String)var1.next();
//        (
//                (ItemModelBuilder)(
//                        (ItemModelBuilder)this.withExistingParent(String.format("item/compound_%s_model", type), this.mcLoc("item/generated"))
//                ).texture("layer0", this.modLoc(String.format("items/compound_%s_layer_0", type)))
//        ).texture("layer1", this.modLoc(String.format("items/compound_%s_layer_1", type)));
//    }
}
