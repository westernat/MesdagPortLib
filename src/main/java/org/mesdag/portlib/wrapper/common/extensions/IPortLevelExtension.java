package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.Level.PortLevelExtension;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortLevelExtension {
    private Level self() {
        return (Level) this;
    }

    default boolean hasAttachments() {
        return PortLevelExtension.hasAttaches(self());
    }

    default boolean hasData(PortAttachmentType<?> type) {
        return PortLevelExtension.hasAttach(self(), type);
    }

    default <T> boolean hasData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.hasAttach(self(), type);
    }

    default <T> T getData(PortAttachmentType<T> type) {
        return PortLevelExtension.getAttach(self(), type);
    }

    default <T> T getData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortAttachmentType<T> type) {
        return PortLevelExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return PortLevelExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return PortLevelExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T setData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortLevelExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return PortLevelExtension.removeAttach(self(), type);
    }

    default <T> @Nullable T removeData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.removeAttach(self(), type);
    }

    default void syncData(PortAttachmentType<?> type) {
        PortLevelExtension.syncAttach(self(), type);
    }

    default void syncData(Supplier<PortAttachmentType<?>> type) {
        PortLevelExtension.syncAttach(self(), type);
    }

    static IPortLevelExtension of(Level level) {
        return (IPortLevelExtension) level;
    }
}
