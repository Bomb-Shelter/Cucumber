package com.blakebr0.cucumber.crafting.recipe;

import com.blakebr0.cucumber.init.ModRecipeSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

public class ShapedNoMirrorRecipe extends ShapedRecipe {
    private final ItemStack result;

    public ShapedNoMirrorRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inventory, Level level) {
        for (int i = 0; i <= inventory.width() - getWidth(); i++) {
            for (int j = 0; j <= inventory.height() - getHeight(); j++) {
                if (this.checkMatch(inventory, i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_SHAPED_NO_MIRROR.get();
    }

    private boolean checkMatch(CraftingInput inventory, int x, int y) {
        for (var i = 0; i < inventory.width(); i++) {
            for (var j = 0; j < inventory.height(); j++) {
                var k = i - x;
                var l = j - y;
                var ingredient = Ingredient.EMPTY;

                if (k >= 0 && l >= 0 && k < getWidth() && l < getHeight()) {
                    ingredient = this.getIngredients().get(k + l * getWidth());
                }

                if (!ingredient.test(inventory.getItem(i + j * inventory.width()))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static class Serializer implements RecipeSerializer<ShapedNoMirrorRecipe> {
        public static final MapCodec<ShapedNoMirrorRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                        ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", Boolean.TRUE).forGetter(ShapedRecipe::showNotification)
                ).apply(builder, ShapedNoMirrorRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ShapedNoMirrorRecipe> STREAM_CODEC = StreamCodec.of(
                ShapedNoMirrorRecipe.Serializer::toNetwork, ShapedNoMirrorRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<ShapedNoMirrorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapedNoMirrorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ShapedNoMirrorRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var group = buffer.readUtf();
            var category = buffer.readEnum(CraftingBookCategory.class);
            var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            var result = ItemStack.STREAM_CODEC.decode(buffer);
            var showNotification = buffer.readBoolean();

            return new ShapedNoMirrorRecipe(group, category, pattern, result, showNotification);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapedNoMirrorRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.showNotification());
        }
    }
}
