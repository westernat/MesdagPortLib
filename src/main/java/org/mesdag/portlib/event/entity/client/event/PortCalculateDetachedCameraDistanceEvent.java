package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.Camera;
import net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortCalculateDetachedCameraDistanceEvent extends PortEvent {
    private final CalculateDetachedCameraDistanceEvent e;

    @Diff
    public PortCalculateDetachedCameraDistanceEvent(CalculateDetachedCameraDistanceEvent e) {
        super(e);
        this.e = e;
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
        PortEventHooks.register(CalculateDetachedCameraDistanceEvent.class, PortCalculateDetachedCameraDistanceEvent.class, PortCalculateDetachedCameraDistanceEvent::new);
    }
}