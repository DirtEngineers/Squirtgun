package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.SquirtMagazine;
import net.dirtengineers.squirtgun.common.item.SquirtSlugItem;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ItemRegistry {

    public static final DeferredRegister<Item> SQUIRTGUNITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,  Squirtgun.MOD_ID);

    public static final CreativeModeTab SQUIRTGUN_TAB = new CreativeModeTab("squirtguntab")
    {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(SQUIRTGUN.get());
        }
    };

    public static final RegistryObject<Item> SQUIRTGUN = SQUIRTGUNITEMS.register("squirtgun",
            () -> new SquirtgunItem(new Item.Properties().tab(SQUIRTGUN_TAB).stacksTo(1)));

    public static final RegistryObject<Item> SQUIRTMAGAZINE = SQUIRTGUNITEMS.register("squirtmagazine", SquirtMagazine::new);

    public static final RegistryObject<Item> SQUIRTSLUGITEM = SQUIRTGUNITEMS.register("squirtslugitem", SquirtSlugItem::new);

    public static void register(IEventBus eventbus){
        SQUIRTGUNITEMS.register(eventbus);
    }
}
