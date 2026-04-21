package PortLib.extensions.net.minecraft.world.level.block.entity.BlockEntity;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@Extension
public class PortBlockEntityExtension {
    // region Attachment

    public static boolean hasAttaches(@This BlockEntity thiz) {
        return CPortAttachmentHolder.of(thiz).hasAttaches();
    }

    public static boolean hasAttach(@This BlockEntity thiz, PortAttachmentType<?> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> boolean hasAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> T getAttach(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> T getAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T setAttach(@This BlockEntity thiz, PortAttachmentType<T> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T setAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T removeAttach(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static <T> @Nullable T removeAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static void syncAttach(@This BlockEntity thiz, PortAttachmentType<?> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    public static void syncAttach(@This BlockEntity thiz, Supplier<? extends PortAttachmentType<?>> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    // endregion Attachment
}
