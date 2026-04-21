package org.mesdag.portlib.event.tick;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortServerTickEvent extends PortEvent<TickEvent.ServerTickEvent> {
    @Diff
    public PortServerTickEvent(TickEvent.ServerTickEvent e) {
        super(e);
    }

    public boolean hasTime() {
        return e.haveTime();
    }

    public MinecraftServer getServer() {
        return e.getServer();
    }

    public static class PortPre extends PortServerTickEvent {
        @Diff
        public PortPre(TickEvent.ServerTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ServerTickEvent.class, PortPre.class, PortPre::new, event -> event.phase == TickEvent.Phase.START);
        }
    }

    public static class PortPost extends PortServerTickEvent {
        @Diff
        public PortPost(TickEvent.ServerTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ServerTickEvent.class, PortPost.class, PortPost::new, event -> event.phase == TickEvent.Phase.END);
        }
    }
}
