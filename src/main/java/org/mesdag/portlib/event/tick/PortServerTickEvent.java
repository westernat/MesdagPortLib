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

    public static class Pre extends PortServerTickEvent {
        @Diff
        public Pre(TickEvent.ServerTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ServerTickEvent.class, Pre.class, Pre::new, event -> event.phase == TickEvent.Phase.START);
        }
    }

    public static class Post extends PortServerTickEvent {
        @Diff
        public Post(TickEvent.ServerTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ServerTickEvent.class, Post.class, Post::new, event -> event.phase == TickEvent.Phase.END);
        }
    }
}
