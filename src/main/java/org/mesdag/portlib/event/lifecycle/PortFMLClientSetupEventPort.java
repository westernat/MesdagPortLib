package org.mesdag.portlib.event.lifecycle;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFMLClientSetupEventPort extends PortParallelDispatchEvent<FMLClientSetupEvent> {
    @Diff
    public PortFMLClientSetupEventPort(FMLClientSetupEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
