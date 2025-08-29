package com.blakebr0.cucumber.inventory;

import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemStackHandler extends SnapshotParticipant<ItemStack[]> implements SlottedStackStorage {
    protected NonNullList<ItemStack> stacks;
    protected SingleSlotStorage<ItemVariant>[] slots;

    public ItemStackHandler() {
        this(1);
    }

    @Override
    protected ItemStack[] createSnapshot() {
        return stacks.stream().map(ItemStack::copy).toArray(ItemStack[]::new);
    }

    @Override
    protected void readSnapshot(ItemStack[] snapshot) {
        this.stacks = NonNullList.of(ItemStack.EMPTY, snapshot);
        this.slots = makeSlots();
    }

    public ItemStackHandler(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        slots = makeSlots();
    }

    public ItemStackHandler(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
        this.slots = makeSlots();
    }

    protected SingleSlotStorage<ItemVariant>[] makeSlots() {
        SingleSlotStorage<ItemVariant>[] slots = new SingleSlotStorage[stacks.size()];
        for (int i = 0; i < stacks.size(); i++) {
            slots[i] = new ItemStackHandlerSlot(i);
        }
        return slots;
    }

    public void setSize(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        validateSlotIndex(slot);
        this.stacks.set(slot, stack);
        onContentsChanged(slot);
    }

    @Override
    public int getSlotCount() {
        return stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);
        int amount;
        if (maxAmount >= Integer.MAX_VALUE)
            amount = Integer.MAX_VALUE;
        else
            amount = (int) maxAmount;
        for (int i = 0; i < this.stacks.size(); i++) {
            int slot = i;
            if (!isItemValid(i, resource, amount))
                continue;

            ItemStack existing = this.stacks.get(i);

            int limit = getStackLimit(i, resource, amount);

            if (!existing.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(resource.toStack(), existing))
                    continue;

                limit -= existing.getCount();
            }

            if (limit <= 0)
                return 0;

            boolean reachedLimit = amount > limit;

            updateSnapshots(transaction);
            if (existing.isEmpty()) {
                this.stacks.set(i, reachedLimit ? resource.toStack(limit) : resource.toStack(amount));
            } else {
                existing.grow(reachedLimit ? limit : amount);
            }
            TransactionCallback.onSuccess(transaction, () -> onContentsChanged(slot));
            return reachedLimit ? limit : amount;
        }
        return 0;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);
        for (int i = 0; i < this.stacks.size(); i++) {
            int slot = i;
            ItemStack existing = this.stacks.get(i);

            if (existing.isEmpty())
                continue;

            int toExtract = Math.min((int) maxAmount, existing.getMaxStackSize());

            TransactionCallback.onSuccess(transaction, () -> onContentsChanged(slot));
            if (existing.getCount() <= toExtract) {
                updateSnapshots(transaction);
                this.stacks.set(i, ItemStack.EMPTY);
                return existing.getCount();
            } else {
                this.stacks.set(i, existing.copyWithCount(existing.getCount() - toExtract));
            }

            return existing.getCount();
        }
        return 0;
    }

    @Override
    public int getSlotLimit(int slot) {
        return Item.ABSOLUTE_MAX_STACK_SIZE;
    }

    public int getStackLimit(int slot, ItemVariant variant, long amount) {
        return Math.min(getSlotLimit(slot), variant.toStack().getMaxStackSize());
    }

    @Override
    public SingleSlotStorage<ItemVariant> getSlot(int slot) {
        return null;
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
        }
    }

    protected void onLoad() {}

    protected void onContentsChanged(int slot) {}

    public class ItemStackHandlerSlot implements SingleSlotStorage<ItemVariant> {
        private final int slot;

        public ItemStackHandlerSlot(int slot) {
            this.slot = slot;
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount);

            int amount;
            if (maxAmount >= Integer.MAX_VALUE)
                amount = Integer.MAX_VALUE;
            else
                amount = (int) maxAmount;

            if (!isItemValid(slot, resource, amount))
                return 0;

            ItemStack existing = stacks.get(slot);

            int limit = getSlotLimit(slot);

            if (!existing.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(resource.toStack(), existing))
                    return 0;

                limit -= existing.getCount();
            }

            if (limit <= 0)
                return 0;

            boolean reachedLimit = amount > limit;
            updateSnapshots(transaction);
            if (existing.isEmpty()) {
                stacks.set(slot, reachedLimit ? resource.toStack(limit) : resource.toStack(amount));
            } else {
                existing.grow(reachedLimit ? limit : amount);
            }
            TransactionCallback.onSuccess(transaction, () -> onContentsChanged(slot));
            return reachedLimit ? limit : amount;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount);
            ItemStack existing = stacks.get(slot);
            if (existing.isEmpty())
                return 0;

            int toExtract = Math.min((int) maxAmount, existing.getMaxStackSize());

            updateSnapshots(transaction);
            TransactionCallback.onSuccess(transaction, () -> onContentsChanged(slot));
            if (existing.getCount() <= toExtract) {
                stacks.set(slot, ItemStack.EMPTY);
                return existing.getCount();
            } else {
                stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
                return toExtract;
            }
        }

        @Override
        public boolean isResourceBlank() {
            return stacks.get(slot).isEmpty();
        }

        @Override
        public ItemVariant getResource() {
            return ItemVariant.of(stacks.get(slot));
        }

        @Override
        public long getAmount() {
            return stacks.get(slot).getCount();
        }

        @Override
        public long getCapacity() {
            return stacks.get(slot).getMaxStackSize();
        }
    }
}
