package com.blakebr0.cucumber.client.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.fabricators_of_create.porting_lib.client_extensions.IClientItemExtensions;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class WateringCanClientItemExtensions implements IClientItemExtensions {
    public static final IClientItemExtensions INSTANCE = new WateringCanClientItemExtensions();

    public boolean applyForgeHandTransform(PoseStack matrix, LocalPlayer player, HumanoidArm arm, ItemStack stack, float partialTick, float equipProcess, float swingProcess) {
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0) {
            float f = (float) (player.getUseItemRemainingTicks() % 20);
            float f1 = f - partialTick + 1.0F;
            float f2 = 1.0F - f1 / 20.0F;
            float f7 = -10.0F + 10.0F * Mth.cos(f2 * 2.0F * (float) Math.PI);

            matrix.mulPose(Axis.XP.rotationDegrees(f7));
        }

        return false;
    }
}
