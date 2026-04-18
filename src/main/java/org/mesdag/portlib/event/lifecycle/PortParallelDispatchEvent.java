package org.mesdag.portlib.event.lifecycle;

import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import org.mesdag.portlib.diff.Diff;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class PortParallelDispatchEvent<E extends ParallelDispatchEvent> extends PortModLifecycleEvent<E> {
    @Diff
    public PortParallelDispatchEvent(E e) {
        super(e);
    }

    public CompletableFuture<Void> enqueueWork(Runnable work) {
        return e.enqueueWork(work);
    }

    public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
        return e.enqueueWork(work);
    }
}
