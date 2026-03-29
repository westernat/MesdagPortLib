package org.mesdag.portlib.event.entity.item;

import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.event.entity.item.ItemEvent;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortItemEvent<E extends ItemEvent> extends PortEntityEvent<E> {
    public PortItemEvent(E e) {
        super(e);
    }

    @Override
    public ItemEntity getEntity() {
        return e.getEntity();
    }
}
