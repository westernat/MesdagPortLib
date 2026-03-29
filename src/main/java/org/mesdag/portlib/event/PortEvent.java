package org.mesdag.portlib.event;

import net.neoforged.bus.api.Event;

public abstract class PortEvent<E extends Event> extends Event {
    protected final E e;

    public PortEvent(E e) {
        this.e = e;
    }

    E unwrap() {
        return e;
    }
}
