package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityMountEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityMountEvent extends PortEntityEvent<EntityMountEvent> implements IPortCancellableEvent {
    @Diff
    public PortEntityMountEvent(EntityMountEvent e) {
        super(e);
    }
    public boolean isMounting() {
        return e.isMounting();
    }
    public boolean isDismounting() {
        return e.isDismounting();
    }

    public Entity getEntityMounting() {
        return e.getEntityMounting();
    }

    public Entity getEntityBeingMounted() {
        return e.getEntityBeingMounted();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    static {
        PortEventHooks.register();
    }
}
