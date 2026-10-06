package org.mesdag.portlib.event.lifecycle;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFMLClientSetupEvent extends PortParallelDispatchEvent<FMLClientSetupEvent> {
    @Diff
    public PortFMLClientSetupEvent(FMLClientSetupEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
