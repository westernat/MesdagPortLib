package org.mesdag.portlib.event.client;

import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public class PortCalculatePlayerTurnEvent extends Event {
    private double mouseSensitivity;
    private PortTriState cinematicCameraEnabled;

    @Diff
    public PortCalculatePlayerTurnEvent(double mouseSensitivity) {
        setMouseSensitivity(mouseSensitivity);
        setCinematicCameraEnabled(PortTriState.DEFAULT);
    }

    public double getMouseSensitivity() {
        return mouseSensitivity;
    }

    public void setMouseSensitivity(double mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    public PortTriState getCinematicCameraEnabled() {
        return cinematicCameraEnabled;
    }

    public void setCinematicCameraEnabled(PortTriState cinematicCameraEnabled) {
        this.cinematicCameraEnabled = cinematicCameraEnabled;
    }
}
