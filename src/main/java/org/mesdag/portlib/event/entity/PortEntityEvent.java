package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import org.mesdag.portlib.event.PortEvent;

public abstract class PortEntityEvent<E extends EntityEvent> extends PortEvent<E> {
    public PortEntityEvent(E e) {
        super(e);
    }

    public Entity getEntity() {
        return e.getEntity();
    }
}
