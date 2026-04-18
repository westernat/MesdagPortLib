package org.mesdag.portlib.event.client;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterClientReloadListenersEvent extends PortEvent<RegisterClientReloadListenersEvent> {
    @Diff
    public PortRegisterClientReloadListenersEvent(RegisterClientReloadListenersEvent e) {
        super(e);
    }

    public void registerReloadListener(PreparableReloadListener reloadListener) {
        e.registerReloadListener(reloadListener);
    }

    static {
        PortEventHooks.register();
    }
}
