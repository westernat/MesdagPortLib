package org.mesdag.portlib.component;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortPatchedDataComponentMap;

import java.util.function.Supplier;

public interface PortDataComponentHolder {
    @Diff
    PortPatchedDataComponentMap portlib$patch();

    default <T> @Nullable T get(PortDataComponentType<T> type) {
        return portlib$patch().get(type);
    }

    default <T> @Nullable T get(Supplier<PortDataComponentType<T>> type) {
        return portlib$patch().get(type);
    }

    default <T> @Nullable T set(PortDataComponentType<T> type, T value) {
        return portlib$patch().set(type, value);
    }

    default <T> @Nullable T set(Supplier<PortDataComponentType<T>> type, T value) {
        return portlib$patch().set(type, value);
    }

    default <T> @Nullable T remove(PortDataComponentType<T> type) {
        return portlib$patch().remove(type);
    }

    default <T> @Nullable T remove(Supplier<PortDataComponentType<T>> type) {
        return portlib$patch().remove(type);
    }

    default <T> T getOrDefault(PortDataComponentType<? extends T> type, T defaultValue) {
        return portlib$patch().getOrDefault(type, defaultValue);
    }

    default <T> T getOrDefault(Supplier<PortDataComponentType<? extends T>> type, T defaultValue) {
        return portlib$patch().getOrDefault(type, defaultValue);
    }

    default <T> boolean has(PortDataComponentType<T> type) {
        return portlib$patch().has(type);
    }

    default <T> boolean has(Supplier<PortDataComponentType<T>> type) {
        return portlib$patch().has(type);
    }

    static PortDataComponentHolder of(ItemStack stack) {
        return (PortDataComponentHolder) (Object) stack;
    }
}
