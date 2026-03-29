package org.mesdag.portlib.event.entity.living;

import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingHealEvent extends PortLivingEvent<LivingHealEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingHealEvent(LivingHealEvent e) {
        super(e);
    }

    public float getAmount() {
        return e.getAmount();
    }

    public void setAmount(float amount) {
        e.setAmount(amount);
    }

    static {
        PortEventHooks.register(LivingHealEvent.class, PortLivingHealEvent.class, PortLivingHealEvent::new);
    }
}
