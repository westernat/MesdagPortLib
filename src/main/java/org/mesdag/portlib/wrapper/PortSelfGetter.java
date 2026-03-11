package org.mesdag.portlib.wrapper;

@SuppressWarnings("all")
public interface PortSelfGetter<T> {
    default T portlib$self() {
        return (T) this;
    }
}
