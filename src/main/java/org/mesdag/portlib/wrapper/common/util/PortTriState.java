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

    public int unwrapState() {
        return switch (this) {
            case TRUE -> 1;
            case DEFAULT -> 0;
            case FALSE -> -1;
        };
    }

    public static PortTriState wrapState(int state) {
        if (state > 0) return TRUE;
        if (state < 0) return FALSE;
        return DEFAULT;
    }

    @Diff
    public TriState unwrap() {
        return switch (this) {
            case TRUE -> TriState.TRUE;
            case DEFAULT -> TriState.DEFAULT;
            case FALSE -> TriState.FALSE;
        };
    }

    @Diff
    public static PortTriState wrap(TriState state) {
        return switch (state) {
            case TRUE -> TRUE;
            case DEFAULT -> DEFAULT;
            case FALSE -> FALSE;
        };
    }
}
