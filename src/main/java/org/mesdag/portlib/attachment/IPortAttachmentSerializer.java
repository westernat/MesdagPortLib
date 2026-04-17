package org.mesdag.portlib.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public interface IPortAttachmentSerializer<S extends Tag, T> {
    T read(IPortAttachmentHolder holder, S tag, HolderLookup.Provider provider);

    @Nullable S write(T attachment, HolderLookup.Provider provider);
}
