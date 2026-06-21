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

    default boolean hasAttachment(PortAttachmentType<?> type) {
        return PortLevelExtension.hasAttach(self(), type);
    }

    default <T> boolean hasAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.hasAttach(self(), type);
    }

    default <T> T getAttachment(PortAttachmentType<T> type) {
        return PortLevelExtension.getAttach(self(), type);
    }

    default <T> T getAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortAttachmentType<T> type) {
        return PortLevelExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortAttachmentType<T> type) {
        return PortLevelExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T setAttachment(PortAttachmentType<T> type, T data) {
        return PortLevelExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T setAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortLevelExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T removeAttachment(PortAttachmentType<T> type) {
        return PortLevelExtension.removeAttach(self(), type);
    }

    default <T> @Nullable T removeAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortLevelExtension.removeAttach(self(), type);
    }

    default void syncAttachment(PortAttachmentType<?> type) {
        PortLevelExtension.syncAttach(self(), type);
    }

    default void syncAttachment(Supplier<PortAttachmentType<?>> type) {
        PortLevelExtension.syncAttach(self(), type);
    }

    static IPortLevelExtension of(Level level) {
        return (IPortLevelExtension) level;
    }
}
