package org.mesdag.portlib.event.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.CalculatePlayerTurnEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public class PortCalculatePlayerTurnEvent extends PortEvent<CalculatePlayerTurnEvent> {
    @Diff
    public PortCalculatePlayerTurnEvent(CalculatePlayerTurnEvent e) {
        super(e);
    }

    public double getMouseSensitivity() {
        return e.getMouseSensitivity();
    }

    public void setMouseSensitivity(double mouseSensitivity) {
        e.setMouseSensitivity(mouseSensitivity);
    }

    public PortTriState getCinematicCameraEnabled() {
        if (e.getCinematicCameraEnabled() == Minecraft.getInstance().options.smoothCamera) {
            return PortTriState.DEFAULT;
        }
        return e.getCinematicCameraEnabled() ? PortTriState.TRUE : PortTriState.FALSE;
    }

    public void setCinematicCameraEnabled(PortTriState cinematicCameraEnabled) {
        if (!cinematicCameraEnabled.isDefault()) {
            e.setCinematicCameraEnabled(cinematicCameraEnabled.isTrue());
        }
    }

    static {
        PortEventHooks.register();
    }
}
