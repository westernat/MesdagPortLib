package org.mesdag.portlib.event.other;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;
import java.util.stream.Stream;


public class PortOnDatapackSyncEvent extends PortEvent<OnDatapackSyncEvent> {
    @Diff
    public PortOnDatapackSyncEvent(OnDatapackSyncEvent e) {
        super(e);
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
        PortEventHooks.register();
    }
}
