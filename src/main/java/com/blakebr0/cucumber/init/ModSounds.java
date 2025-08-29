package com.blakebr0.cucumber.init;

import com.blakebr0.cucumber.Cucumber;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Cucumber.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> WATERING_CAN = REGISTRY.register("watering_can", () -> SoundEvent.createVariableRangeEvent(Cucumber.resource("watering_can")));
}
