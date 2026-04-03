package org.mesdag.portlib.event.entity.player;

import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.mesdag.portlib.diff.Diff;

public class PortPlayerHeartTypeEvent extends PlayerEvent {
    private final Gui.HeartType originalType;
    private Gui.HeartType type;

    @Diff
    public PortPlayerHeartTypeEvent(Player player, Gui.HeartType type) {
        super(player);
        this.type = type;
        this.originalType = type;
    }

    public Gui.HeartType getOriginalType() {
        return originalType;
    }

    public Gui.HeartType getType() {
        return type;
    }

    public void setType(Gui.HeartType type) {
        this.type = type;
    }
}
