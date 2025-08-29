package com.blakebr0.cucumber.inventory.slot;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BaseItemStackHandlerSlot extends SlotItemHandler {
    private final BaseItemStackHandler inventory;
    private final int index;

    public BaseItemStackHandlerSlot(BaseItemStackHandler inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.inventory = inventory;
        this.index = index;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;// !this.inventory.extractItem(this.index, 1, true, true).isEmpty();
    }

    @Override
    public ItemStack remove(int amount) {
        return ItemStack.EMPTY;//this.inventory.extractItem(this.index, amount, false, true);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        var slotLimit = this.inventory.getSlotLimit(this.index);
        if (slotLimit > 64) {
            // if the max size for the stack is less than 64 then we should decrease the max stack size to the
            // same ratio
            if (stack.getMaxStackSize() < 64) {
                return (int) (slotLimit * (float) stack.getMaxStackSize() / 64);
            }

            return slotLimit;
        }

        return super.getMaxStackSize(stack);
    }
}
