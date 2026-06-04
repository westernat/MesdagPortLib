package PortLib.extensions.net.minecraft.client.Timer;

import net.minecraft.client.Timer;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;

public class PortTimerExtension {
    @Diff
    public static PortDeltaTicker wrap(Timer thiz) {
        PortDeltaTicker.INSTANCE.bind(thiz);
        return PortDeltaTicker.INSTANCE;
    }
}
