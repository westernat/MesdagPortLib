package org.mesdag.portlib.event.tick;

import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortServerTickEvent<E extends ServerTickEvent> extends PortEvent<E> {
    @Diff
    public PortServerTickEvent(E e) {
        super(e);
    }

    public boolean hasTime() {
        return e.hasTime();
    }

    public MinecraftServer getServer() {
        return e.getServer();
    }

    public static class PortPre extends PortServerTickEvent<ServerTickEvent.Pre> {
        @Diff
        public PortPre(ServerTickEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register(ServerTickEvent.Pre.class, PortPre.class, PortPre::new);
        }
    }

    public static class PortPost extends PortServerTickEvent<ServerTickEvent.Post> {
        @Diff
        public PortPost(ServerTickEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register(ServerTickEvent.Post.class, PortPost.class, PortPost::new);
        }
    }
}
