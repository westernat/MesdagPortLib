package PortLib.extensions.net.minecraft.client.Timer;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.client.Timer;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;

@Extension
public class PortTimerExtension {
    @Diff
    public static PortDeltaTicker wrap(@This Timer thiz) {
        PortDeltaTicker.INSTANCE.bind(thiz);
        return PortDeltaTicker.INSTANCE;
    }
}
