package net.dirtengineers.squirtgun.common.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smashingmods.chemlib.api.ChemicalItemType;
import com.smashingmods.chemlib.api.MatterState;
import com.smashingmods.chemlib.common.items.CompoundItem;
import com.smashingmods.chemlib.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class CompoundRegistration {
    public static final JsonObject COMPOUNDS_JSON = Registry.getStreamAsJsonObject("/assets/squirtgun/data/compounds.json");

    public CompoundRegistration() {}

    private static void registerCompounds() {
        Iterator<JsonElement> var0 = COMPOUNDS_JSON.getAsJsonArray("compounds").iterator();

        while(true) {
            while(var0.hasNext()) {
                JsonElement jsonElement = var0.next();
                JsonObject object = jsonElement.getAsJsonObject();
                String compoundName = object.get("name").getAsString();
                MatterState matterState = MatterState.valueOf(object.get("matter_state").getAsString().toUpperCase());
                String description = object.has("description") ? object.get("description").getAsString() : "";
                String color = object.get("color").getAsString();
                JsonArray components = object.getAsJsonArray("components");
                HashMap<String, Integer> componentMap = new LinkedHashMap<>();

                for (JsonElement component : components) {
                    JsonObject componentObject = component.getAsJsonObject();
                    String componentName = componentObject.get("name").getAsString();
                    int count = componentObject.has("count") ? componentObject.get("count").getAsInt() : 1;
                    componentMap.put(componentName, count);
                }

                ItemRegistry.REGISTRY_COMPOUNDS.register(compoundName, () -> {
                    return new CompoundItem(compoundName, matterState, componentMap, description, color, mobEffectsFactory(object));
                });
                boolean hasFluid;
                switch (matterState) {
                    case SOLID -> {
                        hasFluid = object.get("has_item").getAsBoolean();
                        if (!hasFluid) {
                            ItemRegistry.registerItemByType(ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_COMPOUNDS, compoundName), ChemicalItemType.COMPOUND, ItemRegistry.COMPOUND_TAB);
                        }
                    }
                    case LIQUID, GAS -> {
                        hasFluid = object.has("has_fluid") && object.get("has_fluid").getAsBoolean();
                        if (!hasFluid) {
                            JsonObject properties = object.get("fluid_properties").getAsJsonObject();
                            int slopeFindDistance = properties.has("slope_find_distance") ? properties.get("slope_find_distance").getAsInt() : 4;
                            int decreasePerBlock = properties.has("decrease_per_block") ? properties.get("decrease_per_block").getAsInt() : 1;
                            switch (matterState) {
                                case LIQUID, GAS ->
                                        FluidRegistration.registerFluids(compoundName, fluidTypePropertiesFactory(properties, compoundName), (int) Long.parseLong(color, 16), slopeFindDistance, decreasePerBlock);
                            }
                        }
                    }
                }
            }
            return;
        }
    }

    public static List<MobEffectInstance> mobEffectsFactory(JsonObject object) {
        List<MobEffectInstance> effectsList = new ArrayList<>();
        JsonArray effects = object.getAsJsonArray("effect");
        if (effects != null) {
            for (JsonElement effect : effects) {
                JsonObject effectObject = effect.getAsJsonObject();
                String effectLocation = effectObject.get("location").getAsString();
                int effectDuration = effectObject.get("duration").getAsInt();
                int effectAmplifier = effectObject.get("amplifier").getAsInt();
                MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectLocation));
                if (mobEffect != null) {
                    effectsList.add(new MobEffectInstance(mobEffect, effectDuration, effectAmplifier));
                }
            }
        }
        return effectsList;
    }

    public static FluidType.Properties fluidTypePropertiesFactory(JsonObject pObject, String pName) {
        int density = pObject.has("density") ? pObject.get("density").getAsInt() : 1000;
        int lightLevel = pObject.has("light_level") ? pObject.get("light_level").getAsInt() : 0;
        int viscosity = pObject.has("viscosity") ? pObject.get("viscosity").getAsInt() : 1000;
        int temperature = pObject.has("temperature") ? pObject.get("temperature").getAsInt() : 300;
        float motionScale = pObject.has("motion_scale") ? pObject.get("motion_scale").getAsFloat() : 0.014F;
        int fallDistanceModifier = pObject.has("fall_distance_modifier") ? pObject.get("fall_distance_modifier").getAsInt() : 0;
        BlockPathTypes pathType = pObject.has("path_type") ? BlockPathTypes.valueOf(pObject.get("path_type").getAsString().toUpperCase()) : BlockPathTypes.WATER;
        boolean pushEntity = !pObject.has("push_entity") || pObject.get("push_entity").getAsBoolean();
        boolean canSwim = !pObject.has("can_swim") || pObject.get("can_swim").getAsBoolean();
        boolean canDrown = pObject.has("can_drown") && pObject.get("can_drown").getAsBoolean();
        boolean canHydrate = pObject.has("can_hydrate") && pObject.get("can_hydrate").getAsBoolean();
        boolean canExtinguish = pObject.has("can_extinguish") && pObject.get("can_extinguish").getAsBoolean();
        boolean supportsBoating = pObject.has("supports_boating") && pObject.get("supports_boating").getAsBoolean();
        boolean canConvertToSource = pObject.has("can_convert_to_source") && pObject.get("can_convert_to_source").getAsBoolean();
        return FluidType.Properties.create().descriptionId(String.format("block.squirtgun.%s", pName)).density(density).lightLevel(lightLevel).viscosity(viscosity).temperature(temperature).motionScale(motionScale).fallDistanceModifier((float)fallDistanceModifier).pathType(pathType).canPushEntity(pushEntity).canSwim(canSwim).canDrown(canDrown).canHydrate(canHydrate).canExtinguish(canExtinguish).canConvertToSource(canConvertToSource).supportsBoating(supportsBoating).rarity(Rarity.COMMON).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH);
    }

    public static void register() {
        registerCompounds();
    }
}