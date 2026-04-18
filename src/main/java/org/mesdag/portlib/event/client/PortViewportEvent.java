package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.ViewportEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortViewportEvent<E extends ViewportEvent> extends PortEvent<E> {
    @Diff
    public PortViewportEvent(E e) {
        super(e);
    }

    public GameRenderer getRenderer() {
        return e.getRenderer();
    }

    public Camera getCamera() {
        return e.getCamera();
    }

    public double getPartialTick() {
        return e.getPartialTick();
    }

    public static class PortRenderFog extends PortViewportEvent<ViewportEvent.RenderFog> implements IPortCancellableEvent {
        @Diff
        public PortRenderFog(ViewportEvent.RenderFog e) {
            super(e);
        }

        public FogMode getMode() {
            return e.getMode();
        }

        public FogType getType() {
            return e.getType();
        }

        public float getFarPlaneDistance() {
            return e.getFarPlaneDistance();
        }

        public float getNearPlaneDistance() {
            return e.getNearPlaneDistance();
        }

        public FogShape getFogShape() {
            return e.getFogShape();
        }

        public void setFarPlaneDistance(float distance) {
            e.setFarPlaneDistance(distance);
        }

        public void setNearPlaneDistance(float distance) {
            e.setNearPlaneDistance(distance);
        }

        public void setFogShape(FogShape shape) {
            e.setFogShape(shape);
        }

        public void scaleFarPlaneDistance(float factor) {
            e.scaleFarPlaneDistance(factor);
        }

        public void scaleNearPlaneDistance(float factor) {
            e.scaleNearPlaneDistance(factor);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortComputeFogColor extends PortViewportEvent<ViewportEvent.ComputeFogColor> {
        @Diff
        public PortComputeFogColor(ViewportEvent.ComputeFogColor e) {
            super(e);
        }

        public float getRed() {
            return e.getRed();
        }

        public void setRed(float red) {
            e.setRed(red);
        }

        public float getGreen() {
            return e.getGreen();
        }

        public void setGreen(float green) {
            e.setGreen(green);
        }

        public float getBlue() {
            return e.getBlue();
        }

        public void setBlue(float blue) {
            e.setBlue(blue);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortComputeCameraAngles extends PortViewportEvent<ViewportEvent.ComputeCameraAngles> {
        @Diff
        public PortComputeCameraAngles(ViewportEvent.ComputeCameraAngles e) {
            super(e);
        }

        public float getYaw() {
            return e.getYaw();
        }

        public void setYaw(float yaw) {
            e.setYaw(yaw);
        }

        public float getPitch() {
            return e.getPitch();
        }

        public void setPitch(float pitch) {
            e.setPitch(pitch);
        }

        public float getRoll() {
            return e.getRoll();
        }

        public void setRoll(float roll) {
            e.setRoll(roll);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortComputeFov extends PortViewportEvent<ViewportEvent.ComputeFov> {
        @Diff
        public PortComputeFov(ViewportEvent.ComputeFov e) {
            super(e);
        }

        public double getFOV() {
            return e.getFOV();
        }

        public void setFOV(double fov) {
            e.setFOV(fov);
        }

        public boolean usedConfiguredFov() {
            return e.usedConfiguredFov();
        }

        static {
            PortEventHooks.register();
        }
    }
}
