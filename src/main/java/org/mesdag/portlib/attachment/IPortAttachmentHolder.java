package org.mesdag.portlib.attachment;

import org.jetbrains.annotations.Nullable;

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
}
