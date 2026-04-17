package org.mesdag.portlib.attachment;

import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

public interface IPortAttachmentCopyHandler<T> {
    @Nullable T copy(T attachment, IPortAttachmentHolder holder, HolderLookup.Provider provider);
}
