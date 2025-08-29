package com.blakebr0.cucumber.inventory.slot;

import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import net.minecraft.world.item.ItemStack;

public class SingleSlot extends SlotItemHandler {
	public SingleSlot(SlottedStackStorage inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}
}
