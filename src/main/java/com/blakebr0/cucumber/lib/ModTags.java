package com.blakebr0.cucumber.lib;

import com.blakebr0.cucumber.Cucumber;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static final TagKey<Block> MINEABLE_WITH_PAXEL = TagKey.create(Registries.BLOCK, Cucumber.resource("mineable/paxel"));
    public static final TagKey<Block> MINEABLE_WITH_SICKLE = TagKey.create(Registries.BLOCK, Cucumber.resource("mineable/sickle"));
}
