package com.blakebr0.cucumber.iface;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;

public interface IFluidHolder {
	int getCapacity(ContainerItemContext context);

	boolean isResourceBlank(ContainerItemContext context);

	FluidVariant getResource(ContainerItemContext context);

	long getAmount(ContainerItemContext context);

	FluidStack getFluid(ContainerItemContext context);

	int fill(ContainerItemContext context, FluidVariant fluid, long amount, TransactionContext tx);

	FluidStack drain(ContainerItemContext context, long amount, TransactionContext tx);
}
