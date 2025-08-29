package com.blakebr0.cucumber.mixin;

import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.level.block.CropBlock.class)
public interface CropBlockAccessor {
    @Invoker
    ItemLike callGetBaseSeedId();
}
