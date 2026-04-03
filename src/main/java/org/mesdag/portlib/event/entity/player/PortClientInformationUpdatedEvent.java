package org.mesdag.portlib.event.entity.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.server.level.PortClientInformation;

public class PortClientInformationUpdatedEvent extends PlayerEvent {
    private final PortClientInformation oldInformation;
    private final PortClientInformation updatedInformation;

    @Diff
    public PortClientInformationUpdatedEvent(ServerPlayer player, PortClientInformation oldInfo, PortClientInformation newInfo) {
        super(player);
        this.oldInformation = oldInfo;
        this.updatedInformation = newInfo;
    }

    @Override
    public ServerPlayer getEntity() {
        return (ServerPlayer) super.getEntity();
    }

    public PortClientInformation getUpdatedInformation() {
        return updatedInformation;
    }

    public PortClientInformation getOldInformation() {
        return oldInformation;
    }
}
