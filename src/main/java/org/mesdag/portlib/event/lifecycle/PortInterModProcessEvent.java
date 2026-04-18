package org.mesdag.portlib.event.lifecycle;

import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortInterModProcessEvent extends PortParallelDispatchEvent<InterModProcessEvent> {
    @Diff
    public PortInterModProcessEvent(InterModProcessEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
