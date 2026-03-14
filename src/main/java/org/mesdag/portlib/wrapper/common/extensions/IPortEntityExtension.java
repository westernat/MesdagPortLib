package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.Entity;

public interface IPortEntityExtension {
    static void copyAttachmentsFrom(Entity self, Entity other, boolean isDeath) {
        self.copyAttachmentsFrom(other, isDeath);
    }
}
