package org.mesdag.portlib.event;

import net.minecraftforge.eventbus.ListenerList;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.ApiStatus;
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
    @ApiStatus.NonExtendable
    @Override
    public boolean isCancelable() {
        return this instanceof IPortCancellableEvent;
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public boolean hasResult() {
        return super.hasResult();
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public Result getResult() {
        return super.getResult();
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public ListenerList getListenerList() {
        return super.getListenerList();
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public @Nullable EventPriority getPhase() {
        return super.getPhase();
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public void setPhase(@NotNull EventPriority value) {
        super.setPhase(value);
    }
}
