package org.mesdag.portlib.event.registries;

import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterDataMapTypesEvent extends PortEvent<RegisterDataMapTypesEvent> implements IPortModBusEvent {
    @Diff
    public PortRegisterDataMapTypesEvent(RegisterDataMapTypesEvent e) {
        super(e);
    }

    public <T, R> void register(PortDataMapType<R, T> type) {
        e.register(type.unwrap());
    }

    static {
        PortEventHooks.register();
    }
}
