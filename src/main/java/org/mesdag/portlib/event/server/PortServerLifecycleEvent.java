package org.mesdag.portlib.event.server;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;

public abstract class PortServerLifecycleEvent<E extends ServerLifecycleEvent> extends PortEvent<E> {
    @Diff
    public PortServerLifecycleEvent(E e) {
        super(e);
    }

    public MinecraftServer getServer() {
        return e.getServer();
    }
}
