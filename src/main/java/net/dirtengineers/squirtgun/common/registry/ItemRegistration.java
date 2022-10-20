package net.dirtengineers.squirtgun.common.registry;

import com.smashingmods.chemlib.api.Chemical;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.BaseSquirtMagazine;
import net.dirtengineers.squirtgun.common.item.SquirtMagazineItem;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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
    public static List<Chemical> ammunitionLiquids = new ArrayList<>();

    public static final Predicate<FluidStack> SQUIRT_AMMUNITION_ONLY = (fluidStack) -> MAGAZINES.containsKey(fluidStack.getFluid());

    public static final RegistryObject<Item> SQUIRTMAGAZINEITEM = SQUIRTGUNITEMS.register("squirtmagazineitem",
            () -> new SquirtMagazineItem(new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(1)));

    public static final RegistryObject<Item> SQUIRTGUNITEM = SQUIRTGUNITEMS.register("squirtgunitem",
            () -> new SquirtgunItem(new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON).stacksTo(1)));

    public static void registerMags(RegisterEvent pEvent){
        if(pEvent.getRegistryKey() == ForgeRegistries.Keys.ITEMS) {
            ammunitionLiquids.addAll(getCompounds().stream().filter(compound -> compound.getMatterState() == LIQUID).toList());
            ammunitionLiquids.addAll(getElements().stream().filter(element -> element.getMatterState() == LIQUID).toList());

            for (Chemical chemical : ammunitionLiquids) {
                ResourceLocation location = new ResourceLocation(Squirtgun.MOD_ID, String.format("%s_magazine", chemical.getChemicalName()));

                pEvent.register(
                        ForgeRegistries.Keys.ITEMS,
                        location,
                        () -> new BaseSquirtMagazine(chemical,
                                new Item.Properties().tab(SQUIRTGUN_TAB).rarity(Rarity.COMMON)));
                MAGAZINES.put((BaseSquirtMagazine) ForgeRegistries.ITEMS.getValue(location), chemical);
            }
        }
    }

    public static String getFriendlyItemName(Item pItem){
        return I18n.get(pItem.getDescriptionId());
    }

    public static void register(IEventBus eventbus){
        SQUIRTGUNITEMS.register(eventbus);
    }
}
