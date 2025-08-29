package com.blakebr0.cucumber.client;

import com.blakebr0.cucumber.client.handler.BowFOVHandler;
import com.blakebr0.cucumber.client.handler.DataComponentTooltipHandler;
import com.blakebr0.cucumber.client.handler.TagTooltipHandler;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ComputeFovModifierEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class ClientCucumber implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ComputeFovModifierEvent.EVENT.register(BowFOVHandler::onFOVUpdate);
        ItemTooltipCallback.EVENT.register(TagTooltipHandler::onItemTooltip);
        ItemTooltipCallback.EVENT.register(DataComponentTooltipHandler::onItemTooltip);
    }
}
