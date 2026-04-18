package org.mesdag.portlib.event.client;

import net.minecraft.client.Camera;
import net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortCalculateDetachedCameraDistanceEvent extends PortEvent<CalculateDetachedCameraDistanceEvent> {
    @Diff
    public PortCalculateDetachedCameraDistanceEvent(CalculateDetachedCameraDistanceEvent e) {
        super(e);
    }

    public Camera getCamera() {
        return e.getCamera();
    }

    public boolean isCameraFlipped() {
        return e.isCameraFlipped();
    }

    public float getEntityScalingFactor() {
        return e.getEntityScalingFactor();
    }

    public float getDistance() {
        return e.getDistance();
    }

    public void setDistance(float distance) {
        e.setDistance(distance);
    }

    static {
        PortEventHooks.register();
    }
}
