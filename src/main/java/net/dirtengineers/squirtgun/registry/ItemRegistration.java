package net.dirtengineers.squirtgun.registry;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.*;
import net.dirtengineers.squirtgun.common.item.components.Actuator;
import net.dirtengineers.squirtgun.common.item.components.PhialCapItem;
import net.dirtengineers.squirtgun.common.item.materials.BrassBlendItem;
import net.dirtengineers.squirtgun.common.item.materials.BrassIngotItem;
import net.dirtengineers.squirtgun.common.item.materials.BrassNuggetItem;
import net.dirtengineers.squirtgun.common.item.materials.FusedQuartzShard;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

import static com.smashingmods.chemlib.api.MatterState.LIQUID;
import static com.smashingmods.chemlib.registry.ItemRegistry.getCompounds;
import static com.smashingmods.chemlib.registry.ItemRegistry.getElements;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ItemRegistration {

    public static final CreativeModeTab SQUIRTGUN_TAB = new CreativeModeTab(Constants.squirtgunTabName) {
        public ItemStack makeIcon() {
            return new ItemStack(SQUIRTGUN.get());
        }
    };

    //Properties
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Squirtgun.MOD_ID);
    public static final Item.Properties ITEM_PROPERTIES_NO_TAB = new Item.Properties().rarity(Rarity.COMMON).stacksTo(1);
    public static final Item.Properties ITEM_PROPERTIES_WITH_TAB = new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(64);
    public static final Item.Properties ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE = new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(1);

    //Items
    public static final RegistryObject<Item> PHIAL = ITEMS.register(Constants.phialItemName, () -> new EmptyPhialItem(ITEM_PROPERTIES_WITH_TAB));
    public static final RegistryObject<Item> SQUIRTSLUG = ITEMS.register(Constants.slugItemName, () -> new GenericSlug(ITEM_PROPERTIES_NO_TAB));
    public static final RegistryObject<Item> SQUIRTGUN = ITEMS.register(Constants.gunItemName, () -> new SquirtgunItem(ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE));
    public static final RegistryObject<Item> BRASS_BLEND = ITEMS.register(Constants.brassBlendItemName, () -> new BrassBlendItem(ITEM_PROPERTIES_WITH_TAB));
    public static final RegistryObject<Item> BRASS_NUGGET = ITEMS.register(Constants.brassNuggetItemName, () -> new BrassNuggetItem(ITEM_PROPERTIES_WITH_TAB));
    public static final RegistryObject<Item> BRASS_INGOT = ITEMS.register(Constants.brassIngotItemName, () -> new BrassIngotItem(ITEM_PROPERTIES_WITH_TAB));
    public static final RegistryObject<Item> PHIAL_CAP = ITEMS.register(Constants.phialCapItemName, () -> new PhialCapItem(ITEM_PROPERTIES_WITH_TAB));
    public static final RegistryObject<Item> FUSED_QUARTZ_SHARD = ITEMS.register(Constants.quartzShardItemName, () -> new FusedQuartzShard(ITEM_PROPERTIES_WITH_TAB));
    public static final RegistryObject<Item> GUN_ACTUATOR = ITEMS.register(Constants.actuatorItemName, () -> new Actuator(ITEM_PROPERTIES_WITH_TAB));

    //Collections
    public static Map<Chemical, Fluid> CHEMICAL_FLUIDS = new HashMap<>();
    public static List<Chemical> ammunitionChemicals = new ArrayList<>();
    public static List<ChemicalPhial> CHEMICAL_PHIALS = new ArrayList<>();
    public static List<PotionPhial> POTION_PHIALS = new ArrayList<>();

    public static void buildChemical_Fluids() {
        for (Chemical chemical : ItemRegistration.ammunitionChemicals)
            if (chemical != null && chemical.getFluidReference().isPresent()) {
                ResourceLocation location = chemical.getFluidReference().get().getRegistryName();
                //TODO: Make a list chemicals to NOT append "_fluid" to.
                // preferably in the CompoundRegistration
                ItemRegistration.CHEMICAL_FLUIDS.put(chemical, ForgeRegistries.FLUIDS.getValue(Objects.requireNonNull(location)));
            }
    }

    @SubscribeEvent
    public static void registerPhialsAndSlugs(RegistryEvent.Register<Item> pEvent) {
        ItemRegistration.ammunitionChemicals.addAll(getCompounds().stream().filter(compound -> compound.getMatterState() == LIQUID).toList());
        ItemRegistration.ammunitionChemicals.addAll(getElements().stream().filter(element -> element.getMatterState() == LIQUID).toList());

        for (Chemical chemical : ItemRegistration.ammunitionChemicals) {
            pEvent.getRegistry().register(new ChemicalPhial(chemical, BasePhial.CAPACITY_UPGRADE.BASE).setRegistryName(Squirtgun.MOD_ID, String.format("%s_phial", chemical.getChemicalName())));
        }

        for (Map.Entry<ResourceKey<Potion>, Potion> potion : ForgeRegistries.POTIONS.getEntries().stream().toList()) {
            if (potion.getValue().getEffects().size() != 0) {
                pEvent.getRegistry().register(new PotionPhial(potion.getKey().location().toString(), BasePhial.CAPACITY_UPGRADE.BASE).setRegistryName(Squirtgun.MOD_ID, String.format("%s_phial", Objects.requireNonNull(potion.getValue().getRegistryName()).getPath())));
            }
        }
    }

    public static <B extends Block> void fromBlock(RegistryObject<B> block) {
        ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES_WITH_TAB));
    }

    public static void register(IEventBus eventbus) {
        BlockRegistration.BLOCKS.getEntries().forEach(ItemRegistration::fromBlock);
        ITEMS.register(eventbus);
    }
}
