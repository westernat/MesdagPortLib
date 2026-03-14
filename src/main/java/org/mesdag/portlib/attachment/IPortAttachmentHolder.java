package org.mesdag.portlib.attachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;
import java.util.function.Supplier;

public interface IPortAttachmentHolder {
    boolean hasAttachments();

    boolean hasData(PortAttachmentType<?> type);

    default <T> boolean hasData(Supplier<PortAttachmentType<T>> type) {
        return hasData(type.get());
    }

    <T> T getData(PortAttachmentType<T> type);

    default <T> T getData(Supplier<PortAttachmentType<T>> type) {
        return getData(type.get());
    }

    default <T> Optional<T> getExistingData(PortAttachmentType<T> type) {
        return Optional.ofNullable(getExistingDataOrNull(type));
    }

    default <T> Optional<T> getExistingData(Supplier<PortAttachmentType<T>> type) {
        return getExistingData(type.get());
    }

    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return getExistingData(type).orElse(null);
    }

    default <T> @Nullable T getExistingDataOrNull(Supplier<PortAttachmentType<T>> type) {
        return getExistingDataOrNull(type.get());
    }

    <T> @Nullable T setData(PortAttachmentType<T> type, T data);

    default <T> @Nullable T setData(Supplier<PortAttachmentType<T>> type, T data) {
        return setData(type.get(), data);
    }

    <T> @Nullable T removeData(PortAttachmentType<T> type);

    default <T> @Nullable T removeData(Supplier<PortAttachmentType<T>> type) {
        return removeData(type.get());
    }

    default void syncData(PortAttachmentType<?> type) {}

    default void syncData(Supplier<? extends PortAttachmentType<?>> type) {
        syncData(type.get());
    }

    @Diff
    default IAttachmentHolder unwrap() {
        return new IAttachmentHolder() {
            @Override
            public boolean hasAttachments() {
                return IPortAttachmentHolder.this.hasAttachments();
            }

            @Override
            public boolean hasData(AttachmentType<?> type) {
                return IPortAttachmentHolder.this.hasData(PortAttachmentType.wrap(type));
            }

            @Override
            public <T> T getData(AttachmentType<T> type) {
                return IPortAttachmentHolder.this.getData(PortAttachmentType.wrap(type));
            }

            @Override
            public <T> @Nullable T setData(AttachmentType<T> type, T data) {
                return IPortAttachmentHolder.this.setData(PortAttachmentType.wrap(type), data);
            }

            @Override
            public <T> @Nullable T removeData(AttachmentType<T> type) {
                return IPortAttachmentHolder.this.removeData(PortAttachmentType.wrap(type));
            }

            @Override
            public void syncData(AttachmentType<?> type) {
                IPortAttachmentHolder.this.syncData(PortAttachmentType.wrap(type));
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
        public boolean hasAttachments() {
            return delegate.hasAttachments();
        }

        @Override
        public boolean hasData(PortAttachmentType<?> type) {
            return delegate.hasData(type.unwrap());
        }

        @Override
        public <T> T getData(PortAttachmentType<T> type) {
            return delegate.getData(type.unwrap());
        }

        @Override
        public <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
            return delegate.setData(type.unwrap(), data);
        }

        @Override
        public <T> @Nullable T removeData(PortAttachmentType<T> type) {
            return delegate.removeData(type.unwrap());
        }

        @Override
        public void syncData(PortAttachmentType<?> type) {
            delegate.syncData(type.unwrap());
        }

        @Override
        public IAttachmentHolder unwrap() {
            return delegate;
        }
    }
}
