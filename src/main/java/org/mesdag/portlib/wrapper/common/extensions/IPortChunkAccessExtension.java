package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortChunkAccessExtension {
    private ChunkAccess self() {
        return (ChunkAccess) this;
    }

    default boolean hasAttachments() {
        return CPortAttachmentHolder.of(self()).hasAttaches();
    }

    default boolean hasData(PortAttachmentType<?> type) {
        return CPortAttachmentHolder.of(self()).hasAttach(type);
    }

    default <T> boolean hasData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(self()).hasAttach(type);
    }

    default <T> T getData(PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(self()).getAttach(type);
    }

    default <T> T getData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(self()).getAttach(type);
    }

    default <T> Optional<T> getExistingData(PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(self()).getExistingAttach(type);
    }

    default <T> Optional<T> getExistingData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(self()).getExistingAttach(type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(self()).getExistingAttachOrNull(type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(self()).getExistingAttachOrNull(type);
    }

    default <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return CPortAttachmentHolder.of(self()).setAttach(type, data);
    }

    default <T> @Nullable T setData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return CPortAttachmentHolder.of(self()).setAttach(type, data);
    }

    default <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(self()).removeAttach(type);
    }

    default <T> @Nullable T removeData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(self()).removeAttach(type);
    }

    default void syncData(PortAttachmentType<?> type) {
        CPortAttachmentHolder.of(self()).syncAttach(type);
    }

    default void syncData(Supplier<PortAttachmentType<?>> type) {
        CPortAttachmentHolder.of(self()).syncAttach(type);
    }

    static IPortChunkAccessExtension of(ChunkAccess chunkAccess) {
        return (IPortChunkAccessExtension) chunkAccess;
    }
}
