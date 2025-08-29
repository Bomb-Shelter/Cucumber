package com.blakebr0.cucumber.compat.almostunified;

import com.almostreliable.unified.api.AlmostUnified;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AlmostUnifiedAdapter {
    public static boolean isLoaded() {
        return FabricLoader.getInstance().isModLoaded("almostunified");
    }

    public static Item getPreferredItemForTag(String tagId) {
        if (isLoaded()) {
            return Adapter.getPreferredItemForTag(TagKey.create(Registries.ITEM, ResourceLocation.parse(tagId)));
        }

        return null;
    }

    private static class Adapter {
        private static Item getPreferredItemForTag(TagKey<Item> tag) {
            return AlmostUnified.INSTANCE.getTagTargetItem(tag);
        }
    }
}
