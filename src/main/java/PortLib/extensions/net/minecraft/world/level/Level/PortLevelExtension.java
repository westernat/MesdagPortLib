package PortLib.extensions.net.minecraft.world.level.Level;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.level.Level;
import org.mesdag.portlib.attachment.PortAttachmentType;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@Extension
public class PortLevelExtension {
    // region Attachment

    public static boolean hasAttaches(@This Level thiz) {
        return thiz.hasAttachments();
    }

    public static boolean hasAttach(@This Level thiz, PortAttachmentType<?> type) {
        return thiz.hasData(type.unwrap());
    }

    public static <T> boolean hasAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.hasData(type.get().unwrap());
    }

    public static <T> T getAttach(@This Level thiz, PortAttachmentType<T> type) {
        return thiz.getData(type.unwrap());
    }

    public static <T> T getAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.getData(type.get().unwrap());
    }

    public static <T> Optional<T> getExistingAttach(@This Level thiz, PortAttachmentType<T> type) {
        return thiz.getExistingData(type.unwrap());
    }

    public static <T> Optional<T> getExistingAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.getExistingData(type.get().unwrap());
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This Level thiz, PortAttachmentType<T> type) {
        return thiz.getExistingDataOrNull(type.unwrap());
    }

    public static <T> @Nullable T getExistingAttachOrNull(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.getExistingDataOrNull(type.get().unwrap());
    }

    public static <T> @Nullable T setAttach(@This Level thiz, PortAttachmentType<T> type, T data) {
        return thiz.setData(type.unwrap(), data);
    }

    public static <T> @Nullable T setAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type, T data) {
        return thiz.setData(type.get().unwrap(), data);
    }

    public static <T> @Nullable T removeAttach(@This Level thiz, PortAttachmentType<T> type) {
        return thiz.removeData(type.unwrap());
    }

    public static <T> @Nullable T removeAttach(@This Level thiz, Supplier<PortAttachmentType<T>> type) {
        return thiz.removeData(type.get().unwrap());
    }

    public static void syncAttach(@This Level thiz, PortAttachmentType<?> type) {
        thiz.syncData(type.unwrap());
    }

    public static void syncAttach(@This Level thiz, Supplier<? extends PortAttachmentType<?>> type) {
        thiz.syncData(type.get().unwrap());
    }

    // endregion Attachment
}
