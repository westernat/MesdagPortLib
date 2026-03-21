package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.entity.living.MobSplitEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortMobSplitEvent extends PortEvent implements IPortCancellableEvent {
    private final MobSplitEvent e;

    @Diff
    public PortMobSplitEvent(MobSplitEvent e) {
        this.e = e;
    }

    public Mob getParent() {
        return e.getParent();
    }

    public List<Mob> getChildren() {
        return e.getChildren();
    }

    @Override
    public void setCanceled(boolean canceled) {
        IPortCancellableEvent.super.setCanceled(canceled);
        e.setCanceled(canceled);
    }

    static {
        PortEventHooks.register(MobSplitEvent.class, PortMobSplitEvent.class, PortMobSplitEvent::new);
    }
}
