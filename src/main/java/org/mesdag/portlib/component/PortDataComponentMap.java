package org.mesdag.portlib.component;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.ImmutableTransformSet;

import java.util.Set;

public interface PortDataComponentMap {
    PortDataComponentMap EMPTY = new PortDataComponentMap() {
        @Override
        public <T> @Nullable T get(PortDataComponentType<T> type) {
            return null;
        }

        @Override
        public Set<PortDataComponentType<?>> keySet() {
            return Set.of();
        }
    };

    <T> @Nullable T get(PortDataComponentType<T> type);

    Set<PortDataComponentType<?>> keySet();

    @Diff
    default DataComponentMap unwrap() {
        return new DataComponentMap() {
            @Override
            public @Nullable <T> T get(DataComponentType<? extends T> component) {
                return PortDataComponentMap.this.get(PortDataComponentType.wrap(component));
            }

            @Override
            public Set<DataComponentType<?>> keySet() {
                return Set.of();
            }
        };
    }

    @Diff
    static PortDataComponentMap wrap(DataComponentMap delegate) {
        return new Delegate(delegate);
    }

    @Diff
    record Delegate(DataComponentMap delegate) implements PortDataComponentMap {
        @Override
        public <T> @Nullable T get(PortDataComponentType<T> type) {
            return delegate.get(type.unwrap());
        }

        @Override
        public Set<PortDataComponentType<?>> keySet() {
            return new ImmutableTransformSet<>(delegate.keySet(), PortDataComponentType::wrap);
        }

        @Override
        public DataComponentMap unwrap() {
            return delegate;
        }
    }

    class PortBuilder {
        private final Item.Properties properties;

        @Diff
        public PortBuilder(Item.Properties properties) {
            this.properties = properties;
        }

        public <T> PortBuilder set(PortDataComponentType<T> component, T value) {
            properties.component(component.unwrap(), value);
            return this;
        }
    }
}
