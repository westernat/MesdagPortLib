package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.Timer;
import org.mesdag.portlib.client.PortDeltaTicker;

public interface IPortTimerExtension {
    private Timer self() {
        return (Timer) this;
    }

    default PortDeltaTicker wrap() {
        PortDeltaTicker.INSTANCE.bind(self());
        return PortDeltaTicker.INSTANCE;
    }

    static IPortTimerExtension of(Timer timer) {
        return (IPortTimerExtension) timer;
    }
}
