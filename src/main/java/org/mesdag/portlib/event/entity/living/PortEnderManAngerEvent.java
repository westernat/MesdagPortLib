package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortEnderManAngerEvent extends PortLivingEvent<EnderManAngerEvent> implements IPortCancellableEvent {
    @Diff
    public PortEnderManAngerEvent(EnderManAngerEvent e) {
        super(e);
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    @Override
    public EnderMan getEntity() {
        return e.getEntity();
    }

    static {
        PortEventHooks.register();
    }
}
