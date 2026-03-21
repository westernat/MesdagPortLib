package org.mesdag.portlib.event.entity.player;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.ClientInformationUpdatedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortClientInformationUpdatedEvent extends PortPlayerEvent {
    private final ClientInformationUpdatedEvent e;

    @Diff
    public PortClientInformationUpdatedEvent(ClientInformationUpdatedEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    @Override
    public ServerPlayer getEntity() {
        return e.getEntity();
    }

    public ClientInformation getUpdatedInformation() {
        return e.getUpdatedInformation();
    }

    public ClientInformation getOldInformation() {
        return e.getOldInformation();
    }

    static {
        PortEventHooks.register(ClientInformationUpdatedEvent.class, PortClientInformationUpdatedEvent.class, PortClientInformationUpdatedEvent::new);
    }
}
