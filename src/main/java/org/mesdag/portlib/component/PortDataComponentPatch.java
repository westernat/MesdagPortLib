package org.mesdag.portlib.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;

public class PortDataComponentPatch {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Reference2ObjectMap<PortDataComponentType<?>, Optional<?>> map = new Reference2ObjectArrayMap<>();

        Builder() {}

        public <T> Builder set(PortDataComponentType<T> component, T value) {

            this.map.put(component, Optional.of(value));
            return this;
        }

        public <T> Builder remove(PortDataComponentType<T> component) {
            this.map.put(component, Optional.empty());
            return this;
        }

        @Diff
        public Reference2ObjectMap<PortDataComponentType<?>, Optional<?>> build() {
            return map;
        }
    }
}
