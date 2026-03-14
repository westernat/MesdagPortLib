package org.mesdag.portlib.network;

import net.neoforged.neoforge.network.connection.ConnectionType;
import org.mesdag.portlib.diff.Diff;

public enum PortConnectionType {
    MODDED,
    VANILLA;

    @Diff
    public ConnectionType unwrap() {
        return switch (this) {
            case MODDED -> ConnectionType.NEOFORGE;
            case VANILLA -> ConnectionType.OTHER;
        };
    }

    @Diff
    public static PortConnectionType wrap(ConnectionType connectionType) {
        if (connectionType == ConnectionType.NEOFORGE) {
            return MODDED;
        }
        return VANILLA;
    }
}
