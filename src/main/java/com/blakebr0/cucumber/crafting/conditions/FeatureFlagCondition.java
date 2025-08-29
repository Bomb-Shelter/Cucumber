package com.blakebr0.cucumber.crafting.conditions;

import com.blakebr0.cucumber.util.FeatureFlag;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.resources.conditions.ICondition;
import net.minecraft.resources.ResourceLocation;

public record FeatureFlagCondition(ResourceLocation flag) implements ICondition {
    public static final MapCodec<FeatureFlagCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("flag").forGetter(FeatureFlagCondition::flag)
            ).apply(builder, FeatureFlagCondition::new)
    );

    @Override
    public boolean test(IContext context) {
        var flag = FeatureFlag.from(this.flag);
        return flag.isEnabled();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
