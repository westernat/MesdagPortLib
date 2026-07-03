package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

public interface IPortClientLevelExtension {
    default void addEntity(Entity entity) {
        ((ClientLevel) this).addEntity(entity.getId(), entity);
    }
}
