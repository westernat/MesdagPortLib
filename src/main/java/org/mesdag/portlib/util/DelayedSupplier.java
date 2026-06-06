package org.mesdag.portlib.util;

import java.util.function.Supplier;

public final class DelayedSupplier<T> implements Supplier<T> {
    public Supplier<T> delegate;

    @Override
    public T get() {
        return delegate.get();
    }
}
