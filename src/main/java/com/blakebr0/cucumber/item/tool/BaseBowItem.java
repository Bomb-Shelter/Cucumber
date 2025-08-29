package com.blakebr0.cucumber.item.tool;

import com.blakebr0.cucumber.iface.ICustomBow;
import io.github.fabricators_of_create.porting_lib.item.extensions.CustomArrowItem;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Function;

public class BaseBowItem extends BowItem implements ICustomBow, CustomArrowItem {
    public BaseBowItem(Function<Properties, Properties> properties) {
        super(properties.apply(new Properties()));
    }

    @Override // copied from BowItem#releaseUsing
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);

            // change: account for draw speed multiplier
            int i = (int) ((this.getUseDuration(stack, entity) - timeLeft) * this.getDrawSpeedMulti(stack));
//            i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
            if (i < 0) return;

            float f = getPowerForTime(i);
            if (!((double) f < 0.1)) {
                List<ItemStack> list = draw(stack, itemstack, player);
                if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
                    this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == 1.0F, null);
                }

                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ARROW_SHOOT,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                );
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        arrow.setBaseDamage(arrow.getBaseDamage() + this.getBonusDamage(weaponStack));
        return arrow;
    }

    @Override
    public boolean hasFOVChange() {
        return true;
    }

    public static ItemPropertyFunction getPullPropertyGetter() {
        return (stack, level, entity, _unused) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) * ((ICustomBow) stack.getItem()).getDrawSpeedMulti(stack) / 20.0F;
            }
        };
    }

    public static ItemPropertyFunction getPullingPropertyGetter() {
        return (stack, level, entity, _unused) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        };
    }
}
