package com.blakebr0.cucumber;

import com.blakebr0.cucumber.client.handler.BowFOVHandler;
import com.blakebr0.cucumber.client.handler.DataComponentTooltipHandler;
import com.blakebr0.cucumber.client.handler.TagTooltipHandler;
import com.blakebr0.cucumber.command.ModCommands;
import com.blakebr0.cucumber.config.ModConfigs;
import com.blakebr0.cucumber.crafting.TagMapper;
import com.blakebr0.cucumber.init.ModConditionSerializers;
import com.blakebr0.cucumber.init.ModDataComponentTypes;
import com.blakebr0.cucumber.init.ModIngredientTypes;
import com.blakebr0.cucumber.init.ModRecipeSerializers;
import com.blakebr0.cucumber.init.ModSounds;
//import com.blakebr0.cucumber.util.FeatureFlagInitializer;
import io.github.fabricators_of_create.porting_lib.blocks.extensions.HarvestableBlock;
import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ModConfig;
import io.github.fabricators_of_create.porting_lib.resources.events.TagsUpdatedEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Cucumber implements ModInitializer {
	public static final String NAME = "ClientCucumber Library";
	public static final String MOD_ID = "cucumber";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public void onInitialize() {
		ModDataComponentTypes.REGISTRY.register();
		ModSounds.REGISTRY.register();
		ModConditionSerializers.REGISTRY.register();
		ModIngredientTypes.register();
		ModRecipeSerializers.REGISTRY.register();
		CommandRegistrationCallback.EVENT.register(ModCommands::onRegisterCommands);
		TagsUpdatedEvent.EVENT.register(TagMapper::onTagsUpdated);

		//FeatureFlagInitializer.init();

		ConfigRegistry.registerConfig(MOD_ID, ModConfig.Type.CLIENT, ModConfigs.CLIENT);
		ConfigRegistry.registerConfig(MOD_ID, ModConfig.Type.COMMON, ModConfigs.COMMON);
	}

    public static boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        if (state.getBlock() instanceof HarvestableBlock harvestable) {
            return harvestable.canHarvestBlock(state, level, pos, player);
        } else {
            return player.hasCorrectToolForDrops(state);
        }
    }

	public static ResourceLocation resource(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
