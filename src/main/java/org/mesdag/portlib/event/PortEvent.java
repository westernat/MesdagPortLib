package org.mesdag.portlib.event;

import net.minecraftforge.eventbus.ListenerList;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public class PortEvent<E extends Event> extends Event {
    protected final E e;

    public PortEvent(E e) {
        this.e = e;
    }

    E unwrap() {
        return e;
    }

    @Diff
    @Override
    public final boolean isCancelable() {
        return this instanceof IPortCancellableEvent;
    }

    @Diff
    @Override
    public final boolean hasResult() {
        return super.hasResult();
    }

    @Diff
    @Override
    public final Result getResult() {
        return super.getResult();
    }

    @Diff
    @Override
    public final ListenerList getListenerList() {
        return super.getListenerList();
    }

    @Diff
    @Override
    public final @Nullable EventPriority getPhase() {
        return super.getPhase();
    }

    @Diff
    @Override
    public final void setPhase(@NotNull EventPriority value) {
        super.setPhase(value);
    }
}
