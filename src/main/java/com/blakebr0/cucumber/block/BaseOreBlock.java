package com.blakebr0.cucumber.block;

import io.github.fabricators_of_create.porting_lib.blocks.extensions.CustomExpBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class BaseOreBlock extends BaseBlock implements CustomExpBlock {
    private final IntProvider xpRange;

    public BaseOreBlock(Function<Properties, Properties> properties, int minExp, int maxExp) {
        super(properties.compose(Properties::requiresCorrectToolForDrops));
        this.xpRange = UniformInt.of(minExp, maxExp);
    }

    public BaseOreBlock(SoundType sound, float hardness, float resistance, int minExp, int maxExp) {
        this(p -> p.sound(sound).strength(hardness, resistance), minExp, maxExp);
    }

    @Override
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool) {
        var hasSilkTouch = tool.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .getLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH)) > 0;
        return hasSilkTouch ? 0 : this.xpRange.sample(level.getRandom());
    }
}
