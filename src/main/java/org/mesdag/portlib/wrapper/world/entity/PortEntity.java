package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.util.Final;

public interface PortEntity {
    private Entity self() {
        return (Entity) this;
    }

    default double getDefaultGravity() {
        return 0.0;
    }

    @Final
    default double getGravity() {
        return self().isNoGravity() ? 0.0 : getDefaultGravity();
    }

    default void applyGravity() {
        double d0 = getGravity();
        if (d0 != 0.0) {
            self().setDeltaMovement(self().getDeltaMovement().add(0.0, -d0, 0.0));
        }
    }
}
