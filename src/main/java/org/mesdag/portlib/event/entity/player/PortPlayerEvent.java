package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.entity.living.PortLivingEvent;

public abstract class PortPlayerEvent<E extends PlayerEvent> extends PortLivingEvent<E> {
    @Diff
    protected PortPlayerEvent(E e) {
        super(e);
    }

    @Override
    public Player getEntity() {
        return e.getEntity();
    }
}
