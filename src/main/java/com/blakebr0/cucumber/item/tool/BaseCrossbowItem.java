package com.blakebr0.cucumber.item.tool;

import com.blakebr0.cucumber.iface.ICustomBow;
import io.github.fabricators_of_create.porting_lib.item.extensions.CustomArrowItem;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class BaseCrossbowItem extends CrossbowItem implements ICustomBow, CustomArrowItem {
    public BaseCrossbowItem(Function<Properties, Properties> properties) {
        super(properties.apply(new Properties()));
    }

    @Override // copied from CrossbowItem#releaseUsing
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        // change: account for draw speed multiplier
        int i = (int) ((this.getUseDuration(stack, entity) - timeLeft) * this.getDrawSpeedMulti(stack));
        float f = getPowerForTime(i, stack, entity);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(entity, stack)) {
            var sounds = this.getChargingSounds(stack);

            sounds.end().ifPresent(
                    sound -> level.playSound(
                            null,
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            sound.value(),
                            entity.getSoundSource(),
                            1.0F,
                            1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                    )
            );
        }
    }

    @Override // copied from CrossbowItem#onUseTick
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int timeLeft) {
        if (!level.isClientSide) {
            var sounds = this.getChargingSounds(stack);
            float f = (float) ((int) ((this.getUseDuration(stack, entity) - timeLeft) * this.getDrawSpeedMulti(stack))) / (float) getChargeDuration(stack, entity);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                sounds.start()
                        .ifPresent(
                                p_352849_ -> level.playSound(
                                        null, entity.getX(), entity.getY(), entity.getZ(), p_352849_.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                                )
                        );
            }

            if (f >= 0.5F && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                sounds.mid()
                        .ifPresent(
                                p_352855_ -> level.playSound(
                                        null, entity.getX(), entity.getY(), entity.getZ(), p_352855_.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                                )
                        );
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
        return false;
    }

    public static ItemPropertyFunction getPullPropertyGetter() {
        return (stack, level, entity, _unused) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return BaseCrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) * ((ICustomBow) stack.getItem()).getDrawSpeedMulti(stack) / (float) BaseCrossbowItem.getChargeDuration(stack, entity);
            }
        };
    }

    public static ItemPropertyFunction getPullingPropertyGetter() {
        return (stack, level, entity, _unused) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        };
    }

    public static ItemPropertyFunction getChargedPropertyGetter() {
        return (stack, level, entity, _unused) -> {
            return entity != null && BaseCrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        };
    }

    public static ItemPropertyFunction getFireworkPropertyGetter() {
        return (stack, level, entity, _unused) -> {
            var projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
            return projectiles != null && projectiles.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        };
    }
}
