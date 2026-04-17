package org.mesdag.portlib.event.entity.client.event;

import net.neoforged.neoforge.client.event.ClientChatEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortClientChatEvent extends PortEvent implements IPortCancellableEvent {
    private final ClientChatEvent e;

    @Diff
    public PortClientChatEvent(ClientChatEvent e) {
        super(e);
        this.e = e;
    }

    public String getMessage() {
        return e.getMessage();
    }

    public void setMessage(String message) {
        e.setMessage(message);
    }

    public String getOriginalMessage() {
        return e.getOriginalMessage();
    }

    static {
        PortEventHooks.register(ClientChatEvent.class, PortClientChatEvent.class, PortClientChatEvent::new);
    }
}