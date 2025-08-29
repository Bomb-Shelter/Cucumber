package com.blakebr0.cucumber.event;

import io.github.fabricators_of_create.porting_lib.core.event.BaseEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class ScytheHarvestCropEvent extends BaseEvent {
    public static final Event<Callback> EVENT = EventFactory.createArrayBacked(Callback.class, callbacks -> event -> {
        for (var callback : callbacks) {
            callback.onScytheHarvestCrop(event);
        }
    });
    private final LevelAccessor level;
    private final BlockPos pos;
    private final BlockState state;
    private final ItemStack stack;
    private final Player player;

    public ScytheHarvestCropEvent(LevelAccessor level, BlockPos pos, BlockState state, ItemStack stack, Player player) {
        this.pos = pos;
        this.level = level;
        this.state = state;
        this.stack = stack;
        this.player = player;
    }

    public LevelAccessor getLevel()
    {
        return this.level;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public BlockState getState()
    {
        return this.state;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void sendEvent() {
        EVENT.invoker().onScytheHarvestCrop(this);
    }

    interface Callback {
        void onScytheHarvestCrop(ScytheHarvestCropEvent event);
    }

}
