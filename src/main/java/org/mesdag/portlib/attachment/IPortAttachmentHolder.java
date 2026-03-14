package org.mesdag.portlib.attachment;

import org.jetbrains.annotations.Nullable;

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
}
