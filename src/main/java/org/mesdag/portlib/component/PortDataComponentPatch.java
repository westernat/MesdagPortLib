package org.mesdag.portlib.component;

import net.minecraft.core.component.DataComponentPatch;
import org.mesdag.portlib.diff.Diff;

public class PortDataComponentPatch {
    public static PortBuilder builder() {
        return new PortBuilder(DataComponentPatch.builder());
    }

    public static class PortBuilder {
        private final DataComponentPatch.Builder delegate;

        PortBuilder(DataComponentPatch.Builder delegate) {
            this.delegate = delegate;
        }

        public <T> PortBuilder set(PortDataComponentType<T> component, T value) {
            delegate.set(component.unwrap(), value);
            return this;
        }

        public <T> PortBuilder remove(PortDataComponentType<T> component) {
            delegate.remove(component.unwrap());
            return this;
        }

        @Diff
        public DataComponentPatch.Builder unwrap() {
            return delegate;
        }

        @Diff
        public static PortBuilder wrap(DataComponentPatch.Builder delegate) {
            return new PortBuilder(delegate);
        }
    }
}
