package net.dirtengineers.squirtgun.common.item;

import net.minecraft.world.item.Item;

import static net.dirtengineers.squirtgun.common.registry.ItemRegistration.SQUIRTGUN_TAB;

public class SquirtMagazineTest extends Item {
    public SquirtMagazineTest() {
        super(new Item.Properties().tab(SQUIRTGUN_TAB));

    }
}
