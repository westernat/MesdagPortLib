package org.mesdag.portlib.event.lifecycle;

import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFMLConstructModEvent extends PortParallelDispatchEvent<FMLConstructModEvent> {
    @Diff
    public PortFMLConstructModEvent(FMLConstructModEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
