package org.mesdag.portlib.attachment;

import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.util.Final;

import java.util.Optional;
import java.util.function.Supplier;

public interface IPortAttachmentHolder {
    boolean hasAttaches();

    boolean hasAttach(PortAttachmentType<?> type);

    default <T> boolean hasAttach(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return hasAttach(type.get());
    }

    <T> T getAttach(PortAttachmentType<T> type);

    default <T> T getAttach(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return getAttach(type.get());
    }

    default <T> Optional<T> getExistingAttach(PortAttachmentType<T> type) {
        return Optional.ofNullable(getExistingAttachOrNull(type));
    }

    default <T> Optional<T> getExistingAttach(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return getExistingAttach(type.get());
    }

    default <T> @Nullable T getExistingAttachOrNull(PortAttachmentType<T> type) {
        return getExistingAttach(type).orElse(null);
    }

    default <T> @Nullable T getExistingAttachOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return getExistingAttachOrNull(type.get());
    }

    <T> @Nullable T portlib$setAttach(PortAttachmentType<T> type, T data);

    @Final
    default <T> @Nullable T setAttach(PortAttachmentType<T> type, T data) {
        return portlib$setAttach(type, data);
    }

    default <T> @Nullable T setAttach(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return setAttach(type.get(), data);
    }

    <T> @Nullable T portlib$removeAttach(PortAttachmentType<T> type);

    @Final
    default <T> @Nullable T removeAttach(PortAttachmentType<T> type) {
        return portlib$removeAttach(type);
    }

    default <T> @Nullable T removeAttach(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return removeAttach(type.get());
    }

    void portlib$syncAttach(PortAttachmentType<?> type);

    @Final
    default void syncAttach(PortAttachmentType<?> type) {
        portlib$syncAttach(type);
    }

    default void syncAttach(Supplier<? extends PortAttachmentType<?>> type) {
        syncAttach(type.get());
    }
}
