package org.mesdag.portlib.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import org.mesdag.portlib.diff.Diff;

import javax.annotation.Nullable;
import java.util.stream.Stream;


public class PortOnDatapackSyncEvent extends PortEvent {

    private final OnDatapackSyncEvent e;

    @Diff
    public PortOnDatapackSyncEvent(OnDatapackSyncEvent e) {
        super();
        this.e = e;
    }

    public PlayerList getPlayerList() {
        return e.getPlayerList();
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return e.getPlayer();
    }

    public Stream<ServerPlayer> getRelevantPlayers() {
        return e.getRelevantPlayers();
    }

    static {
        PortEventHooks.register(OnDatapackSyncEvent.class, PortOnDatapackSyncEvent.class, PortOnDatapackSyncEvent::new);
    }
}