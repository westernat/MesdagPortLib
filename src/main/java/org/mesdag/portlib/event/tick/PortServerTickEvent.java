package org.mesdag.portlib.event.tick;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

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
    }

    public static class PortPost extends PortServerTickEvent {
        @Diff
        public PortPost(TickEvent.ServerTickEvent e) {
            super(e);
        }
    }

    static {
        PortEventHooks.registerCombined(TickEvent.ServerTickEvent.class, List.of(
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
