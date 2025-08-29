package com.blakebr0.cucumber.command;

import com.blakebr0.cucumber.Cucumber;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.util.Localizable;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.InteractionHand;
import team.reborn.energy.api.EnergyStorage;

public final class ModCommands {
    public static final LiteralArgumentBuilder<CommandSourceStack> ROOT = Commands.literal(Cucumber.MOD_ID);

    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {

        dispatcher.register(ROOT.then(Commands.literal("fillenergy").requires(source -> source.hasPermission(4))
                .then(Commands.literal("block").executes(context -> {
                    var source = context.getSource();
                    var level = source.getLevel();
                    var player = source.getPlayerOrException();
                    var trace = BlockHelper.rayTraceBlocks(level, player);
                    var pos = trace.getBlockPos();

                    var energy = EnergyStorage.SIDED.find(level, pos, trace.getDirection());
                    if (energy != null) {
                        if (energy.supportsInsertion()) {
                            try (Transaction tx = Transaction.openOuter()) {
                                energy.insert(Long.MAX_VALUE, tx);
                                tx.commit();
                            }
                            var message = Localizable.of("message.cucumber.filled_energy").args("block").build();

                            source.sendSuccess(() -> message, false);
                        }
                    } else {
                        var message = Localizable.of("message.cucumber.filled_energy_error").args("block").build();

                        source.sendFailure(message);
                    }

                    return 0;
                }))
                .then(Commands.literal("hand").executes(context -> {
                    var source = context.getSource();
                    var player = source.getPlayerOrException();
                    var stack = player.getItemInHand(InteractionHand.MAIN_HAND);

                    if (!stack.isEmpty()) {
                        var energy = EnergyStorage.ITEM.find(stack, ContainerItemContext.ofPlayerHand(player, InteractionHand.MAIN_HAND));

                        if (energy != null) {
                            if (energy.supportsInsertion()) {
                                try (Transaction tx = Transaction.openOuter()) {
                                    energy.insert(Long.MAX_VALUE, tx);
                                    tx.commit();
                                }

                                var message = Localizable.of("message.cucumber.filled_energy").args("item").build();

                                source.sendSuccess(() -> message, false);
                            }
                        } else {
                            var message = Localizable.of("message.cucumber.filled_energy_error").args("item").build();

                            source.sendFailure(message);
                        }
                    } else {
                        var message = Localizable.of("message.cucumber.filled_energy_error").args("item").build();

                        source.sendFailure(message);
                    }

                    return 0;
                }))
        ));
    }
}
