package com.blakebr0.cucumber.init;

import com.blakebr0.cucumber.Cucumber;
import com.blakebr0.cucumber.crafting.ingredient.IngredientWithCount;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;

public final class ModIngredientTypes {
    public static final IngredientWithCount.Serializer WITH_COUNT = new IngredientWithCount.Serializer();

    public static void register() {
        CustomIngredientSerializer.register(WITH_COUNT);
    }
}
