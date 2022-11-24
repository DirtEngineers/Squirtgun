package net.dirtengineers.squirtgun.common.block;

import net.minecraft.client.color.block.BlockColor;

public interface BlockBrassColor {
    static BlockColor getColor() {
        return (pState, pLevel, pPos, pTintIndex1) -> 0XC6A874;
    }
}
