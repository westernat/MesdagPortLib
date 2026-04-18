package org.mesdag.portlib.event.client;

import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortClientTickEvent extends PortEvent<TickEvent.ClientTickEvent> {
    @Diff
    public PortClientTickEvent(TickEvent.ClientTickEvent e) {
        super(e);
    }

    public static class PortPre extends PortClientTickEvent {
        @Diff
        public PortPre(TickEvent.ClientTickEvent e) {
            super(e);
        }
    }

    public static class PortPost extends PortClientTickEvent {
        @Diff
        public PortPost(TickEvent.ClientTickEvent e) {
            super(e);
        }
    }

    static {
        PortEventHooks.registerCombined(TickEvent.ClientTickEvent.class, List.of(
                PortPre.class,
                PortPost.class
        ), e -> {
            if (e.phase == TickEvent.Phase.START) {
                return new PortPre(e);
            }
            return new PortPost(e);
        });
    }
}
