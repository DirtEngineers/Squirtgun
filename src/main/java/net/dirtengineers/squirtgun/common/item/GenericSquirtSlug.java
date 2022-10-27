package net.dirtengineers.squirtgun.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GenericSquirtSlug extends Item {
    public GenericSquirtSlug() {
        super(new Item.Properties().rarity(Rarity.COMMON).stacksTo(1));
    }
}
