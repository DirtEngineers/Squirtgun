package net.dirtengineers.squirtgun.common.registry;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.block.EncapsulatorMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistration {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES;
    public static final RegistryObject<MenuType<EncapsulatorMenu>> ENCAPSULATOR_MENU;

    MenuRegistration(){}

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }

    static {
        MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Squirtgun.MOD_ID);
        ENCAPSULATOR_MENU = registerMenuType(EncapsulatorMenu::new, Constants.encapsulatorMenuName);
    }
}
