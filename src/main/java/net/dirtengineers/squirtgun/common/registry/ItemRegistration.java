package net.dirtengineers.squirtgun.common.registry;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BaseSquirtMagazine;
import net.dirtengineers.squirtgun.common.item.GenericSquirtSlug;
import net.dirtengineers.squirtgun.common.item.SquirtMagazineItem;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smashingmods.chemlib.api.MatterState.LIQUID;
import static com.smashingmods.chemlib.registry.ItemRegistry.getCompounds;
import static com.smashingmods.chemlib.registry.ItemRegistry.getElements;


public class ItemRegistration {

    public static final DeferredRegister<Item> SQUIRTGUNITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,  Squirtgun.MOD_ID);
    public static final CreativeModeTab SQUIRTGUN_TAB = new CreativeModeTab("squirtguntab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(SQUIRTGUNITEM.get());
        }
    };

    public static Map<BaseSquirtMagazine, Chemical> MAGAZINES = new HashMap<>();
    public static Map<Chemical, Fluid> CHEMICAL_FLUIDS = new HashMap<>();
    public static List<Chemical> ammunitionChemicals = new ArrayList<>();

    public static final RegistryObject<Item> SQUIRTMAGAZINEITEM = SQUIRTGUNITEMS.register("squirtmagazineitem", SquirtMagazineItem::new);

    public static final RegistryObject<Item> SQUIRTSLUGITEM = SQUIRTGUNITEMS.register("squirtslugitem", GenericSquirtSlug::new);

    public static final RegistryObject<Item> SQUIRTGUNITEM = SQUIRTGUNITEMS.register("squirtgunitem", SquirtgunItem::new);

    public static void registerMagsAndSlugs(RegisterEvent pEvent){
        if(pEvent.getRegistryKey() == ForgeRegistries.Keys.ITEMS) {
            ItemRegistration.buildAmmunitionChemicals();
            ItemRegistration.buildMagazines(pEvent);
        }
    }

    private static void buildAmmunitionChemicals(){
        ItemRegistration.ammunitionChemicals.addAll(getCompounds().stream().filter(compound -> compound.getMatterState() == LIQUID).toList());
        ItemRegistration.ammunitionChemicals.addAll(getElements().stream().filter(element -> element.getMatterState() == LIQUID).toList());
    }

    private static void buildMagazines(RegisterEvent pEvent){
        for (Chemical chemical : ItemRegistration.ammunitionChemicals) {
            ResourceLocation magLocation = new ResourceLocation(Squirtgun.MOD_ID, String.format("%s_magazine", chemical.getChemicalName()));

            pEvent.register(
                    ForgeRegistries.Keys.ITEMS,
                    magLocation,
                    () -> new BaseSquirtMagazine(chemical,
                            new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON), BaseSquirtMagazine.UPGRADES.BASE));
            MAGAZINES.put((BaseSquirtMagazine) ForgeRegistries.ITEMS.getValue(magLocation), chemical);
        }
    }

    public static void register(IEventBus eventbus){
        SQUIRTGUNITEMS.register(eventbus);
    }
}
