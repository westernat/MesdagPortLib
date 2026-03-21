package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortLivingEvent extends PortEntityEvent {
    private final LivingEntity living;

    public PortLivingEvent(LivingEntity living) {
        super(living);
        this.living = living;
    }

    @Override
    public LivingEntity getEntity() {
        return living;
    }
}
