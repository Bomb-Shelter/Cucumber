package com.blakebr0.cucumber.init;

import com.blakebr0.cucumber.Cucumber;
import com.blakebr0.cucumber.crafting.conditions.FeatureFlagCondition;
import com.mojang.serialization.MapCodec;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import io.github.fabricators_of_create.porting_lib.resources.conditions.ICondition;
import io.github.fabricators_of_create.porting_lib.resources.conditions.PortingLibConditions;

public final class ModConditionSerializers {
    public static final DeferredRegister<MapCodec<? extends ICondition>> REGISTRY = DeferredRegister.create(PortingLibConditions.CONDITION_SERIALIZERS, Cucumber.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> FEATURE_FLAG = REGISTRY.register("feature_flag", () -> FeatureFlagCondition.CODEC);
}
