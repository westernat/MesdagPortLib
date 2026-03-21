package org.mesdag.portlib.event.entity.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.neoforged.neoforge.event.entity.player.PlayerNegotiationEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.concurrent.Future;

public class PortPlayerNegotiationEvent extends PortEvent {
    private final PlayerNegotiationEvent e;

    @Diff
    public PortPlayerNegotiationEvent(PlayerNegotiationEvent e) {
        this.e = e;
    }

    public void enqueueWork(Runnable runnable) {
        e.enqueueWork(runnable);
    }

    public void enqueueWork(Future<Void> future) {
        e.enqueueWork(future);
    }

    public Connection getConnection() {
        return e.getConnection();
    }

    public GameProfile getProfile() {
        return e.getProfile();
    }

    static {
        PortEventHooks.register(PlayerNegotiationEvent.class, PortPlayerNegotiationEvent.class, PortPlayerNegotiationEvent::new);
    }
}
