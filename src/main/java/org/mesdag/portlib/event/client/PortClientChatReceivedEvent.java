package org.mesdag.portlib.event.client;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.UUID;

public class PortClientChatReceivedEvent<E extends ClientChatReceivedEvent> extends PortEvent<E> implements IPortCancellableEvent {
    @Diff
    public PortClientChatReceivedEvent(E e) {
        super(e);
    }

    public Component getMessage() {
        return e.getMessage();
    }

    public void setMessage(Component message) {
        e.setMessage(message);
    }

    public @Nullable ChatType.Bound getBoundChatType() {
        return e.getBoundChatType();
    }

    public UUID getSender() {
        return e.getSender();
    }

    public boolean isSystem() {
        return e.isSystem();
    }

    static {
        PortEventHooks.register();
    }

    public static class Player extends PortClientChatReceivedEvent<ClientChatReceivedEvent.Player> {
        @Diff
        public Player(ClientChatReceivedEvent.Player e) {
            super(e);
        }

        public PlayerChatMessage getPlayerChatMessage() {
            return e.getPlayerChatMessage();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class System extends PortClientChatReceivedEvent<ClientChatReceivedEvent.System> {
        @Diff
        public System(ClientChatReceivedEvent.System e) {
            super(e);
        }

        public boolean isOverlay() {
            return e.isOverlay();
        }

        static {
            PortEventHooks.register();
        }
    }
}
