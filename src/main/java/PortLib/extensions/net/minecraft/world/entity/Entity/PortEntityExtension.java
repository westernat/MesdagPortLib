package PortLib.extensions.net.minecraft.world.entity.Entity;

import com.google.common.base.Supplier;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.diff.attachment.PortAttachmentInternals;
import org.mesdag.portlib.registries.PortRegistryEntry;

import javax.annotation.Nullable;
import java.util.Optional;

public class PortEntityExtension {
    // region Attachment

    public static void copyAttachmentsFrom(Entity thiz, Entity other, boolean isDeath) {
        PortAttachmentInternals.copyEntityAttachments(other, thiz, isDeath);
    }

    public static boolean hasAttaches(Entity thiz) {
        return CPortAttachmentHolder.of(thiz).hasAttaches();
    }

    public static boolean hasAttach(Entity thiz, PortAttachmentType<?> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> boolean hasAttach(Entity thiz, PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).hasAttach(type);
    }

    public static <T> T getAttach(Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> T getAttach(Entity thiz, PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> Optional<T> getExistingAttach(Entity thiz, PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttach(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T getExistingAttachOrNull(Entity thiz, PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).getExistingAttachOrNull(type);
    }

    public static <T> @Nullable T setAttach(Entity thiz, PortAttachmentType<T> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T setAttach(Entity thiz, PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return CPortAttachmentHolder.of(thiz).setAttach(type, data);
    }

    public static <T> @Nullable T removeAttach(Entity thiz, PortAttachmentType<T> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static <T> @Nullable T removeAttach(Entity thiz, PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return CPortAttachmentHolder.of(thiz).removeAttach(type);
    }

    public static void syncAttach(Entity thiz, PortAttachmentType<?> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    public static void syncAttach(Entity thiz, Supplier<PortAttachmentType<?>> type) {
        CPortAttachmentHolder.of(thiz).syncAttach(type);
    }

    // endregion Attachment

    public static RegistryAccess registryAccess(Entity thiz) {
        return thiz.level().registryAccess();
    }
}
