package com.blakebr0.cucumber.inventory.slot;

import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import net.minecraft.world.item.ItemStack;

public class OutputSlot extends SlotItemHandler {
	public OutputSlot(SlottedStackStorage inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}
