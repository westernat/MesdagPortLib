package org.mesdag.portlib.event.entity.client.event;

import net.neoforged.neoforge.client.event.CalculatePlayerTurnEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortCalculatePlayerTurnEvent extends PortEvent {
    private final CalculatePlayerTurnEvent e;

    @Diff
    public PortCalculatePlayerTurnEvent(CalculatePlayerTurnEvent e) {
        super(e);
        this.e = e;
    }

    public double getMouseSensitivity() {
        return e.getMouseSensitivity();
    }

    public void setMouseSensitivity(double mouseSensitivity) {
        e.setMouseSensitivity(mouseSensitivity);
    }

    public boolean getCinematicCameraEnabled() {
        return e.getCinematicCameraEnabled();
    }

    public void setCinematicCameraEnabled(boolean cinematicCameraEnabled) {
        e.setCinematicCameraEnabled(cinematicCameraEnabled);
    }

    static {
        PortEventHooks.register(CalculatePlayerTurnEvent.class, PortCalculatePlayerTurnEvent.class, PortCalculatePlayerTurnEvent::new);
    }
}