package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public enum PortEntityAttachment {
    PASSENGER(Fallback.AT_HEIGHT),
    VEHICLE(Fallback.AT_FEET),
    NAME_TAG(Fallback.AT_HEIGHT),
    WARDEN_CHEST(Fallback.AT_CENTER);

    private final Fallback fallback;

    PortEntityAttachment(Fallback fallback) {
        this.fallback = fallback;
    }

    public List<Vec3> createFallbackPoints(float width, float height) {
        return fallback.create(width, height);
    }

    public interface Fallback {
        List<Vec3> ZERO = List.of(Vec3.ZERO);
        Fallback AT_FEET = (width, height) -> ZERO;
        Fallback AT_HEIGHT = (width, height) -> List.of(new Vec3(0.0, height, 0.0));
        Fallback AT_CENTER = (width, height) -> List.of(new Vec3(0.0, height * 0.5, 0.0));

        List<Vec3> create(float width, float height);
    }
}
