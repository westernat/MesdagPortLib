package org.mesdag.portlib.component;

import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
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

    static PortBuilder builder() {
        return new PortBuilder();
    }

    class PortBuilder {
        private final Map<PortDataComponentType<?>, Optional<?>> map = new IdentityHashMap<>();

        PortBuilder() {}

        public <T> PortBuilder set(PortDataComponentType<T> component, T value) {
            map.put(component, Optional.of(value));
            return this;
        }

        @Diff
        public Map<PortDataComponentType<?>, Optional<?>> getMap() {
            return map;
        }
    }
}
