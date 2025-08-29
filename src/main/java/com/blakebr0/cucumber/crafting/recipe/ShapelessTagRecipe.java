package com.blakebr0.cucumber.crafting.recipe;

import com.blakebr0.cucumber.crafting.OutputResolver;
import com.blakebr0.cucumber.init.ModRecipeSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ShapelessTagRecipe extends ShapelessRecipe {
    private final OutputResolver outputResolver;
    private ItemStack result;

    public ShapelessTagRecipe(String group, CraftingBookCategory category, NonNullList<Ingredient> inputs, OutputResolver outputResolver) {
        super(group, category, ItemStack.EMPTY, inputs);
        this.outputResolver = outputResolver;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider lookup) {
        if (this.result == null) {
            this.result = this.outputResolver.resolve();
        }

        return this.result;
    }

    @Override
    public boolean isSpecial() {
        if (this.result == null) {
            this.result = this.outputResolver.resolve();
        }

        return this.result.isEmpty();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_SHAPELESS_TAG.get();
    }

    public static class Serializer implements RecipeSerializer<ShapelessTagRecipe> {
        public static final MapCodec<ShapelessTagRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::getGroup),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapelessRecipe::category),
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            Ingredient[] ingredients = field.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
                                            if (ingredients.length == 0) {
                                                return DataResult.error(() -> "No ingredients for shapeless recipe");
                                            } else {
//                                                return ingredients.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()
//                                                        ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()))
                                                        return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(ShapelessRecipe::getIngredients),
                        OutputResolver.Tag.CODEC.fieldOf("result").forGetter(recipe -> (OutputResolver.Tag) recipe.outputResolver)
                ).apply(builder, ShapelessTagRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessTagRecipe> STREAM_CODEC = StreamCodec.of(
                ShapelessTagRecipe.Serializer::toNetwork, ShapelessTagRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<ShapelessTagRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapelessTagRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ShapelessTagRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var group = buffer.readUtf(32767);
            var size = buffer.readVarInt();
            var category = buffer.readEnum(CraftingBookCategory.class);

            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            ingredients.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

            var result = OutputResolver.create(buffer);

            return new ShapelessTagRecipe(group, category, ingredients, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapelessTagRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());

            for (var ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.outputResolver.resolve());
            buffer.writeBoolean(recipe.showNotification());
        }
    }
}
