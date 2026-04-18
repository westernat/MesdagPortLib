package org.mesdag.portlib.event.tick;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public abstract class PortPlayerTickEvent<E extends PlayerTickEvent> extends PortPlayerEvent<E> {
    @Diff
    public PortPlayerTickEvent(E e) {
        super(e);
    }

    public static class PortPre extends PortPlayerTickEvent<PlayerTickEvent.Pre> {
        @Diff
        public PortPre(PlayerTickEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortPlayerTickEvent<PlayerTickEvent.Post> {
        @Diff
        public PortPost(PlayerTickEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
