package com.blakebr0.cucumber.mixin;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    /**
     * This is a slightly less disgusting hack for dynamically registering recipes. Doing it this way means I don't have
     * to replace the internal recipe maps.
     * <p>
     * Surely I'll come up with a better solution one day Clueless.
     */
    @Definition(id = "builder", method = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;")
    @Expression("? = builder()")
    @Inject(
            at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER),
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"
    )
    public void cucumber$apply(
            Map<ResourceLocation, JsonElement> p_44037_,
            ResourceManager p_44038_,
            ProfilerFiller p_44039_,
            CallbackInfo ci,
            @Local ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType,
            @Local ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byName
    ) {
        RecipeHelper.fireRecipeManagerLoadingEvent((RecipeManager) (Object) this, byType, byName);
    }
}
