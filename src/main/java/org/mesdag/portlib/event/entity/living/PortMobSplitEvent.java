package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;

import java.util.List;

@Cancelable
public class PortMobSplitEvent extends Event {
    protected final Mob parent;
    protected final List<Mob> children;

    @Diff
    public PortMobSplitEvent(Mob parent, List<Mob> children) {

        this.parent = parent;
        this.children = children;
    }

    public Mob getParent() {
        return parent;
    }

    public List<Mob> getChildren() {
        return children;
    }

    @Diff
    public static PortMobSplitEvent onMobSplit(Mob parent, List<Mob> children) {
        var event = new PortMobSplitEvent(parent, children);
        PortEventHandler.postEvent(event);
        return event;
    }
}
