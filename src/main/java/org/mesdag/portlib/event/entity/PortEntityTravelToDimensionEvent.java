package org.mesdag.portlib.event.entity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityTravelToDimensionEvent extends PortEntityEvent implements IPortCancellableEvent {

    private final EntityTravelToDimensionEvent e;

    @Diff
    public PortEntityTravelToDimensionEvent(EntityTravelToDimensionEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public ResourceKey<Level> getDimension() {
        return e.getDimension();
    }

    static {
        PortEventHooks.register(EntityTravelToDimensionEvent.class, PortEntityTravelToDimensionEvent.class, PortEntityTravelToDimensionEvent::new);
    }
}