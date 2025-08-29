package com.blakebr0.cucumber.client.handler;

import com.blakebr0.cucumber.iface.ICustomBow;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ComputeFovModifierEvent;
import net.minecraft.util.Mth;

public final class BowFOVHandler {
	public static void onFOVUpdate(ComputeFovModifierEvent event) {
		var entity = event.getPlayer();
		var stack = entity.getUseItem();

		if (!stack.isEmpty()) {
			var item = stack.getItem();
			if (item instanceof ICustomBow bow && bow.hasFOVChange()) {
				float f = Mth.clamp((stack.getUseDuration(event.getPlayer()) - entity.getUseItemRemainingTicks()) * bow.getDrawSpeedMulti(stack) / 20.0F, 0, 1.0F);

				event.setNewFovModifier(event.getNewFovModifier() - (f * f * 0.15F));
			}
		}
	}
}
