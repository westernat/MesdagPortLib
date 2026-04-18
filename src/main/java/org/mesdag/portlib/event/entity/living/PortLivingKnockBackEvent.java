package org.mesdag.portlib.event.entity.living;

import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingKnockBackEvent extends PortLivingEvent<LivingKnockBackEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingKnockBackEvent(LivingKnockBackEvent e) {
        super(e);
    }

    public float getStrength() {
        return e.getStrength();
    }

    public double getRatioX() {
        return e.getRatioX();
    }

    public double getRatioZ() {
        return e.getRatioZ();
    }

    public float getOriginalStrength() {
        return e.getOriginalStrength();
    }

    public double getOriginalRatioX() {
        return e.getOriginalRatioX();
    }

    public double getOriginalRatioZ() {
        return e.getOriginalRatioZ();
    }

    public void setStrength(float strength) {
        e.setStrength(strength);
    }

    public void setRatioX(double ratioX) {
        e.setRatioX(ratioX);
    }

    public void setRatioZ(double ratioZ) {
        e.setRatioZ(ratioZ);
    }

    static {
        PortEventHooks.register();
    }
}
