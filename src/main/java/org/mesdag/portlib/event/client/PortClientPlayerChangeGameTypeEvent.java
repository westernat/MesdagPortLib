package org.mesdag.portlib.event.client;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.client.event.ClientPlayerChangeGameTypeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortClientPlayerChangeGameTypeEvent extends PortEvent<ClientPlayerChangeGameTypeEvent> {
    @Diff
    public PortClientPlayerChangeGameTypeEvent(ClientPlayerChangeGameTypeEvent e) {
        super(e);
    }

    public PlayerInfo getInfo() {
        return e.getInfo();
    }

    public GameType getCurrentGameType() {
        return e.getCurrentGameType();
    }

    public GameType getNewGameType() {
        return e.getNewGameType();
    }

    static {
        PortEventHooks.register();
    }
}
