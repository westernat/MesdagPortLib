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

    /**
     * 同步取消包装事件与底层加载器事件。
     *
     * <p>{@link Event} 已经声明了同名实例方法，因此 Java 不会调用
     * {@link IPortCancellableEvent} 中的默认实现。统一在包装事件基类中回写，
     * 才能保证监听者取消 PortLib 事件时，原始 Forge 事件也真正停止执行。</p>
     */
    @Diff
    @ApiStatus.NonExtendable
    @Override
    public void setCanceled(boolean canceled) {
        super.setCanceled(canceled);
        e.setCanceled(canceled);
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
