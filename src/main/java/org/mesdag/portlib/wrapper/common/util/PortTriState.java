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
    public Event.Result unwrapResult() {
        return switch (this) {
            case TRUE -> Event.Result.ALLOW;
            case DEFAULT -> Event.Result.DEFAULT;
            case FALSE -> Event.Result.DENY;
        };
    }

    @Diff
    public static PortTriState wrapResult(Event.Result result) {
        return switch (result) {
            case ALLOW -> TRUE;
            case DEFAULT -> DEFAULT;
            case DENY -> FALSE;
        };
    }
}
