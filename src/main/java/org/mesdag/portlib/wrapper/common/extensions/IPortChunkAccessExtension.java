package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.chunk.ChunkAccess.PortChunkAccessExtension;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortChunkAccessExtension {

    private ChunkAccess self() {
        return (ChunkAccess) this;
    }

    default boolean hasAttachments() {
        return PortChunkAccessExtension.hasAttaches(self());
    }

    default boolean hasAttachment(PortAttachmentType<?> type) {
        return PortChunkAccessExtension.hasAttach(self(), type);
    }

    default <T> boolean hasAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.hasAttach(self(), type);
    }

    default <T> T getAttachment(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.getAttach(self(), type);
    }

    default <T> T getAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T setAttachment(PortAttachmentType<T> type, T data) {
        return PortChunkAccessExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T setAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortChunkAccessExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T removeAttachment(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.removeAttach(self(), type);
    }

    default <T> @Nullable T removeAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.removeAttach(self(), type);
    }

    default void syncAttachment(PortAttachmentType<?> type) {
        PortChunkAccessExtension.syncAttach(self(), type);
    }

    default void syncAttachment(Supplier<PortAttachmentType<?>> type) {
        PortChunkAccessExtension.syncAttach(self(), type);
    }

    static IPortChunkAccessExtension of(ChunkAccess chunkAccess) {
        return (IPortChunkAccessExtension) chunkAccess;
    }
}
