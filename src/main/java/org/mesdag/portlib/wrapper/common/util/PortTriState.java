package org.mesdag.portlib.wrapper.common.util;

import net.minecraftforge.eventbus.api.Event;
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
    public Event.Result unwrapResult() {
        if (isTrue()) return Event.Result.ALLOW;
        if (isFalse()) return Event.Result.DENY;
        return Event.Result.DEFAULT;
    }
}
