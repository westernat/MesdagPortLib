package org.mesdag.portlib.event.lifecycle;

import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFMLDedicatedServerSetupEvent extends PortParallelDispatchEvent<FMLDedicatedServerSetupEvent> {
    @Diff
    public PortFMLDedicatedServerSetupEvent(FMLDedicatedServerSetupEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
