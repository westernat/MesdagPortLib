package org.mesdag.portlib.event.entity;

import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityMobGriefingEvent extends PortEntityEvent<EntityMobGriefingEvent> {
    @Diff
    public PortEntityMobGriefingEvent(EntityMobGriefingEvent e) {
        super(e);
    }

    public boolean isMobGriefingEnabled() {
        return e.isMobGriefingEnabled();
    }

    public void setCanGrief(boolean canGrief) {
        e.setCanGrief(canGrief);
    }

    public boolean canGrief() {
        return e.canGrief();
    }

    static {
        PortEventHooks.register();
    }
}
