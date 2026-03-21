package org.mesdag.portlib.event.entity.item;

import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortItemExpireEvent extends PortItemEvent {
    private final ItemExpireEvent e;

    @Diff
    public PortItemExpireEvent(ItemExpireEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public int getExtraLife() {
        return e.getExtraLife();
    }

    public void setExtraLife(int extraLife) {
        e.setExtraLife(extraLife);
    }

    public void addExtraLife(int extraLife) {
        e.addExtraLife(extraLife);
    }

    static {
        PortEventHooks.register(ItemExpireEvent.class, PortItemExpireEvent.class, PortItemExpireEvent::new);
    }
}
