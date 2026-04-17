package org.mesdag.portlib.event.entity.client.event;


import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterKeyMappingsEvent extends PortEvent {
    private final RegisterKeyMappingsEvent e;

    @Diff
    public PortRegisterKeyMappingsEvent(RegisterKeyMappingsEvent e) {
        super(e);
        this.e = e;
    }

    public void register(KeyMapping key) {
        e.register(key);
    }

    static {
        PortEventHooks.register(RegisterKeyMappingsEvent.class, PortRegisterKeyMappingsEvent.class, PortRegisterKeyMappingsEvent::new);
    }
}