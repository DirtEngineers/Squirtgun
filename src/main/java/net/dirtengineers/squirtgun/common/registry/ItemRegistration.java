package net.dirtengineers.squirtgun.common.registry;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.*;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
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
            return new ItemStack(SQUIRTGUNITEM.get());
        }
    };
    public static final Item.Properties ITEM_PROPERTIES_NO_TAB;
    public static final Item.Properties ITEM_PROPERTIES_WITH_TAB;
    public static final Item.Properties ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE;
    public static final DeferredRegister<Item> SQUIRTGUNITEMS;
    public static final RegistryObject<Item> PHIAL;
    public static final RegistryObject<Item> SQUIRTSLUGITEM;
    public static final RegistryObject<Item> SQUIRTGUNITEM;
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
        SQUIRTGUNITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES_WITH_TAB));
    }

    public static void register(IEventBus eventbus) {
        BlockRegistration.BLOCKS.getEntries().forEach(ItemRegistration::fromBlock);
        SQUIRTGUNITEMS.register(eventbus);
    }

    static {
        ITEM_PROPERTIES_WITH_TAB = new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(64);
        ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE = new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(1);
        ITEM_PROPERTIES_NO_TAB = new Item.Properties().rarity(Rarity.COMMON).stacksTo(1);
        SQUIRTGUNITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Squirtgun.MOD_ID);
        PHIAL = SQUIRTGUNITEMS.register(Constants.phialItemName, () -> new EmptyPhialItem(ITEM_PROPERTIES_WITH_TAB));
        SQUIRTSLUGITEM = SQUIRTGUNITEMS.register(Constants.slugItemName, () -> new GenericSlug(ITEM_PROPERTIES_NO_TAB));
        SQUIRTGUNITEM = SQUIRTGUNITEMS.register(Constants.gunItemName, () -> new SquirtgunItem(ITEM_PROPERTIES_WITH_TAB_STACK_TO_ONE));
        PHIALS = new HashMap<>();
        CHEMICAL_FLUIDS = new HashMap<>();
        ammunitionChemicals = new ArrayList<>();
    }
}
