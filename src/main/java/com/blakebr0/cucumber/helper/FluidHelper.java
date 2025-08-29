package com.blakebr0.cucumber.helper;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class FluidHelper {
	public static FluidStack getFluidFromStack(ItemStack stack) {
		var handler = FluidStorage.ITEM.find(stack, ContainerItemContext.withConstant(stack));
		return handler == null ? FluidStack.EMPTY : new FluidStack(StorageUtil.findExtractableContent(handler, null));
	}

	public static long getFluidAmount(ItemStack stack) {
		var fluid = getFluidFromStack(stack);
		return fluid == null ? 0 : fluid.getAmount();
	}

	public static ItemStack getFilledBucket(FluidStack fluid, Item bucket, int capacity, ContainerItemContext context) {
		var filledBucket = new ItemStack(bucket);
		var fluidContents = fluid.copyWithAmount(capacity);

		var tank = FluidStorage.ITEM.find(filledBucket, context);
		if (tank != null) {
			try (Transaction tx = Transaction.openOuter()) {
				tank.insert(fluidContents.getVariant(), fluidContents.getAmount(), tx);
				tx.commit();
			}
		}

		return filledBucket;
	}

	@Deprecated(forRemoval = true)
	public static long toBuckets(long i) {
		return i - (i % 1000);
	}

	public static long toDroplets(long i) {
		return i - (i % FluidConstants.BUCKET);
	}
}
