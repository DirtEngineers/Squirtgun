package net.dirtengineers.squirtgun.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class SquirtMagazineItem extends Item {

    public SquirtMagazineItem() {
        super(new Item.Properties().rarity(Rarity.COMMON).stacksTo(1));
    }
}



