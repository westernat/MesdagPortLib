package org.mesdag.portlib.event;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public interface IPortCancellableEvent {
    @MustBeInvokedByOverriders
    default void setCanceled(boolean canceled) {
        ((PortEvent) this).setCanceled(canceled);
    }

    @ApiStatus.NonExtendable
    default boolean isCanceled() {
        return ((PortEvent) this).isCanceled();
    }
}
