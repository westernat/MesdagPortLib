package PortLib.extensions.net.minecraft.world.level.block.entity.BlockEntity;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.mesdag.portlib.attachment.PortAttachmentType;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@Extension
public class PortBlockEntityExtension {
    // region Attachment

    public static boolean hasAttaches(@This BlockEntity thiz) {
        return thiz.hasAttachments();
    }

    public static boolean hasAttach(@This BlockEntity thiz, PortAttachmentType<?> type) {
        return thiz.hasData(type.unwrap());
    }

    public static <T> boolean hasAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.hasData(type.get().unwrap());
    }

    public static <T> T getAttach(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return thiz.getData(type.unwrap());
    }

    public static <T> T getAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.getData(type.get().unwrap());
    }

    public static <T> Optional<T> getExistingAttach(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return thiz.getExistingData(type.unwrap());
    }

    public static <T> Optional<T> getExistingAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.getExistingData(type.get().unwrap());
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return thiz.getExistingDataOrNull(type.unwrap());
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.getExistingDataOrNull(type.get().unwrap());
    }

    public static <T> @Nullable T setAttach(@This BlockEntity thiz, PortAttachmentType<T> type, T data) {
        return thiz.setData(type.unwrap(), data);
    }

    public static <T> @Nullable T setAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type, T data) {
        return thiz.setData(type.get().unwrap(), data);
    }

    public static <T> @Nullable T removeAttach(@This BlockEntity thiz, PortAttachmentType<T> type) {
        return thiz.removeData(type.unwrap());
    }

    public static <T> @Nullable T removeAttach(@This BlockEntity thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.removeData(type.get().unwrap());
    }

    public static void syncAttach(@This BlockEntity thiz, PortAttachmentType<?> type) {
        thiz.syncData(type.unwrap());
    }

    public static void syncAttach(@This BlockEntity thiz, Supplier<? extends PortAttachmentType<?>> type) {
        thiz.syncData(type.get().unwrap());
    }

    // endregion Attachment
}
