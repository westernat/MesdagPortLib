package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.entity.living.PortLivingEvent;

public abstract class PortPlayerEvent extends PortLivingEvent {
    private final Player player;

    @Diff
    protected PortPlayerEvent(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public Player getEntity() {
        return player;
    }
}
