package org.mesdag.portlib.event.entity;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityLeaveLevelEvent extends PortEntityEvent<EntityLeaveLevelEvent> {
    @Diff
    public PortEntityLeaveLevelEvent(EntityLeaveLevelEvent e) {
        super(e);
    }

    public Level getLevel() {
        return e.getLevel();
    }

    static {PortEventHooks.register(EntityLeaveLevelEvent.class, PortEntityLeaveLevelEvent.class, PortEntityLeaveLevelEvent::new);
    }
}
