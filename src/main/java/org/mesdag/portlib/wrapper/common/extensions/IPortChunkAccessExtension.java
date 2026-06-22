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

    default boolean hasData(PortAttachmentType<?> type) {
        return PortChunkAccessExtension.hasAttach(self(), type);
    }

    default <T> boolean hasData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.hasAttach(self(), type);
    }

    default <T> T getData(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.getAttach(self(), type);
    }

    default <T> T getData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return PortChunkAccessExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T setData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortChunkAccessExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return PortChunkAccessExtension.removeAttach(self(), type);
    }

    default <T> @Nullable T removeData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortChunkAccessExtension.removeAttach(self(), type);
    }

    default void syncData(PortAttachmentType<?> type) {
        PortChunkAccessExtension.syncAttach(self(), type);
    }

    default void syncData(Supplier<PortAttachmentType<?>> type) {
        PortChunkAccessExtension.syncAttach(self(), type);
    }

    static IPortChunkAccessExtension of(ChunkAccess chunkAccess) {
        return (IPortChunkAccessExtension) chunkAccess;
    }
}
