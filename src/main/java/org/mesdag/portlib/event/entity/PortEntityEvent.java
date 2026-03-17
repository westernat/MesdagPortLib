package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.event.PortEvent;

public abstract class PortEntityEvent extends PortEvent {
    public abstract Entity getEntity();
}
