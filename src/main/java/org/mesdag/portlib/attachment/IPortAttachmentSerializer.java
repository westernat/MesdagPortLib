package org.mesdag.portlib.attachment;

import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public interface IPortAttachmentSerializer<S extends Tag, T> {
    T read(IPortAttachmentHolder holder, S tag, PortRegistryAccess provider);

    @Nullable S write(T attachment, PortRegistryAccess provider);
}
