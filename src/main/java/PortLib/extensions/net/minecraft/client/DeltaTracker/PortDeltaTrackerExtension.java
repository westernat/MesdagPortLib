package PortLib.extensions.net.minecraft.client.DeltaTracker;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.client.DeltaTracker;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;

@Extension
public class PortDeltaTrackerExtension {
    @Diff
    public static PortDeltaTicker wrap(@This DeltaTracker thiz) {
        PortDeltaTicker.INSTANCE.bind(thiz);
        return PortDeltaTicker.INSTANCE;
    }
}
