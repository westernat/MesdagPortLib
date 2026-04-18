package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSpawnClusterSizeEvent extends PortLivingEvent<LivingPackSizeEvent> {
    @Diff
    public PortSpawnClusterSizeEvent(LivingPackSizeEvent e) {
        super(e);
    }

    public int getSize() {
        return e.getMaxPackSize();
    }

    public void setSize(int size) {
        e.setMaxPackSize(size);
    }

    @Override
    public Mob getEntity() {
        return (Mob) e.getEntity();
    }

    static {
        PortEventHooks.register();
    }
}
