package org.mesdag.portlib.event;

import org.jetbrains.annotations.ApiStatus;

public interface IPortCancellableEvent {
    @ApiStatus.NonExtendable
    default void setCanceled(boolean canceled) {
        ((PortEvent<?>) this).setCanceled(canceled);
        ((PortEvent<?>) this).unwrap().setCanceled(canceled);
    }

    @ApiStatus.NonExtendable
    default boolean isCanceled() {
        return ((PortEvent<?>) this).isCanceled();
    }
}
