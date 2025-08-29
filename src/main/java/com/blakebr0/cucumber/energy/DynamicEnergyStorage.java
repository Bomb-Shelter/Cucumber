package com.blakebr0.cucumber.energy;

public class DynamicEnergyStorage extends BaseEnergyStorage {
    private final long initialCapacity;

    public DynamicEnergyStorage(long capacity, Runnable onContentsChanged) {
        super(capacity, onContentsChanged);
        this.initialCapacity = capacity;
    }

    public void setMaxEnergyStorage(long capacity) {
        this.capacity = capacity;
    }

    public void setMaxEnergyStorage(double capacity) {
        this.capacity = (long) capacity;
    }

    public void resetMaxEnergyStorage() {
        this.capacity = this.initialCapacity;
    }
}
