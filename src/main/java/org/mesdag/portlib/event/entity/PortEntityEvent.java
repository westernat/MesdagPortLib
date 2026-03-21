package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.event.PortEvent;

public abstract class PortEntityEvent extends PortEvent {
    private final Entity entity;

    protected PortEntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
