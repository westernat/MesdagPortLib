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

    default boolean hasData(PortAttachmentType<?> type) {
        return PortBlockEntityExtension.hasAttach(self(), type);
    }

    default <T> boolean hasData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.hasAttach(self(), type);
    }

    default <T> T getData(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.getAttach(self(), type);
    }

    default <T> T getData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return PortBlockEntityExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T setData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortBlockEntityExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return PortBlockEntityExtension.removeAttach(self(), type);
    }

    default <T> @Nullable T removeData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortBlockEntityExtension.removeAttach(self(), type);
    }

    default void syncData(PortAttachmentType<?> type) {
        PortBlockEntityExtension.syncAttach(self(), type);
    }

    default void syncData(Supplier<PortAttachmentType<?>> type) {
        PortBlockEntityExtension.syncAttach(self(), type);
    }

    static IPortBlockEntityExtension of(BlockEntity blockEntity) {
        return (IPortBlockEntityExtension) blockEntity;
    }
}
