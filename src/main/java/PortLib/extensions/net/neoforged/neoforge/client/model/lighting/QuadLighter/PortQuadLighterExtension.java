package PortLib.extensions.net.neoforged.neoforge.client.model.lighting.QuadLighter;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.client.model.lighting.QuadLighter;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.client.model.lighting.PortQuadLighter;

@Extension
public class PortQuadLighterExtension {
    @Diff
    public static PortQuadLighter wrap(@This QuadLighter thiz) {
        return new PortQuadLighter(thiz);
    }
}
