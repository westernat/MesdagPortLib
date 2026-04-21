package PortLib.extensions.net.minecraft.world.level.chunk.ChunkAccess;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@Extension
public class PortChunkAccessExtension {
    // region Attachment

    public static boolean hasAttaches(@This ChunkAccess thiz) {
        return CPortAttachmentHolder.of(thiz).hasAttaches();
    }

    public static boolean hasAttach(@This ChunkAccess thiz, PortAttachmentType<?> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> boolean hasAttach(@This ChunkAccess thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> T getAttach(@This ChunkAccess thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> T getAttach(@This ChunkAccess thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This ChunkAccess thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This ChunkAccess thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This ChunkAccess thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This ChunkAccess thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T setAttach(@This ChunkAccess thiz, PortAttachmentType<T> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T setAttach(@This ChunkAccess thiz, Supplier<PortAttachmentType<T>> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T removeAttach(@This ChunkAccess thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static <T> @Nullable T removeAttach(@This ChunkAccess thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static void syncAttach(@This ChunkAccess thiz, PortAttachmentType<?> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    public static void syncAttach(@This ChunkAccess thiz, Supplier<? extends PortAttachmentType<?>> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    // endregion Attachment
}
