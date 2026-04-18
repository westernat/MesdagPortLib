package org.mesdag.portlib.event.entity;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityJoinLevelEvent extends PortEntityEvent<EntityJoinLevelEvent> {
    @Diff
    public PortEntityJoinLevelEvent(EntityJoinLevelEvent e) {
        super(e);
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public boolean loadedFromDisk() {
        return e.loadedFromDisk();
    }

    static {
        PortEventHooks.register();
    }
}
