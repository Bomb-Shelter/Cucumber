package com.blakebr0.cucumber.init;

import com.blakebr0.cucumber.Cucumber;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

public final class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Cucumber.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> WATERING_CAN_FILLED = REGISTRY.register("watering_can_filled", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
}
