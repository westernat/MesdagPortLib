package org.mesdag.portlib.network;

import net.minecraftforge.network.ConnectionType;
import org.mesdag.portlib.diff.Diff;

public enum PortConnectionType {
    MODDED,
    VANILLA;

    @Diff
    public ConnectionType unwrap() {
        return switch (this) {
            case MODDED -> ConnectionType.MODDED;
            case VANILLA -> ConnectionType.VANILLA;
        };
    }
}
