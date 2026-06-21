package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.block.entity.BlockEntity.PortBlockEntityExtension;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortBlockEntityExtension {

    private BlockEntity self() {
        return (BlockEntity) this;
    }

    default boolean hasAttachments() {
        return PortBlockEntityExtension.hasAttaches(self());
    }

    default boolean hasAttachment(PortAttachmentType<?> type) {
        return PortBlockEntityExtension.hasAttach(self(), type);
    }

    default <T> boolean hasAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.hasAttach(self(), type);
    }

    default <T> T getAttachment(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.getAttach(self(), type);
    }

    default <T> T getAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T setAttachment(PortAttachmentType<T> type, T data) {
        return PortBlockEntityExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T setAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortBlockEntityExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T removeAttachment(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.removeAttach(self(), type);
    }

    default <T> @Nullable T removeAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.removeAttach(self(), type);
    }

    default void syncAttachment(PortAttachmentType<?> type) {
        PortBlockEntityExtension.syncAttach(self(), type);
    }

    default void syncAttachment(Supplier<PortAttachmentType<?>> type) {
        PortBlockEntityExtension.syncAttach(self(), type);
    }

    static IPortBlockEntityExtension of(BlockEntity blockEntity) {
        return (IPortBlockEntityExtension) blockEntity;
    }
}
