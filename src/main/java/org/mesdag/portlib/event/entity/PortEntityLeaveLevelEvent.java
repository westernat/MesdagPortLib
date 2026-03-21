package org.mesdag.portlib.event.entity;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityLeaveLevelEvent extends PortEntityEvent {
    private final EntityLeaveLevelEvent e;

    @Diff
    public PortEntityLeaveLevelEvent(EntityLeaveLevelEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public Level getLevel() {
        return e.getLevel();
    }

    static {PortEventHooks.register(EntityLeaveLevelEvent.class, PortEntityLeaveLevelEvent.class, PortEntityLeaveLevelEvent::new);
    }
}