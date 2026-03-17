package org.mesdag.portlib.wrapper;

import net.neoforged.fml.LogicalSide;
import org.mesdag.portlib.diff.Diff;

public enum PortLogicalSide {
    CLIENT,
    SERVER;

    public boolean isServer() {
        return this == SERVER;
    }

    public boolean isClient() {
        return this == CLIENT;
    }

    @Diff
    public LogicalSide unwrap() {
        return this == CLIENT ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }
}
