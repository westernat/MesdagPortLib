package org.mesdag.portlib.attachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;
import java.util.function.Supplier;

public interface IPortAttachmentHolder {
    boolean hasAttaches();

    boolean hasAttach(PortAttachmentType<?> type);

    default <T> boolean hasAttach(Supplier<PortAttachmentType<T>> type) {
        return hasAttach(type.get());
    }

    <T> T getAttach(PortAttachmentType<T> type);

    default <T> T getAttach(Supplier<PortAttachmentType<T>> type) {
        return getAttach(type.get());
    }

    default <T> Optional<T> getExistingAttach(PortAttachmentType<T> type) {
        return Optional.ofNullable(getExistingAttachOrNull(type));
    }

    default <T> Optional<T> getExistingAttach(Supplier<PortAttachmentType<T>> type) {
        return getExistingAttach(type.get());
    }

    default <T> @Nullable T getExistingAttachOrNull(PortAttachmentType<T> type) {
        return getExistingAttach(type).orElse(null);
    }

    default <T> @Nullable T getExistingAttachOrNull(Supplier<PortAttachmentType<T>> type) {
        return getExistingAttachOrNull(type.get());
    }

    <T> @Nullable T setAttach(PortAttachmentType<T> type, T data);

    default <T> @Nullable T setAttach(Supplier<PortAttachmentType<T>> type, T data) {
        return setAttach(type.get(), data);
    }

    <T> @Nullable T removeAttach(PortAttachmentType<T> type);

    default <T> @Nullable T removeAttach(Supplier<PortAttachmentType<T>> type) {
        return removeAttach(type.get());
    }

    default void syncAttach(PortAttachmentType<?> type) {}

    default void syncAttach(Supplier<? extends PortAttachmentType<?>> type) {
        syncAttach(type.get());
    }

    @Diff
    default IAttachmentHolder unwrap() {
        return new IAttachmentHolder() {
            @Override
            public boolean hasAttachments() {
                return IPortAttachmentHolder.this.hasAttaches();
            }

            @Override
            public boolean hasData(AttachmentType<?> type) {
                return IPortAttachmentHolder.this.hasAttach(type.wrap());
            }

            @Override
            public <T> T getData(AttachmentType<T> type) {
                return IPortAttachmentHolder.this.getAttach(type.wrap());
            }

            @Override
            public <T> @Nullable T setData(AttachmentType<T> type, T data) {
                return IPortAttachmentHolder.this.setAttach(type.wrap(), data);
            }

            @Override
            public <T> @Nullable T removeData(AttachmentType<T> type) {
                return IPortAttachmentHolder.this.removeAttach(type.wrap());
            }

            @Override
            public void syncData(AttachmentType<?> type) {
                IPortAttachmentHolder.this.syncAttach(type.wrap());
            }
        };
    }

    @Diff
    static IPortAttachmentHolder wrap(IAttachmentHolder delegate) {
        return new Delegate(delegate);
    }

    @Diff
    record Delegate(IAttachmentHolder delegate) implements IPortAttachmentHolder {
        @Override
        public boolean hasAttaches() {
            return delegate.hasAttachments();
        }

        @Override
        public boolean hasAttach(PortAttachmentType<?> type) {
            return delegate.hasData(type.unwrap());
        }

        @Override
        public <T> T getAttach(PortAttachmentType<T> type) {
            return delegate.getData(type.unwrap());
        }

        @Override
        public <T> @Nullable T setAttach(PortAttachmentType<T> type, T data) {
            return delegate.setData(type.unwrap(), data);
        }

        @Override
        public <T> @Nullable T removeAttach(PortAttachmentType<T> type) {
            return delegate.removeData(type.unwrap());
        }

        @Override
        public void syncAttach(PortAttachmentType<?> type) {
            delegate.syncData(type.unwrap());
        }

        @Override
        public IAttachmentHolder unwrap() {
            return delegate;
        }
    }
}
