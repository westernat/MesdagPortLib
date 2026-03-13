package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.diff.PortAttachmentInternals;

public interface IPortEntityExtension {
    static void copyAttachmentsFrom(Entity self, Entity other, boolean isDeath) {
        PortAttachmentInternals.copyEntityAttachments(other, self, isDeath);
    }
}
