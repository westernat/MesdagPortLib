package org.mesdag.portlib.event.network;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortPayloadHandler;
import org.mesdag.portlib.wrapper.PortEnvironment;

public class PortRegisterPayloadHandlersEvent extends Event implements IModBusEvent {
    @Diff
    public PortRegisterPayloadHandlersEvent() {}

    public PortPayloadHandler registrar(String version) {
        return new PortPayloadHandler(PortEnvironment.getCallerModId(), version);
    }
}
