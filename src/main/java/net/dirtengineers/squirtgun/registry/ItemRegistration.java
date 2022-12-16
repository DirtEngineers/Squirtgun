package net.dirtengineers.squirtgun.registry;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.*;
import net.dirtengineers.squirtgun.common.item.components.PhialCapItem;
import net.dirtengineers.squirtgun.common.item.components.Actuator;
import net.dirtengineers.squirtgun.common.item.materials.BrassBlendItem;
import net.dirtengineers.squirtgun.common.item.materials.BrassIngotItem;
import net.dirtengineers.squirtgun.common.item.materials.BrassNuggetItem;
import net.dirtengineers.squirtgun.common.item.materials.FusedQuartzShard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

import static com.smashingmods.chemlib.api.MatterState.LIQUID;
import static com.smashingmods.chemlib.registry.ItemRegistry.getCompounds;
import static com.smashingmods.chemlib.registry.ItemRegistry.getElements;



public class ItemRegistration {
    public static final CreativeModeTab SQUIRTGUN_TAB = new CreativeModeTab(Constants.squirtgunTabName) {
        public ItemStack makeIcon() {
            return new ItemStack(SQUIRTGUN.get());
        }
    };

    //Properties
    public static final Item.Properties ITEM_PROPERTIES_NO_TAB;
    public static final Item.Properties ITEM_PROPERTIES_WITH_TAB;
    public static final Item.Properties ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE;
    public static final DeferredRegister<Item> ITEMS;

    //Items
    public static final RegistryObject<Item> PHIAL;
    public static final RegistryObject<Item> SQUIRTSLUG;
    public static final RegistryObject<Item> SQUIRTGUN;
    public static final RegistryObject<Item> BRASS_BLEND;
    public static final RegistryObject<Item> BRASS_NUGGET;
    public static final RegistryObject<Item> BRASS_INGOT;
    public static final RegistryObject<Item> PHIAL_CAP;
    public static final RegistryObject<Item> FUSED_QUARTZ_SHARD;
    public static final RegistryObject<Item> GUN_ACTUATOR;

    //Collections
    public static Map<ChemicalPhial, Chemical> PHIALS;
    public static Map<Chemical, Fluid> CHEMICAL_FLUIDS;
    public static List<Chemical> ammunitionChemicals;

    public static void buildChemical_Fluids() {
        for (Chemical chemical : ItemRegistration.ammunitionChemicals)
            if (chemical != null && chemical.getFluidTypeReference().isPresent()) {
                String location = String.valueOf(chemical.getFluidTypeReference().get());
                //TODO: Make a list chemicals to NOT append "_fluid" to.
                // preferably in the CompoundRegistration
                if (!Objects.equals(location, Constants.EMPTY_FLUID_NAME)
                        && !Objects.equals(chemical.getChemicalName(), "water")
                        && !Objects.equals(chemical.getChemicalName(), "milk")
                        && !Objects.equals(chemical.getChemicalName(), "lava")) {
                    location += "_fluid";
                }
                ItemRegistration.CHEMICAL_FLUIDS.put(chemical, ForgeRegistries.FLUIDS.getValue(new ResourceLocation(location)));
            }
    }

    public static void registerPhialsAndSlugs(RegisterEvent pEvent) {
        if (pEvent.getRegistryKey() == ForgeRegistries.Keys.ITEMS) {
            ItemRegistration.SetAmmunitionChemicals();
            ItemRegistration.buildPhials(pEvent);
        }
    }

    private static void SetAmmunitionChemicals() {
        ItemRegistration.ammunitionChemicals.addAll(getCompounds().stream().filter(compound -> compound.getMatterState() == LIQUID).toList());
        ItemRegistration.ammunitionChemicals.addAll(getElements().stream().filter(element -> element.getMatterState() == LIQUID).toList());
    }

    private static void buildPhials(RegisterEvent pEvent) {
        ResourceLocation phialLocation;
        for (Chemical chemical : ItemRegistration.ammunitionChemicals) {
            phialLocation =
                    new ResourceLocation(
                            Squirtgun.MOD_ID,
                            String.format("%s_phial", chemical.getChemicalName()));

            pEvent.register(
                    ForgeRegistries.Keys.ITEMS,
                    phialLocation,
                    () -> new ChemicalPhial(chemical, BasePhial.CAPACITY_UPGRADE.BASE));
            PHIALS.put((ChemicalPhial) ForgeRegistries.ITEMS.getValue(phialLocation), chemical);
        }
    }

    public static <B extends Block> void fromBlock(RegistryObject<B> block) {
        ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES_WITH_TAB));
    }

    public static void register(IEventBus eventbus) {
        BlockRegistration.BLOCKS.getEntries().forEach(ItemRegistration::fromBlock);
        ITEMS.register(eventbus);
    }

    static {
        ITEM_PROPERTIES_WITH_TAB = new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(64);
        ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE = new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(1);
        ITEM_PROPERTIES_NO_TAB = new Item.Properties().rarity(Rarity.COMMON).stacksTo(1);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Squirtgun.MOD_ID);
        PHIAL = ITEMS.register(Constants.phialItemName, () -> new EmptyPhialItem(ITEM_PROPERTIES_WITH_TAB));
        SQUIRTSLUG = ITEMS.register(Constants.slugItemName, () -> new GenericSlug(ITEM_PROPERTIES_NO_TAB));
        SQUIRTGUN = ITEMS.register(Constants.gunItemName, () -> new SquirtgunItem(ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE));
        BRASS_BLEND = ITEMS.register(Constants.brassBlendItemName, () -> new BrassBlendItem(ITEM_PROPERTIES_WITH_TAB));
        PHIAL_CAP = ITEMS.register(Constants.phialCapItemName, () -> new PhialCapItem(ITEM_PROPERTIES_WITH_TAB));
        BRASS_NUGGET = ITEMS.register(Constants.brassNuggetItemName, () -> new BrassNuggetItem(ITEM_PROPERTIES_WITH_TAB));
        BRASS_INGOT = ITEMS.register(Constants.brassIngotItemName, () -> new BrassIngotItem(ITEM_PROPERTIES_WITH_TAB));
        FUSED_QUARTZ_SHARD = ITEMS.register(Constants.quartzShardItemName, () -> new FusedQuartzShard(ITEM_PROPERTIES_WITH_TAB));
        GUN_ACTUATOR = ITEMS.register(Constants.actuatorItemName, () -> new Actuator(ITEM_PROPERTIES_WITH_TAB));

        PHIALS = new HashMap<>();
        CHEMICAL_FLUIDS = new HashMap<>();
        ammunitionChemicals = new ArrayList<>();
    }
}
