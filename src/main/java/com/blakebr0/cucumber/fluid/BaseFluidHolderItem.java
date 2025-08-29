package com.blakebr0.cucumber.fluid;

import com.blakebr0.cucumber.iface.IFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class BaseFluidHolderItem implements SingleSlotStorage<FluidVariant> {
    private final IFluidHolder holder;
    private final ContainerItemContext context;

    public BaseFluidHolderItem(ContainerItemContext context, IFluidHolder holder) {
        this.context = context;
        this.holder = holder;
    }

    @Override
    public long getCapacity() {
        return holder.getCapacity(context);
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return this.holder.fill(this.context, resource, maxAmount, transaction) ;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return this.holder.drain(this.context, maxAmount, transaction).getAmount();
    }

    @Override
    public boolean isResourceBlank() {
        return this.holder.isResourceBlank(this.context);
    }

    @Override
    public FluidVariant getResource() {
        return holder.getResource(this.context);
    }

    @Override
    public long getAmount() {
        return this.holder.getAmount(this.context);
    }
}
