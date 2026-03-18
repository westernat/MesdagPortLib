package org.mesdag.portlib.event;

import net.minecraftforge.eventbus.ListenerList;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.Final;

public class PortEvent extends Event {
    @Diff
    @Final
    @Override
    public boolean isCancelable() {
        return this instanceof IPortCancellableEvent;
    }

    @Diff
    @Final
    @Override
    public boolean hasResult() {
        return super.hasResult();
    }

    @Diff
    @Final
    @Override
    public Result getResult() {
        return super.getResult();
    }

    @Diff
    @Final
    @Override
    public ListenerList getListenerList() {
        return super.getListenerList();
    }

    @Diff
    @Final
    @Override
    public @Nullable EventPriority getPhase() {
        return super.getPhase();
    }

    @Diff
    @Final
    @Override
    public void setPhase(@NotNull EventPriority value) {
        super.setPhase(value);
    }
}
