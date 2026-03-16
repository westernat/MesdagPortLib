package org.mesdag.portlib.component;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.function.Supplier;

public interface PortDataComponentHolder {
    default <T> @Nullable T get(PortDataComponentType<T> type) {
        return null;
    }

    default <T> @Nullable T get(Supplier<PortDataComponentType<T>> type) {
        return null;
    }

    default <T> @Nullable T set(PortDataComponentType<T> type, T value) {
        return null;
    }

    default <T> @Nullable T set(Supplier<PortDataComponentType<T>> type, T value) {
        return null;
    }

    default <T> @Nullable T remove(PortDataComponentType<T> type) {
        return null;
    }

    default <T> @Nullable T remove(Supplier<PortDataComponentType<T>> type) {
        return null;
    }

    default <T> T getOrDefault(PortDataComponentType<? extends T> type, T defaultValue) {
        return defaultValue;
    }

    default <T> T getOrDefault(Supplier<PortDataComponentType<? extends T>> type, T defaultValue) {
        return defaultValue;
    }

    default <T> boolean has(PortDataComponentType<T> type) {
        return false;
    }

    default <T> boolean has(Supplier<PortDataComponentType<T>> type) {
        return false;
    }

    static PortDataComponentHolder of(ItemStack stack) {
        return new Delegate(stack);
    }

    @Diff
    record Delegate(ItemStack delegate) implements PortDataComponentHolder {
        @Override
        public <T> @Nullable T get(PortDataComponentType<T> type) {
            return delegate.get(type.unwrap());
        }

        @Override
        public <T> @Nullable T get(Supplier<PortDataComponentType<T>> type) {
            return delegate.get(type.get().unwrap());
        }

        @Override
        public <T> @Nullable T set(PortDataComponentType<T> type, T value) {
            return delegate.set(type.unwrap(), value);
        }

        @Override
        public <T> @Nullable T set(Supplier<PortDataComponentType<T>> type, T value) {
            return delegate.set(type.get().unwrap(), value);
        }

        @Override
        public <T> @Nullable T remove(PortDataComponentType<T> type) {
            return delegate.remove(type.unwrap());
        }

        @Override
        public <T> @Nullable T remove(Supplier<PortDataComponentType<T>> type) {
            return delegate.remove(type.get().unwrap());
        }

        @Override
        public <T> T getOrDefault(PortDataComponentType<? extends T> type, T defaultValue) {
            return delegate.getOrDefault(type.unwrap(), defaultValue);
        }

        @Override
        public <T> T getOrDefault(Supplier<PortDataComponentType<? extends T>> type, T defaultValue) {
            return delegate.getOrDefault(type.get().unwrap(), defaultValue);
        }

        @Override
        public <T> boolean has(PortDataComponentType<T> type) {
            return delegate.has(type.unwrap());
        }

        @Override
        public <T> boolean has(Supplier<PortDataComponentType<T>> type) {
            return delegate.has(type.get().unwrap());
        }
    }
}
