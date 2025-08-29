package com.blakebr0.cucumber.energy;

import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class BaseEnergyStorage extends SnapshotParticipant<Long> implements EnergyStorage {
    public long amount = 0;
    public long capacity;
    public final long maxInsert, maxExtract;
    private final Runnable onContentsChanged;

    public BaseEnergyStorage(long capacity, Runnable onContentsChanged) {
        this(capacity, capacity, capacity, onContentsChanged);
    }

    public BaseEnergyStorage(long capacity, long maxReceive, long maxExtract, Runnable onContentsChanged) {
        this(capacity, maxReceive, maxExtract, 0, onContentsChanged);
    }

    public BaseEnergyStorage(long capacity, long maxReceive, long maxExtract, long energy, Runnable onContentsChanged) {
        this.capacity = capacity;
        this.maxInsert = maxReceive;
        this.maxExtract = maxExtract;
        this.onContentsChanged = onContentsChanged;
    }

    @Override
    protected Long createSnapshot() {
        return amount;
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        amount = snapshot;
    }

    @Override
    public boolean supportsInsertion() {
        return maxInsert > 0;
    }

    @Override
    public long insert(long maxAmount, TransactionContext tx) {
        StoragePreconditions.notNegative(maxAmount);

        long inserted = Math.min(maxInsert, Math.min(maxAmount, capacity - amount));

        if (inserted > 0) {
            updateSnapshots(tx);
            amount += inserted;
            if (inserted != 0 && this.onContentsChanged != null)
                TransactionCallback.onSuccess(tx, this.onContentsChanged);
            return inserted;
        }

        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return maxExtract > 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext tx) {
        StoragePreconditions.notNegative(maxAmount);

        long extracted = Math.min(maxExtract, Math.min(maxAmount, amount));

        if (extracted > 0) {
            updateSnapshots(tx);
            amount -= extracted;
            if (extracted != 0 && this.onContentsChanged != null)
                TransactionCallback.onSuccess(tx, this.onContentsChanged);
            return extracted;
        }

        return 0;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    public void setEnergyStored(long energy) {
        this.amount = energy;

        if (this.onContentsChanged != null)
            this.onContentsChanged.run();
    }
}
