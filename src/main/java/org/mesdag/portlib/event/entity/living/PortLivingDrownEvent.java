package org.mesdag.portlib.event.entity.living;

import net.neoforged.neoforge.event.entity.living.LivingDrownEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingDrownEvent extends PortLivingEvent<LivingDrownEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingDrownEvent(LivingDrownEvent e) {
        super(e);
    }

    public boolean isDrowning() {
        return e.isDrowning();
    }

    public void setDrowning(boolean isDrowning) {
        e.setDrowning(isDrowning);
    }

    public float getDamageAmount() {
        return e.getDamageAmount();
    }

    public void setDamageAmount(float damageAmount) {
        e.setDamageAmount(damageAmount);
    }

    public int getBubbleCount() {
        return e.getBubbleCount();
    }

    public void setBubbleCount(int bubbleCount) {
        e.setBubbleCount(bubbleCount);
    }

    static {
        PortEventHooks.register(LivingDrownEvent.class, PortLivingDrownEvent.class, PortLivingDrownEvent::new);
    }
}
