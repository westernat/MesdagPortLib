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
        if (isTrue()) return 1;
        if (isFalse()) return -1;
        return 0;
    }

    public static PortTriState wrapState(int state) {
        if (state > 0) return TRUE;
        if (state < 0) return FALSE;
        return DEFAULT;
    }

    @Diff
    public TriState unwrap() {
        if (isTrue()) return TriState.TRUE;
        if (isFalse()) return TriState.FALSE;
        return TriState.DEFAULT;
    }
}
