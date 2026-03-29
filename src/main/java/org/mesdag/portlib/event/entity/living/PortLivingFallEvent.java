package org.mesdag.portlib.event.entity.living;

import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingFallEvent extends PortLivingEvent<LivingFallEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingFallEvent(LivingFallEvent e) {
        super(e);
    }

    public float getDistance() {
        return e.getDistance();
    }

    public void setDistance(float distance) {
        e.setDistance(distance);
    }

    public float getDamageMultiplier() {
        return e.getDamageMultiplier();
    }

    public void setDamageMultiplier(float damageMultiplier) {
        e.setDamageMultiplier(damageMultiplier);
    }

    static {
        PortEventHooks.register(LivingFallEvent.class, PortLivingFallEvent.class, PortLivingFallEvent::new);
    }
}
