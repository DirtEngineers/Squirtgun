package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class SquirtSlugItem extends ArrowItem {

    public SquirtSlugItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public SquirtSlug createSlug(Level pLevel, LivingEntity pShooter) {
        return new SquirtSlug(pShooter, pLevel);
    }
}
