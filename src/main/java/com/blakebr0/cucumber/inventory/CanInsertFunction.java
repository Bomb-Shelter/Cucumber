package com.blakebr0.cucumber.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface CanInsertFunction {
    boolean apply(int slot, ItemVariant variant, long amount);

    @FunctionalInterface
    interface Sided {
        boolean apply(int slot, ItemStack stack, Direction direction);
    }
}
