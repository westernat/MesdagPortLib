package PortLib.extensions.net.minecraftforge.client.model.lighting.QuadLighter;

import net.minecraftforge.client.model.lighting.QuadLighter;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.client.model.lighting.PortQuadLighter;

public class PortQuadLighterExtension {
    @Diff
    public static PortQuadLighter wrap(QuadLighter thiz) {
        return new PortQuadLighter(thiz);
    }
}
