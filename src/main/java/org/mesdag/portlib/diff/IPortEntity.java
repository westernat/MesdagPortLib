package org.mesdag.portlib.diff;

import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.wrapper.common.extensions.IPortEntityExtension;

@Diff
public interface IPortEntity extends CPortAttachmentHolder, IPortEntityExtension {
    static IPortEntity of(Entity entity) {
        return (IPortEntity) entity;
    }
}
