package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.item.SquirtMagazine;
import net.dirtengineers.squirtgun.common.item.SquirtgunItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ItemRegistration {

    public static final DeferredRegister<Item> SQUIRTGUNITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,  Squirtgun.MOD_ID);

    public static final CreativeModeTab SQUIRTGUN_TAB = new CreativeModeTab("squirtguntab")
    {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(SQUIRTGUN.get());
        }
    };

    public static final RegistryObject<Item> SQUIRTMAGAZINE = SQUIRTGUNITEMS.register("squirtmagazine", SquirtMagazine::new);

    public static final RegistryObject<Item> SQUIRTGUN = SQUIRTGUNITEMS.register("squirtgun", SquirtgunItem::new);

//    public static final RegistryObject<Item> SQUIRTMAGAZINETEST = SQUIRTGUNITEMS.register("squirtmagazinetest", SquirtMagazineTest::new);

    public static void register(IEventBus eventbus){
        SQUIRTGUNITEMS.register(eventbus);
    }
}
