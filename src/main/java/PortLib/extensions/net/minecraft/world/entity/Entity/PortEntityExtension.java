package PortLib.extensions.net.minecraft.world.entity.Entity;

import com.google.common.base.Supplier;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.diff.attachment.PortAttachmentInternals;

import javax.annotation.Nullable;
import java.util.Optional;

@Extension
public class PortEntityExtension {
    // region Attachment

    public static void copyAttachmentsFrom(@This Entity thiz, Entity other, boolean isDeath) {
        PortAttachmentInternals.copyEntityAttachments(other, thiz, isDeath);
    }

    public static boolean hasAttaches(@This Entity thiz) {
        return CPortAttachmentHolder.of(thiz).hasAttaches();
    }

    public static boolean hasAttach(@This Entity thiz, PortAttachmentType<?> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> boolean hasAttach(@This Entity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> T getAttach(@This Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> T getAttach(@This Entity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(@This Entity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This Entity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T setAttach(@This Entity thiz, PortAttachmentType<T> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T setAttach(@This Entity thiz, Supplier<PortAttachmentType<T>> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T removeAttach(@This Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static <T> @Nullable T removeAttach(@This Entity thiz, Supplier<PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static void syncAttach(@This Entity thiz, PortAttachmentType<?> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    public static void syncAttach(@This Entity thiz, Supplier<PortAttachmentType<?>> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    // endregion Attachment

    public static RegistryAccess registryAccess(@This Entity thiz) {
        return thiz.level().registryAccess();
    }
}
