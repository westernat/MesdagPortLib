package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortLevelExtension {
    private Level self() {
        return (Level) this;
    }

    default boolean hasAttachments() {
        return ((CPortAttachmentHolder) this).hasAttaches();
    }

    default boolean hasData(PortAttachmentType<?> type) {
        return ((CPortAttachmentHolder) this).hasAttach(type);
    }

    default <T> boolean hasData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return hasData(type.get());
    }

    default <T> T getData(PortAttachmentType<T> type) {
        return ((CPortAttachmentHolder) this).getAttach(type);
    }

    default <T> T getData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return getData(type.get());
    }

    default <T> Optional<T> getExistingData(PortAttachmentType<T> type) {
        return ((CPortAttachmentHolder) this).getExistingAttach(type);
    }

    default <T> Optional<T> getExistingData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return getExistingData(type.get());
    }

    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return ((CPortAttachmentHolder) this).getExistingAttachOrNull(type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return getExistingDataOrNull(type.get());
    }

    default <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return ((CPortAttachmentHolder) this).setAttach(type, data);
    }

    default <T> @Nullable T setData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return setData(type.get(), data);
    }

    default <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return ((CPortAttachmentHolder) this).removeAttach(type);
    }

    default <T> @Nullable T removeData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return removeData(type.get());
    }

    default void syncData(PortAttachmentType<?> type) {
        ((CPortAttachmentHolder) this).syncAttach(type);
    }

    default void syncData(Supplier<PortAttachmentType<?>> type) {
        syncData(type.get());
    }

    static IPortLevelExtension of(Level level) {
        return (IPortLevelExtension) level;
    }
}
