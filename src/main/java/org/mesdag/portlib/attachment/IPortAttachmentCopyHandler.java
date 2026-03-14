package org.mesdag.portlib.attachment;

import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public interface IPortAttachmentCopyHandler<T> {
    @Nullable T copy(T attachment, IPortAttachmentHolder holder, PortRegistryAccess provider);
}
