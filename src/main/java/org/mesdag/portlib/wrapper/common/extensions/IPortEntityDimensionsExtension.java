package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.EntityDimensions;
import org.mesdag.portlib.diff.IPortEntityDimensions;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachments;

public interface IPortEntityDimensionsExtension {
    private EntityDimensions self() {
        return (EntityDimensions) this;
    }

    default float width() {
        return self().width;
    }

    default float height() {
        return self().height;
    }

    default boolean fixed() {
        return self().fixed;
    }

    default float eyeHeight() {
        return ((IPortEntityDimensions) this).portlib$getEyeHeight();
    }

    default PortEntityAttachments attachments() {
        return ((IPortEntityDimensions) this).portlib$getAttachments();
    }

    default EntityDimensions withEyeHeight(float eyeHeight) {
        EntityDimensions dimensions = new EntityDimensions(width(), height(), fixed());
        IPortEntityDimensions port = IPortEntityDimensions.of(dimensions);
        port.portlib$setAttachments(attachments());
        port.portlib$setEyeHeight(eyeHeight);
        return dimensions;
    }

    default EntityDimensions withAttachments(PortEntityAttachments.Builder attachments) {
        EntityDimensions dimensions = new EntityDimensions(width(), height(), fixed());
        IPortEntityDimensions port = IPortEntityDimensions.of(dimensions);
        port.portlib$setAttachments(attachments.build(width(), height()));
        port.portlib$setEyeHeight(eyeHeight());
        return dimensions;
    }
}
