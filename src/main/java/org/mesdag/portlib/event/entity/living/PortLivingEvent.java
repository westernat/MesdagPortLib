package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortLivingEvent<E extends LivingEvent> extends PortEntityEvent<E> {
    public PortLivingEvent(E e) {
        super(e);
    }

    @Override
    public LivingEntity getEntity() {
        return e.getEntity();
    }
}
