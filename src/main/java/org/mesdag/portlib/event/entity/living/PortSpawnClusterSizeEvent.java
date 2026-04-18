package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.entity.living.SpawnClusterSizeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSpawnClusterSizeEvent extends PortLivingEvent<SpawnClusterSizeEvent> {
    @Diff
    public PortSpawnClusterSizeEvent(SpawnClusterSizeEvent e) {
        super(e);
    }

    public int getSize() {
        return e.getSize();
    }

    public void setSize(int size) {
        e.setSize(size);
    }

    @Override
    public Mob getEntity() {
        return e.getEntity();
    }

    static {
        PortEventHooks.register();
    }
}
