package org.mesdag.portlib.event.lifecycle;

import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortInterModEnqueueEvent extends PortParallelDispatchEvent<InterModEnqueueEvent> {
    @Diff
    public PortInterModEnqueueEvent(InterModEnqueueEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
