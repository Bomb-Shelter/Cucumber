package com.blakebr0.cucumber.client.handler;

import com.blakebr0.cucumber.config.ModConfigs;
import com.blakebr0.cucumber.lib.Tooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class DataComponentTooltipHandler {
    public static void onItemTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipType, List<Component> lines) {
        if (!ModConfigs.ENABLE_DATA_COMPONENT_TOOLTIPS.get())
            return;

        if (Minecraft.getInstance().options.advancedItemTooltips) {
            var components = stack.getComponents();
            var tooltip = lines;

            if (Screen.hasAltDown()) {
                tooltip.add(Tooltips.DATA_COMPONENTS.build());

                for (TypedDataComponent<?> component : components) {
                    tooltip.add(createTextComponent(component));
                }
            } else {
                tooltip.add(Tooltips.HOLD_ALT_FOR_DATA_COMPONENTS.build());
            }
        }
    }

    private static Component createTextComponent(TypedDataComponent<?> component) {
        var text = " " + StringUtil.truncateStringIfNecessary(component.toString(), 50, true);
        return Component.literal(text).withStyle(ChatFormatting.DARK_GRAY);
    }
}
