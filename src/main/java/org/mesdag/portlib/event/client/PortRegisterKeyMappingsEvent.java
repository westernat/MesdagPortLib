package org.mesdag.portlib.event.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterKeyMappingsEvent extends PortEvent<RegisterKeyMappingsEvent> {
    @Diff
    public PortRegisterKeyMappingsEvent(RegisterKeyMappingsEvent e) {
        super(e);
    }

    public void register(KeyMapping key) {
        e.register(key);
    }

    static {
        PortEventHooks.register();
    }
}
