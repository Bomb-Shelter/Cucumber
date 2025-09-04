package com.blakebr0.cucumber.event;

import io.github.fabricators_of_create.porting_lib.core.event.BaseEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.crafting.RecipeManager;

public class RecipeManagerLoadedEvent extends BaseEvent {
    public static final Event<Callback> EVENT = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (var callback : callbacks) {
            callback.onRecipeManagerLoaded(event);
        }
    });

    private final RecipeManager manager;

    public RecipeManagerLoadedEvent(RecipeManager manager) {
        this.manager = manager;
    }

    public RecipeManager getRecipeManager() {
        return this.manager;
    }

    @Override
    public void sendEvent() {
        EVENT.invoker().onRecipeManagerLoaded(this);
    }

    public interface Callback {
        void onRecipeManagerLoaded(RecipeManagerLoadedEvent event);
    }
}
