package org.mesdag.portlib.event.client;

import net.neoforged.neoforge.client.event.ClientPauseChangeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortClientPauseChangeEvent<E extends ClientPauseChangeEvent> extends PortEvent<E> {
    @Diff
    public PortClientPauseChangeEvent(E e) {
        super(e);
    }

    public boolean isPaused() {
        return e.isPaused();
    }

    public static class PortPre extends PortClientPauseChangeEvent<ClientPauseChangeEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(ClientPauseChangeEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortClientPauseChangeEvent<ClientPauseChangeEvent.Post> {
        @Diff
        public PortPost(ClientPauseChangeEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
