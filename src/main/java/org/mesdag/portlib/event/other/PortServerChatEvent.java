package org.mesdag.portlib.event.other;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortServerChatEvent extends PortEvent<ServerChatEvent> implements IPortCancellableEvent {
    @Diff
    public PortServerChatEvent(ServerChatEvent e) {
        super(e);
    }

    public ServerPlayer getPlayer() {
        return e.getPlayer();
    }

    public String getUsername() {
        return e.getUsername();
    }

    public String getRawText() {
        return e.getRawText();
    }

    public Component getMessage() {
        return e.getMessage();
    }

    public void setMessage(Component message) {
        e.setMessage(message);
    }

    static {
        PortEventHooks.register();
    }
}
