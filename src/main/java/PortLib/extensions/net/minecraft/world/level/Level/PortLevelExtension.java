package PortLib.extensions.net.minecraft.world.level.Level;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.level.Level;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@Extension
public class PortLevelExtension {
    // region Attachment

    public static boolean hasAttaches(@This Level thiz) {
        return CPortAttachmentHolder.of(thiz).hasAttaches();
    }

    public static boolean hasAttach(@This Level thiz, PortAttachmentType<?> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> boolean hasAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> T getAttach(@This Level thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> T getAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This Level thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This Level thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T setAttach(@This Level thiz, PortAttachmentType<T> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T setAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T removeAttach(@This Level thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static <T> @Nullable T removeAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static void syncAttach(@This Level thiz, PortAttachmentType<?> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    public static void syncAttach(@This Level thiz, Supplier<? extends PortAttachmentType<?>> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    // endregion Attachment
}
