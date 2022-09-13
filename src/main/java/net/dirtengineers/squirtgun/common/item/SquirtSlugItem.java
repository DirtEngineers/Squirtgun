package net.dirtengineers.squirtgun.common.item;

import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.dirtengineers.squirtgun.common.registry.EntityRegistry.SQUIRT_SLUG;

public class SquirtSlugItem extends Item {

    public SquirtSlugItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public SquirtSlug createSquirtSlug(Level pLevel, ItemStack pStack, LivingEntity pShooter) {

        SquirtSlug SquirtSlug = SQUIRT_SLUG.get().create(pLevel);

        return SquirtSlug;
    }
}
