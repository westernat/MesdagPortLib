package org.mesdag.portlib.wrapper.common.util;

import net.neoforged.neoforge.common.util.TriState;
import org.mesdag.portlib.diff.Diff;

public enum PortTriState {
    TRUE,
    DEFAULT,
    FALSE;

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isDefault() {
        return this == DEFAULT;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    @Diff
    public TriState unwrap() {
        return switch (this) {
            case TRUE -> TriState.TRUE;
            case DEFAULT -> TriState.DEFAULT;
            case FALSE -> TriState.FALSE;
        };
    }
}
