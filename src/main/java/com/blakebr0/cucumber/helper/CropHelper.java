package com.blakebr0.cucumber.helper;

import com.blakebr0.cucumber.Cucumber;
import com.blakebr0.cucumber.mixin.CropBlockAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;

import java.util.HashSet;

public final class CropHelper {
    private static final HashSet<Block> ERRORED_BLOCKS = new HashSet<>();

    public static Item getSeedsItem(CropBlock block) {
        try {
            return ((CropBlockAccessor) block).callGetBaseSeedId().asItem();
        } catch (Exception e) {
            if (ERRORED_BLOCKS.add(block)) {
                Cucumber.LOGGER.error("Unable to get seed from crop {}", e.getLocalizedMessage());
            }
        }

        return null;
    }
}
