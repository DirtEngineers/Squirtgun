package net.dirtengineers.squirtgun.common.registry;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BasePhial;
import net.dirtengineers.squirtgun.common.item.GenericSlug;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smashingmods.chemlib.api.MatterState.LIQUID;
import static com.smashingmods.chemlib.registry.ItemRegistry.getCompounds;
import static com.smashingmods.chemlib.registry.ItemRegistry.getElements;


public class ItemRegistration {
    public static final CreativeModeTab SQUIRTGUN_TAB = new CreativeModeTab("squirtguntab") {
        public ItemStack makeIcon() {
            return new ItemStack(SQUIRTGUNITEM.get());
        }
    };
    public static final Item.Properties ITEM_PROPERTIES_NO_TAB;
    public static final Item.Properties ITEM_PROPERTIES_WITH_TAB;
    public static final DeferredRegister<Item> SQUIRTGUNITEMS;
//    public static final RegistryObject<Item> PHIAL_ITEMS;
    public static final RegistryObject<Item> SQUIRTSLUGITEM;
    public static final RegistryObject<Item> SQUIRTGUNITEM;
    public static Map<BasePhial, Chemical> PHIALS;
    public static Map<Chemical, Fluid> CHEMICAL_FLUIDS;
    public static List<Chemical> ammunitionChemicals;

    public static void registerPhialsAndSlugs(RegisterEvent pEvent){
        if(pEvent.getRegistryKey() == ForgeRegistries.Keys.ITEMS) {
            ItemRegistration.ammunitionChemicals.addAll(getCompounds().stream().filter(compound -> compound.getMatterState() == LIQUID).toList());
            ItemRegistration.ammunitionChemicals.addAll(getElements().stream().filter(element -> element.getMatterState() == LIQUID).toList());
            ItemRegistration.buildPhials(pEvent);
        }
    }

    private static void buildPhials(RegisterEvent pEvent){
        for (Chemical chemical : ItemRegistration.ammunitionChemicals) {
            ResourceLocation magLocation = new ResourceLocation(Squirtgun.MOD_ID, String.format("%s_phial", chemical.getChemicalName()));

            pEvent.register(
                    ForgeRegistries.Keys.ITEMS,
                    magLocation,
                    () -> new BasePhial(chemical, BasePhial.UPGRADES.BASE));
            PHIALS.put((BasePhial) ForgeRegistries.ITEMS.getValue(magLocation), chemical);
        }
    }

    public static <B extends Block> void fromBlock(RegistryObject<B> block) {
        SQUIRTGUNITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES_WITH_TAB));
    }

    public static void register(IEventBus eventbus){
        BlockRegistration.BLOCKS.getEntries().forEach(ItemRegistration::fromBlock);
        SQUIRTGUNITEMS.register(eventbus);
    }

    static{
        ITEM_PROPERTIES_WITH_TAB = new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(1);
        ITEM_PROPERTIES_NO_TAB = new Item.Properties().rarity(Rarity.COMMON).stacksTo(1);
        SQUIRTGUNITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,  Squirtgun.MOD_ID);
//        PHIAL_ITEMS = SQUIRTGUNITEMS.register("squirtphialitem", () -> new PhialItem(ITEM_PROPERTIES_NO_TAB));
        SQUIRTSLUGITEM = SQUIRTGUNITEMS.register("squirtslugitem", () -> new GenericSlug(ITEM_PROPERTIES_NO_TAB));
        SQUIRTGUNITEM = SQUIRTGUNITEMS.register("squirtgunitem", () -> new SquirtgunItem(ITEM_PROPERTIES_WITH_TAB));
        PHIALS = new HashMap<>();
        CHEMICAL_FLUIDS = new HashMap<>();
        ammunitionChemicals = new ArrayList<>();
    }
}
