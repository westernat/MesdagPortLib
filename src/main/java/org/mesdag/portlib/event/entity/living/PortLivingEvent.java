package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortLivingEvent extends PortEntityEvent {
    @Override
    public abstract LivingEntity getEntity();
}
