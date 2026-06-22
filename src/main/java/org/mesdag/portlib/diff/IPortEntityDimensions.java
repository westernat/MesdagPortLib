package org.mesdag.portlib.diff;

import net.minecraft.world.entity.EntityDimensions;
import org.mesdag.portlib.wrapper.common.extensions.IPortEntityDimensionsExtension;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachments;

public interface IPortEntityDimensions extends IPortEntityDimensionsExtension {
    PortEntityAttachments portlib$getAttachments();

    void portlib$setAttachments(PortEntityAttachments attachments);

    float portlib$getEyeHeight();

    void portlib$setEyeHeight(float eyeHeight);

    static IPortEntityDimensions of(EntityDimensions dimensions) {
        return (IPortEntityDimensions) dimensions;
    }
}
