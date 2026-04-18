package PortLib.extensions.net.minecraftforge.eventbus.api.Event;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

@Extension
public class PortEventExtension {
    public static class Result {
        @Diff
        public static PortTriState wrap(@This Event.Result thiz) {
            if (thiz.isAllowed()) return PortTriState.TRUE;
            if (thiz.isDenied()) return PortTriState.FALSE;
            return PortTriState.DEFAULT;
        }
    }
}
