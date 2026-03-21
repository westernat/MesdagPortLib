package org.mesdag.portlib.event.entity.item;

import net.minecraft.world.entity.item.ItemEntity;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortItemEvent extends PortEntityEvent {
    private final ItemEntity itemEntity;

    protected PortItemEvent(ItemEntity itemEntity) {
        super(itemEntity);
        this.itemEntity = itemEntity;
    }

    @Override
    public ItemEntity getEntity() {
        return itemEntity;
    }
}
