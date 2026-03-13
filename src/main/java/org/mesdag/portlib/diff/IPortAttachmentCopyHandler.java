package org.mesdag.portlib.diff;

import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

@Diff
public interface IPortAttachmentCopyHandler<T> {
    @Nullable T copy(T attachment, IPortAttachmentHolder holder, PortRegistryAccess provider);
}
