package net.dirtengineers.squirtgun.datagen.recipe;

import com.smashingmods.chemlib.registry.ItemRegistry;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.registry.ItemRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ItemDefinitions {
    public static final Item COPPER_DUST = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_METAL_DUSTS, "copper_dust").get();
    public static final Item ZINC_DUST = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_METAL_DUSTS, "zinc_dust").get();
    public static final Item SILICA = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_COMPOUNDS, "silicon_dioxide").get();
    public static final Item PVC = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_COMPOUNDS, "polyvinyl_chloride").get();
    public static final Item GRAPHITE_DUST = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_COMPOUND_DUSTS, "graphite_dust").get();
    public static final Item RADIUM_DUST = ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_METAL_DUSTS, "radium_dust").get();
    public static final Item BRASS_BLEND = ItemRegistration.BRASS_BLEND.get();
    public static final Item BRASS_NUGGET = ItemRegistration.BRASS_NUGGET.get();
    public static final Item BRASS_INGOT = ItemRegistration.BRASS_INGOT.get();
    public static final Item BRASS_BLOCK = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Squirtgun.MOD_ID, "brass_block")));
    public static final Item QUARTZ_SHARD = ItemRegistration.QUARTZ_SHARD.get();
    public static final Item PHIAL_CAP = ItemRegistration.PHIAL_CAP.get();
    public static final Item GUN_GRIP = ItemRegistration.GUN_GRIP.get();
    public static final Item GUN_BARREL_SECTION = ItemRegistration.GUN_BARREL_SECTION.get();
    public static final Item PHIAL_MOUNT = ItemRegistration.GUN_PHIAL_MOUNT.get();
    public static final Item GUN_ACTUATOR = ItemRegistration.GUN_ACTUATOR.get();
    public static final Item GUN = ItemRegistration.SQUIRTGUN.get();
}
