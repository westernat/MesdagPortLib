package org.mesdag.portlib.event.lifecycle;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFMLCommonSetupEvent extends PortParallelDispatchEvent<FMLCommonSetupEvent> {
    @Diff
    public PortFMLCommonSetupEvent(FMLCommonSetupEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
