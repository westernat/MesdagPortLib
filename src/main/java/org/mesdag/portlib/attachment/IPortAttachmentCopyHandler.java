package org.mesdag.portlib.attachment;

import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public interface IPortAttachmentCopyHandler<T> {
    @Nullable T copy(T attachment, IPortAttachmentHolder holder, HolderLookup.Provider provider);

    @Diff
    default IAttachmentCopyHandler<T> unwrap() {
        return (attachment, holder, provider) -> copy(attachment, IPortAttachmentHolder.wrap(holder), provider);
    }

    @Diff
    static <T> IPortAttachmentCopyHandler<T> wrap(IAttachmentCopyHandler<T> delegate) {
        return new Delegate<>(delegate);
    }

    @Diff
    record Delegate<T>(IAttachmentCopyHandler<T> delegate) implements IPortAttachmentCopyHandler<T> {
        @Override
        public @Nullable T copy(T attachment, IPortAttachmentHolder holder, HolderLookup.Provider provider) {
            return delegate.copy(attachment, holder.unwrap(), provider);
        }

        @Override
        public IAttachmentCopyHandler<T> unwrap() {
            return delegate;
        }
    }
}
