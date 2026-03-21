package org.mesdag.portlib.event.entity.player;

import net.minecraft.client.gui.Gui;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import org.mesdag.portlib.diff.Diff;

public class PortPlayerHeartTypeEvent extends PortPlayerEvent {
    private final PlayerHeartTypeEvent e;

    @Diff
    public PortPlayerHeartTypeEvent(PlayerHeartTypeEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public Gui.HeartType getOriginalType() {
        return e.getOriginalType();
    }

    public Gui.HeartType getType() {
        return e.getType();
    }

    public void setType(Gui.HeartType type) {
        e.setType(type);
    }
}
