package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortBlockDropsEvent extends PortBlockEvent<BlockDropsEvent> implements IPortCancellableEvent {
    @Diff
    public PortBlockDropsEvent(BlockDropsEvent e) {
        super(e);
    }

    public List<ItemEntity> getDrops() {
        return e.getDrops();
    }

    public @Nullable BlockEntity getBlockEntity() {
        return e.getBlockEntity();
    }

    public @Nullable Entity getBreaker() {
        return e.getBreaker();
    }

    public ItemStack getTool() {
        return e.getTool();
    }

    @Override
    public void setCanceled(boolean canceled) {
        IPortCancellableEvent.super.setCanceled(canceled);
        e.setCanceled(canceled);
    }

    @Override
    public ServerLevel getLevel() {
        return e.getLevel();
    }

    public int getDroppedExperience() {
        return e.getDroppedExperience();
    }

    public void setDroppedExperience(int experience) {
        e.setDroppedExperience(experience);
    }

    static {
        PortEventHooks.register(BlockDropsEvent.class, PortBlockDropsEvent.class, PortBlockDropsEvent::new);
    }
}
