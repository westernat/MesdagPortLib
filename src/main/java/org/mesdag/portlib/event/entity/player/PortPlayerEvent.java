package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.event.entity.living.PortLivingEvent;

public abstract class PortPlayerEvent extends PortLivingEvent {
    @Override
    public abstract Player getEntity();
}
