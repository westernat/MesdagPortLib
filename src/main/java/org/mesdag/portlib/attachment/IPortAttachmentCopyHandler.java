package org.mesdag.portlib.attachment;

import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public interface IPortAttachmentCopyHandler<T> {
    @Nullable T copy(T attachment, IPortAttachmentHolder holder, PortRegistryAccess provider);

    @Diff
    default IAttachmentCopyHandler<T> unwrap() {
        return (attachment, holder, provider) -> copy(attachment, IPortAttachmentHolder.wrap(holder), new PortRegistryAccess(provider));
    }

    @Diff
    static <T> IPortAttachmentCopyHandler<T> wrap(IAttachmentCopyHandler<T> delegate) {
        return new Delegate<>(delegate);
    }

    @Diff
    record Delegate<T>(IAttachmentCopyHandler<T> delegate) implements IPortAttachmentCopyHandler<T> {
        @Override
        public @Nullable T copy(T attachment, IPortAttachmentHolder holder, PortRegistryAccess provider) {
            return delegate.copy(attachment, holder.unwrap(), provider);
        }

        @Override
        public IAttachmentCopyHandler<T> unwrap() {
            return delegate;
        }
    }
}
