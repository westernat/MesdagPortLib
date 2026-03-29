package org.mesdag.portlib.component;

import net.minecraft.core.component.DataComponentPatch;
import org.mesdag.portlib.diff.Diff;

public class PortDataComponentPatch$Builder {
    private final DataComponentPatch.Builder delegate;

    PortDataComponentPatch$Builder(DataComponentPatch.Builder delegate) {
        this.delegate = delegate;
    }

    public <T> PortDataComponentPatch$Builder set(PortDataComponentType<T> component, T value) {
        delegate.set(component.unwrap(), value);
        return this;
    }

    public <T> PortDataComponentPatch$Builder remove(PortDataComponentType<T> component) {
        delegate.remove(component.unwrap());
        return this;
    }

    @Diff
    public DataComponentPatch.Builder unwrap() {
        return delegate;
    }

    @Diff
    public static PortDataComponentPatch$Builder wrap(DataComponentPatch.Builder delegate) {
        return new PortDataComponentPatch$Builder(delegate);
    }
}
