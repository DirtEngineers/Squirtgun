package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,  Squirtgun.MOD_ID);
    public static final RegistryObject<EntityType<SquirtSlug>> SQUIRT_SLUG =
            ENTITIES.register("squirt_slug",
                    () -> EntityType.Builder.of(SquirtSlug::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F).build("squirt_slug"));
    public static void register(IEventBus eventbus) {
        ENTITIES.register(eventbus);
    }
}
