package com.blakebr0.cucumber.event;

import io.github.fabricators_of_create.porting_lib.core.event.BaseEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

public class RecipeManagerLoadingEvent extends BaseEvent {
    public static final Event<Callback> EVENT = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (var callback : callbacks) {
            callback.onRecipeManagerLoading(event);
        }
    });
    private final RecipeManager manager;
    private final List<RecipeHolder<?>> recipes;

    public RecipeManagerLoadingEvent(RecipeManager manager, List<RecipeHolder<?>> recipes) {
        this.manager = manager;
        this.recipes = recipes;
    }

    public RecipeManager getRecipeManager() {
        return this.manager;
    }

    public void addRecipe(RecipeHolder<?> recipe) {
        this.recipes.add(recipe);
    }

    @Override
    public void sendEvent() {
        EVENT.invoker().onRecipeManagerLoading(this);
    }

    public interface Callback {
        void onRecipeManagerLoading(RecipeManagerLoadingEvent event);
    }

}
