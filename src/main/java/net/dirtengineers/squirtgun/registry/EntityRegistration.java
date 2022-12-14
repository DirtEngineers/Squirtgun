package net.dirtengineers.squirtgun.registry;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class EntityRegistration {

    public static final DeferredRegister<EntityType<?>> SG_ENTITIES;
    public static final RegistryObject<EntityType<SquirtSlug>> SQUIRT_SLUG;

    public static void register(IEventBus eventbus) {
        SG_ENTITIES.register(eventbus);
    }

    static{
        SG_ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,  Squirtgun.MOD_ID);
        SQUIRT_SLUG =
                SG_ENTITIES.register(Constants.slugEntityEntityName,
                        () -> EntityType.Builder.of((EntityType.EntityFactory<SquirtSlug>) SquirtSlug::new,
                                MobCategory.MISC).sized(0.5F, 0.5F).build(Constants.slugEntityEntityName));
    }
}

