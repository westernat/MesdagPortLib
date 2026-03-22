package org.mesdag.portlib.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.mesdag.portlib.diff.Diff;

public class PortServerChatEvent extends PortEvent implements IPortCancellableEvent {

    private final ServerChatEvent e;

    @Diff
    public PortServerChatEvent(ServerChatEvent e) {
        super();
        this.e = e;
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
        PortEventHooks.register(ServerChatEvent.class, PortServerChatEvent.class, PortServerChatEvent::new);
    }
}