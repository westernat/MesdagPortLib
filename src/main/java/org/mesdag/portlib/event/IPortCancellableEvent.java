package org.mesdag.portlib.event;

import net.neoforged.bus.api.ICancellableEvent;
import org.jetbrains.annotations.ApiStatus;

public interface IPortCancellableEvent extends ICancellableEvent {
    @ApiStatus.NonExtendable
    default void setCanceled(boolean canceled) {
        ICancellableEvent.super.setCanceled(canceled);
        ((ICancellableEvent) ((PortEvent<?>) this).unwrap()).setCanceled(canceled);
    }
}
