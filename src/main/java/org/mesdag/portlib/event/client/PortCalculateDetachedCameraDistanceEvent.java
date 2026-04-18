package org.mesdag.portlib.event.client;

import net.minecraft.client.Camera;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;

public class PortCalculateDetachedCameraDistanceEvent extends Event {
    private final Camera camera;
    private final boolean cameraFlipped;
    private final float entityScale;

    private float distance;

    @Diff
    public PortCalculateDetachedCameraDistanceEvent(Camera camera, boolean cameraFlipped, float entityScale, float distance) {
        this.camera = camera;
        this.cameraFlipped = cameraFlipped;
        this.entityScale = entityScale;
        this.distance = distance;
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean isCameraFlipped() {
        return cameraFlipped;
    }

    public float getEntityScalingFactor() {
        return entityScale;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
