package PortLib.extensions.net.neoforged.neoforge.common.util.TriState;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.common.util.TriState;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

@Extension
public class PortTriStateExtension {
    @Diff
    public static PortTriState wrap(@This TriState thiz) {
        if (thiz.isTrue()) return PortTriState.TRUE;
        if (thiz.isFalse()) return PortTriState.FALSE;
        return PortTriState.DEFAULT;
    }
}
