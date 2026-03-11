package org.mesdag.portlib.event;

import net.minecraftforge.eventbus.api.EventPriority;

@SuppressWarnings("all")
public enum PortPriority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST;

    EventPriority unwrap() {
        return switch (this) {
            case HIGHEST -> EventPriority.HIGHEST;
            case HIGH -> EventPriority.HIGH;
            case NORMAL -> EventPriority.NORMAL;
            case LOW -> EventPriority.LOW;
            case LOWEST -> EventPriority.LOWEST;
        };
    }
}
