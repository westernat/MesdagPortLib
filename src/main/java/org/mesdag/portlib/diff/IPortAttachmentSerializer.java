package org.mesdag.portlib.diff;

import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

@Diff
public interface IPortAttachmentSerializer<S extends Tag, T> {
    T read(IPortAttachmentHolder holder, S tag, PortRegistryAccess provider);

    @Nullable S write(T attachment, PortRegistryAccess provider);
}
