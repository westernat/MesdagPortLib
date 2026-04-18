package org.mesdag.portlib.event.lifecycle;

import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFMLLoadCompleteEvent extends PortParallelDispatchEvent<FMLLoadCompleteEvent> {
    @Diff
    public PortFMLLoadCompleteEvent(FMLLoadCompleteEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
