package org.mesdag.portlib.event.client;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortClientTickEvent<E extends ClientTickEvent> extends PortEvent<E> {
    @Diff
    public PortClientTickEvent(E e) {
        super(e);
    }

    public static class PortPre extends PortClientTickEvent<ClientTickEvent.Pre> {
        @Diff
        public PortPre(ClientTickEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortClientTickEvent<ClientTickEvent.Post> {
        @Diff
        public PortPost(ClientTickEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
